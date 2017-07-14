package com.uc4.ecc.plugins.actionbuilder.content.view.action.clone;

import com.automic.apm.models.Action;
import com.automic.apm.models.ClonePlan;
import com.automic.apm.models.Pack;
import com.automic.apm.tasks.CloneAction;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.WizardState;
import com.uc4.ecc.plugins.actioncommon.utils.UIException;
import com.vaadin.server.Sizeable.Unit;

public final class ProcessCloneAction extends ProcessClone<CloneAction> {

	private static final int DEFAULT_WIDTH = 480;

	private final ClonePlan clonePlan;
	private WizardState state;

	protected ProcessCloneAction(CloneActionWizard wizard, Action sourceAction, Action targetAction, ClonePlan clonePlan) {
		super(wizard, sourceAction, targetAction);
		this.clonePlan = clonePlan;
	}

	@Override
	public CloneAction getTask() {
		return new CloneAction(this.sourceAction, this.targetAction, this.clonePlan);
	}

	@Override
	public WizardState getState() {
		return this.state;
	}

	@Override
	public void afterSuccess(CloneAction task) {
		this.state = WizardState.OK;
	}

	@Override
	public void afterError(UIException exception) {
		super.afterError(exception);
		this.state = WizardState.ERROR;
	}

	@Override
	public final void onFinish() {
		this.loading.setVisible(false);
		this.updateHeader(ActionBuilderMessages.CLONE_ACTION_SUCCESS.format(this.targetAction.getName()));
		this.wizard.getCancelButton().setEnabled(true);
	}

	@Override
	public void onCancel() {
		if (WizardState.OK == this.state) {
			if (this.sourceAction.getPack().getName().equals(this.targetAction.getPack().getName())) {
				this.parent().getPresenter().getMasterPresenter().updateState(Action.class, this.targetAction);
				this.wizard.getPresenter().getMasterPresenter().refreshContent();
			} else {
				this.parent().getPresenter().getMasterPresenter().navigateTo(Pack.class, this.targetAction.getPack());
				this.wizard.getPresenter().getMasterPresenter().updateState(Action.class, this.targetAction);
				this.wizard.getPresenter().getMasterPresenter().refreshToolbar();
			}
		}
	}

	@Override
	public void onInit() {
		super.onInit();
		this.wizard.setWidth(DEFAULT_WIDTH, Unit.PIXELS);
		this.wizard.center();
	}
}
