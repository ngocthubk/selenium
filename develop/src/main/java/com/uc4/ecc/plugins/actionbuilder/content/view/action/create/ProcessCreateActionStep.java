package com.uc4.ecc.plugins.actionbuilder.content.view.action.create;

import com.automic.apm.models.Action;
import com.automic.apm.models.ActionTemplate;
import com.automic.apm.tasks.ScaffoldAction;
import com.uc4.ecc.framework.core.interfaces.IText;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.apm.EncapsuleTask;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.ErrorStep;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.RealtimeWizardLoading;
import com.uc4.ecc.plugins.actioncommon.utils.UIException;

public final class ProcessCreateActionStep extends RealtimeWizardLoading<CreateActionWizard> implements EncapsuleTask<ScaffoldAction> {

	private final ActionTemplate actionTemplate;
	private Action newAction;

	public ProcessCreateActionStep(CreateActionWizard wizard, ActionTemplate actionTemplate) {
		super(wizard);
		this.actionTemplate = actionTemplate;
	}

	@Override
	public ScaffoldAction getTask() {
		return new ScaffoldAction(this.actionTemplate);
	}

	@Override
	public void afterSuccess(ScaffoldAction task) {
		this.newAction = task.getNewAction();
	}

	@Override
	public void afterError(UIException exception) {
		throw exception;
	}

	@Override
	public IText getStateTitle() {
		return ActionBuilderMessages.CREATE_ACTION_PROCESS.format(this.actionTemplate.getActionTitle());
	}

	@Override
	public void onCancel() {
		this.parent().getPresenter().getMasterPresenter().updateState(Action.class, this.newAction);
		this.parent().getPresenter().getMasterPresenter().refreshContent();
		this.parent().getPresenter().getMasterPresenter().refreshToolbar();
	}

	@Override
	public void onInit() {
		this.parent().getCancelButton().setEnabled(false);
		this.start();
	}

	@Override
	public void onStart() {
		this.parent().getPresenter().getMasterPresenter().getOperation().execute(this);
	}

	@Override
	public void onError(Exception ex) {
		ErrorStep errorStep =
				convertToErrorStep(ex, ActionBuilderMessages.CREATE_ACTION_ERROR.format(this.actionTemplate.getActionTitle()).get());
		this.wizard.navigateTo(errorStep);
	}

	@Override
	public void onFinish() {
		this.loading.setVisible(false);
		this.updateHeader(ActionBuilderMessages.CREATE_ACTION_SUCCESS.format(this.actionTemplate.getActionTitle()));
		this.wizard.getCancelButton().setEnabled(true);
	}

	@Override
	public boolean isAlreadyFocus() {
		return false;
	}

}
