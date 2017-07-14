package com.uc4.ecc.plugins.actionbuilder.content.view.action.remove;

import com.automic.apm.models.Action;
import com.automic.apm.tasks.RemoveAction;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.apm.EncapsuleTask;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.WizardPresenter;
import com.uc4.ecc.plugins.actioncommon.utils.UIException;

public final class DeleteActionPresenter extends WizardPresenter<Action, DeleteActionDialog> implements EncapsuleTask<RemoveAction> {

	protected DeleteActionPresenter(DeleteActionDialog currentComponent, IMasterPresenter masterTabPresenter, Action action) {
		super(Action.class, action, currentComponent, masterTabPresenter);
	}

	@Override
	public RemoveAction getTask() {
		return new RemoveAction(this.getBean().getPack(), this.getBean().getName());
	}

	@Override
	public void afterSuccess(RemoveAction task) {
		this.notifyInfo(ActionBuilderMessages.REMOVE_ACTION_TOAST_SUCCESS.get());
		this.getMasterPresenter().updateState(Action.class, null);
		this.getMasterPresenter().refreshContent();
		this.getMasterPresenter().refreshToolbar();
	}

	@Override
	public void afterError(UIException exception) {
		this.notifyError(ActionBuilderMessages.REMOVE_ACTION_TOAST_ERROR.get(), exception);
	}

}
