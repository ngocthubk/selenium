package com.uc4.ecc.plugins.actionbuilder.content.view.action.clone;

import com.automic.apm.Task;
import com.automic.apm.models.Action;
import com.uc4.ecc.framework.core.interfaces.IText;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.apm.EncapsuleTask;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.IWizardStep;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.RealtimeWizardLoading;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.WizardState;
import com.uc4.ecc.plugins.actioncommon.utils.UIException;

public abstract class ProcessClone<T extends Task> extends RealtimeWizardLoading<CloneActionWizard> implements EncapsuleTask<T> {

	protected IWizardStep next;
	protected final Action sourceAction;
	protected final Action targetAction;

	protected ProcessClone(CloneActionWizard wizard, Action sourceAction, Action targetAction) {
		super(wizard);
		this.sourceAction = sourceAction;
		this.targetAction = targetAction;
	}

	@Override
	public IText getStateTitle() {
		return ActionBuilderMessages.LABEL_CLONE_PROCESS.format(this.sourceAction.getName(), this.targetAction.getName());
	}

	@Override
	public void onInit() {
		this.wizard.getCancelButton().setEnabled(false);
		this.start();
	}

	@Override
	public IWizardStep next() {
		return next;
	}

	@Override
	public WizardState getState() {
		return WizardState.OK;
	}

	@Override
	public final boolean isAlreadyFocus() {
		return false;
	}

	@Override
	public final void onStart() {
		this.wizard.getPresenter().getMasterPresenter().getOperation().execute(this);
	}

	@Override
	public void onError(Exception ex) {}

	@Override
	public void afterError(UIException exception) {
		this.wizard.navigateTo(
				convertToErrorStep(exception, ActionBuilderMessages.CLONE_ACTION_ERROR.format(this.sourceAction.getTitle()).get()));
	}

}
