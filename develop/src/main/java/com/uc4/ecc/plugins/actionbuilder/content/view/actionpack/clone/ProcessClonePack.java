package com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.clone;

import java.util.ArrayList;
import java.util.Set;

import com.automic.apm.exceptions.ObjectsConflictedException;
import com.automic.apm.models.Pack;
import com.automic.apm.tasks.ClonePack;
import com.uc4.ecc.framework.core.interfaces.IText;
import com.uc4.ecc.plugins.actionbuilder.entrypoint.ActionBuilderPluginInstance;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.apm.EncapsuleTask;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.ErrorStep;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.RealtimeWizardLoading;
import com.uc4.ecc.plugins.actioncommon.utils.HTMLUtils;
import com.uc4.ecc.plugins.actioncommon.utils.UIException;

public final class ProcessClonePack extends RealtimeWizardLoading<ClonePackWizard> implements EncapsuleTask<ClonePack> {

	private final Pack originPack;
	private Pack clonedPack;
	private String clonedPackName;
	private String clonedPackTitle;

	protected ProcessClonePack(ClonePackWizard wizard, Pack originPack) {
		super(wizard);
		this.originPack = originPack;
	}

	protected void setClonedPackName(String name) {
		this.clonedPackName = name.toUpperCase();
	}

	protected void setClonedPackTitle(String title) {
		this.clonedPackTitle = title;
	}

	@Override
	public void onCancel() {
		IMasterPresenter masterPresenter = this.wizard.getPresenter().getMasterPresenter();
		masterPresenter.refreshSidebar();
		masterPresenter.navigateTo(Pack.class, this.clonedPack);
	}

	@Override
	public void onInit() {
		this.wizard.getCancelButton().setEnabled(false);
		this.start();
	}

	@Override
	public IText getStateTitle() {
		return ActionBuilderMessages.LABEL_CLONE_PROCESS.format(this.originPack.getName(), this.clonedPackName);
	}

	@Override
	public ClonePack getTask() {
		ClonePack task = new ClonePack(this.originPack, this.clonedPackName, this.clonedPackTitle);
		task.setTargetHomepage(ActionBuilderPluginInstance.pluginConfig.scaffPackageHomePage.value());
		task.setTargetCompany(ActionBuilderPluginInstance.pluginConfig.scaffPackageCompany.value());
		task.setTargetVersion(ActionBuilderPluginInstance.pluginConfig.scaffPackageVersion.value());
		return task;
	}

	@Override
	public void afterSuccess(ClonePack task) {
		this.clonedPack = task.getTargetPack();
	}

	@Override
	public void afterError(UIException exception) {
		throw exception;
	}

	@Override
	public void onStart() {
		this.wizard.getPresenter().getMasterPresenter().getOperation().execute(this);
	}

	@Override
	public void onError(Exception ex) {
		String recapMessage = "";
		if (ObjectsConflictedException.class.isInstance(ex.getCause())) {
			Set<String> conflictedObjects = ((ObjectsConflictedException) ex.getCause()).getConflictedObjects();
			recapMessage = ActionBuilderMessages.CLONE_PACK_CONFLICT.format(HTMLUtils.ul(new ArrayList<>(conflictedObjects), true)).get();
		}
		ErrorStep errorStep = convertToErrorStep(
				ex,
				ActionBuilderMessages.CLONE_PACK_ERROR.format(this.originPack.getName(), this.originPack.getVersion()).get(),
				recapMessage);
		this.wizard.navigateTo(errorStep);
	}

	@Override
	public void onFinish() {
		this.loading.setVisible(false);
		this.updateHeader(ActionBuilderMessages.CLONE_PACK_SUCCESS.format(this.clonedPackName, this.clonedPack.getVersion()));
		this.wizard.getCancelButton().setEnabled(true);
	}

	@Override
	public boolean isAlreadyFocus() {
		return false;
	}

}
