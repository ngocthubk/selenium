package com.uc4.ecc.plugins.actionbuilder.content.view.action.clone;

import com.automic.apm.models.Action;
import com.automic.apm.models.ClonePlan;
import com.automic.apm.tasks.VerifyCloneAction;

public final class ProcessVerifyCloneAction extends ProcessClone<VerifyCloneAction> {

	private final ClonePlan clonePlan;

	protected ProcessVerifyCloneAction(CloneActionWizard wizard, Action sourceAction, Action targetAction) {
		this(wizard, sourceAction, targetAction, null);
	}

	protected ProcessVerifyCloneAction(CloneActionWizard wizard, Action sourceAction, Action targetAction, ClonePlan clonePlan) {
		super(wizard, sourceAction, targetAction);
		this.clonePlan = clonePlan;
	}

	@Override
	public VerifyCloneAction getTask() {
		if (this.clonePlan == null) {
			return new VerifyCloneAction(this.sourceAction.getName(), this.targetAction.getName(), null, this.targetAction.getTitle());
		}
		return new VerifyCloneAction(
				this.sourceAction.getName(),
				this.targetAction.getName(),
				this.clonePlan,
				this.targetAction.getTitle());
	}

	@Override
	public void afterSuccess(VerifyCloneAction task) {
		ClonePlan clonePlan = task.getClonePlan();
		if (clonePlan.isUnresolvedConflicted()) {
			this.next = new ResolveCloningConflictStep(wizard, sourceAction, targetAction, clonePlan);
		} else {
			this.next = new ProcessCloneAction(wizard, sourceAction, targetAction, clonePlan);
		}
		this.wizard.navigateTo(next);
	}

	@Override
	public final void onFinish() {}

	@Override
	protected boolean isAddLogPanel() {
		return false;
	}

	@Override
	public void onCancel() {}

}
