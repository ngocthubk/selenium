package com.uc4.ecc.plugins.actionbuilder.content.view.action.clone;

import com.automic.apm.models.Action;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.Wizard;

public final class CloneActionWizard extends Wizard<CloneActionPresenter> {

	private final CloneActionPresenter presenter;

	public CloneActionWizard(IMasterPresenter masterTabPresenter, Action action) {
		this.presenter = new CloneActionPresenter(this, masterTabPresenter, action);
		this.navigateTo(new CloneActionInputStep(this, this.getPresenter().getBean()));
	}

	@Override
	public CloneActionPresenter getPresenter() {
		return this.presenter;
	}

	@Override
	protected String getDialogTitle() {
		return ActionBuilderMessages.CLONE_ACTION_TITLE.get();
	}

}
