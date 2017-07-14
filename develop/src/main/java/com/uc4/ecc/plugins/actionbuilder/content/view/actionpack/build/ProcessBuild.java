package com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.build;

import java.io.File;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.Version;

import com.automic.apm.CompositeValidationWarning;
import com.automic.apm.ValidationWarning;
import com.automic.apm.models.BuildFormat;
import com.automic.apm.models.Pack;
import com.automic.apm.tasks.BuildPack;
import com.google.common.eventbus.EventBus;
import com.google.common.io.Files;
import com.uc4.UCVersion;
import com.uc4.ecc.framework.commons.utils.componentfactories.Buttons;
import com.uc4.ecc.framework.commons.utils.componentfactories.ExtendedLabel;
import com.uc4.ecc.framework.commons.utils.componentfactories.Labels;
import com.uc4.ecc.framework.core.context.Context;
import com.uc4.ecc.framework.core.interfaces.IText;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actionbuilder.utils.ActionBuilderConstants.METADATA;
import com.uc4.ecc.plugins.actionbuilder.utils.PluginInfo;
import com.uc4.ecc.plugins.actioncommon.apm.EncapsuleTask;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.RealtimeWizardLoading;
import com.uc4.ecc.plugins.actioncommon.utils.DateTimeUtils;
import com.uc4.ecc.plugins.actioncommon.utils.HTMLUtils;
import com.uc4.ecc.plugins.actioncommon.utils.UIException;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public final class ProcessBuild extends RealtimeWizardLoading<BuildWizard> implements EncapsuleTask<BuildPack> {

	private final ExtendedLabel label;
	private final VerticalLayout buildOutputLayout;
	private Downloader downloader;
	private boolean hasValidationWarning = false;
	private String warningMessage;

	protected ProcessBuild(BuildWizard wizard, Pack pack) {
		super(wizard);
		this.label = Labels.HTML.create(ActionBuilderMessages.LABEL_COMMON_PROCESS);
		this.buildOutputLayout = new VerticalLayout();
	}

	@Override
	public void onStart() {
		this.parent().getPresenter().getMasterPresenter().getOperation().execute(this);
	}

	@Override
	public void onError(Exception ex) {
		this.wizard.navigateTo(
				convertToErrorStep(
						ex,
						ActionBuilderMessages.BUILD_PACK_ERROR.get(),
						ActionBuilderMessages.BUILD_PACK_WARNING_EXPORT_AGAIN.get()));
	}

	@Override
	public void onFinish() {
		if (hasValidationWarning) {
			onWarning();
			hasValidationWarning = false;
		} else {
			Button buildPackBtn = Buttons.Default.create(ActionBuilderMessages.BUTTON_DOWNLOAD);
			// TODO tricky
			this.wizard.setFooterButtons(buildPackBtn, this.wizard.getCancelButton());
			buildPackBtn.addClickListener(event -> onExportPack());
			this.loading.setVisible(false);
			this.label.setValue(ActionBuilderMessages.BUILD_PACK_SUCCESS);
			this.buildOutputLayout.removeAllComponents();
			this.buildOutputLayout.addComponent(new Label(this.downloader.getFilename()));
			this.buildOutputLayout.setVisible(true);
			this.wizard.getNextButton().setVisible(true);
			this.wizard.getCancelButton().setEnabled(true);
		}
	}
	
	private void onExportPack() {
		Context.getInjectableInstance(EventBus.class).post(new DownloadEvent(this.downloader));
		this.wizard.close();
	}

	public void onWarning() {
		this.label.setValue(ActionBuilderMessages.BUILD_PACK_WARNING);
		this.wizard.getCancelButton().setEnabled(true);
		this.loading.setVisible(false);
		Panel warningPanel = new Panel();
		warningPanel.setHeight(180, Unit.PIXELS);
		VerticalLayout layout = new VerticalLayout();
		layout.setWidth(100, Unit.PERCENTAGE);
		layout.setHeightUndefined();

		warningPanel.setContent(layout);
		this.buildOutputLayout.addComponent(warningPanel);
		this.buildOutputLayout.setVisible(true);

		this.wizard.getNextButton().setVisible(true);
		this.wizard.getNextButton().setCaption(ActionBuilderMessages.BUTTON_IGNORE.get());

		if (!StringUtils.isBlank(this.warningMessage)) {
			layout.addComponent(new Label(this.warningMessage, ContentMode.HTML));
		}

		this.buildOutputLayout.addComponent(new Label(HTMLUtils.br(), ContentMode.HTML));
		this.buildOutputLayout.addComponent(Labels.HTML.create(ActionBuilderMessages.BUILD_PACK_WARNING_EXPORT_IGNORE));
	}

	@Override
	public AbstractLayout getContent() {
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSizeFull();
		this.buildOutputLayout.setSizeFull();
		verticalLayout.addComponents(this.label, new Label(HTMLUtils.hr(), ContentMode.HTML), this.loading, this.buildOutputLayout);
		verticalLayout.setComponentAlignment(this.loading, Alignment.MIDDLE_CENTER);
		return verticalLayout;
	}

	@Override
	public boolean isAlreadyFocus() {
		return false;
	}

	@Override
	public IText getStateTitle() {
		return null;
	}

	@Override
	public void onNext() {
		// TODO check!!!
		onFinish();
	}

	@Override
	public void onCancel() {
		if (this.downloader == null || this.downloader.getFile() == null) {
			return;
		}
		final File file = this.downloader.getFile();
		Thread thread = new Thread(() -> {
			file.delete();
		});
		thread.start();
	}

	@Override
	public void onInit() {
		this.wizard.getNextButton().setVisible(false);
		this.wizard.getCancelButton().setEnabled(false);
		this.buildOutputLayout.setVisible(false);
		this.start();
	}

	@Override
	public BuildPack getTask() {
		File temp = Files.createTempDir();
		BuildPack buildTask = new BuildPack(this.wizard.getPresenter().getBean());
		buildTask.setBuildFormat(BuildFormat.ZIP);
		buildTask.setTargetPath(temp.getAbsolutePath());
		buildTask.setOverwriteMetadata(this.generateMetadata());
		return buildTask;
	}

	@Override
	public void afterSuccess(BuildPack task) {
		this.downloader = new Downloader(task.getOutputFile());
		if (!task.getState().getValidationWarnings().isEmpty()) {
			this.hasValidationWarning = true;
			warningMessage = formatValidationWarning(task.getState().getValidationWarnings());
		}
	}

	private String formatValidationWarning(List<ValidationWarning> validationWarnings) {
		Map<String, Map<String, List<String>>> mapWarning = new HashMap<>();
		for (ValidationWarning validationWarning : validationWarnings) {
			if (CompositeValidationWarning.class.isInstance(validationWarning)) {

				CompositeValidationWarning buildWarning = (CompositeValidationWarning) validationWarning;
				if (buildWarning.getSubMessageMap() != null && !buildWarning.isEmpty()) {
					mapWarning.put(buildWarning.getMessage(), buildWarning.getSubMessageMap());
				}
			}
		}
		return HTMLUtils.ul(mapWarning, false);
	}

	@Override
	public void afterError(UIException exception) {
		throw exception;
	}

	private String getBundleVersion(Class<?> className) {
		String version = PluginInfo.VERSION;
		Bundle bundle = FrameworkUtil.getBundle(className);

		if (bundle == null || bundle.getVersion() == Version.emptyVersion)
			return version;

		Dictionary<?, ?> headers = bundle.getHeaders();
		String buildID = (String) headers.get("Ecc-BuildID");
		if (!StringUtils.isBlank(buildID)) {
			version = bundle.getVersion().toString() + buildID;
		}

		return version;
	}

	private Map<String, String> generateMetadata() {
		Map<String, String> metadata = new LinkedHashMap<String, String>();
		metadata.put(PluginInfo.generateMetadata(METADATA.VERSION_KEY), getBundleVersion(this.getClass()));
		metadata.put(PluginInfo.generateMetadata(METADATA.LIBRARY_KEY), UCVersion.VERSION);
		metadata.put(
				PluginInfo.generateMetadata(METADATA.EXPORTED_KEY),
				DateTimeUtils.formatCurrentTime(DateTimeUtils.DATE_TIME_ZONE_FORMAT));
		return metadata;
	}
}
