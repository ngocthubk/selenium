package com.uc4.ecc.plugins.actionbuilder.content.view.action.remove;

import com.automic.apm.models.Action;
import com.automic.apm.tasks.RemoveAction;
import com.uc4.ecc.framework.core.interfaces.IText;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.apm.EncapsuleTask;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.RealtimeWizardLoading;
import com.uc4.ecc.plugins.actioncommon.utils.UIException;

public final class ProcessDeleteAction extends RealtimeWizardLoading<DeleteActionDialog> implements EncapsuleTask<RemoveAction> {

	protected ProcessDeleteAction(DeleteActionDialog wizard) {
		super(wizard);
	}

	@Override
	public IText getStateTitle() {
		return ActionBuilderMessages.REMOVE_ACTION_PROCESS.format(this.wizard.getPresenter().getBean().getTitle());
	}

	@Override
	public void onCancel() {
		this.wizard.getPresenter().getMasterPresenter().updateState(Action.class, null);
		this.wizard.getPresenter().getMasterPresenter().refreshContent();
		this.wizard.getPresenter().getMasterPresenter().refreshToolbar();
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
		this.wizard.navigateTo(convertToErrorStep(ex, ActionBuilderMessages.REMOVE_ACTION_LABEL_ERROR.get()));
	}

	@Override
	public void onFinish() {
		this.loading.setVisible(false);
		this.wizard.getCancelButton().setEnabled(true);
		this.updateHeader(ActionBuilderMessages.REMOVE_ACTION_SUCCESS.format(this.wizard.getPresenter().getBean().getTitle()));
		this.wizard.getPresenter().notifyInfo(ActionBuilderMessages.REMOVE_ACTION_TOAST_SUCCESS.get());
	}

	@Override
	public RemoveAction getTask() {
		Action bean = this.wizard.getPresenter().getBean();
		return new RemoveAction(bean.getPack(), bean.getName());
	}

	@Override
	public void afterSuccess(RemoveAction task) {}

	@Override
	public void afterError(UIException exception) {
		throw exception;
	}

	@Override
	public boolean isAlreadyFocus() {
		return false;
	}

}
