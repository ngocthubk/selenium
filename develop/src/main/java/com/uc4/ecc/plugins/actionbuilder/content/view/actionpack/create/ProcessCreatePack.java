package com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.create;

import com.automic.apm.models.Pack;
import com.automic.apm.models.templates.DefaultPackTemplate;
import com.automic.apm.tasks.ScaffoldPack;
import com.uc4.ecc.framework.core.interfaces.IText;
import com.uc4.ecc.plugins.actionbuilder.entrypoint.ActionBuilderPluginInstance;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.apm.EncapsuleTask;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.RealtimeWizardLoading;
import com.uc4.ecc.plugins.actioncommon.utils.UIException;

public final class ProcessCreatePack extends RealtimeWizardLoading<CreatePackDialog> implements EncapsuleTask<ScaffoldPack> {

	private String title;
	private String name;
	private Pack pack;

	protected ProcessCreatePack(CreatePackDialog wizard) {
		super(wizard);
	}

	protected void setTitle(String title) {
		this.title = title;
	}

	protected void setName(String name) {
		this.name = name;
	}

	@Override
	public IText getStateTitle() {
		return ActionBuilderMessages.LABEL_COMMON_PROCESS;
	}

	@Override
	public void onInit() {
		this.wizard.getCancelButton().setEnabled(false);
		this.start();
	}

	@Override
	public void onStart() {
		this.wizard.getPresenter().getMasterPresenter().getOperation().execute(this);
	}

	@Override
	public void onError(Exception ex) {
		this.wizard.getCancelButton().setEnabled(true);
		this.wizard.getCancelButton().click();
		this.wizard.getPresenter().notifyError(ActionBuilderMessages.CREATE_PACK_TOAST_ERROR.get(), ex);
	}

	@Override
	public void onFinish() {
		this.wizard.getCancelButton().setEnabled(true);
		this.wizard.getCancelButton().click();
		this.wizard.getPresenter().notifyInfo(ActionBuilderMessages.CREATE_PACK_SUCCESS.get());
		this.wizard.getPresenter().getMasterPresenter().refreshSidebar();
		this.wizard.getPresenter().getMasterPresenter().navigateTo(Pack.class, this.pack);
	}

	@Override
	public ScaffoldPack getTask() {
		DefaultPackTemplate template = new DefaultPackTemplate(this.name, this.title);
		template.setDescription(ActionBuilderPluginInstance.pluginConfig.scaffPackageDescription.value());
		template.setDependencies(ActionBuilderPluginInstance.pluginConfig.scaffPackageDependencies.value());
		template.setHomepage(ActionBuilderPluginInstance.pluginConfig.scaffPackageHomePage.value());
		template.setCompany(ActionBuilderPluginInstance.pluginConfig.scaffPackageCompany.value());
		template.setVersion(ActionBuilderPluginInstance.pluginConfig.scaffPackageVersion.value());
		return new ScaffoldPack(template);
	}

	@Override
	public void afterSuccess(ScaffoldPack task) {
		this.pack = task.getFreshPack();
	}

	@Override
	public void afterError(UIException exception) {
		throw exception;
	}

	@Override
	public boolean isAlreadyFocus() {
		return false;
	}

	@Override
	protected boolean isAddLogPanel() {
		return false;
	}

}
