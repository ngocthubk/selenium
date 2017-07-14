package com.uc4.ecc.plugins.actionbuilder.content.view.action.remove;

import com.automic.apm.models.Action;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.Wizard;

public final class DeleteActionDialog extends Wizard<DeleteActionPresenter> {

	private final DeleteActionPresenter presenter;

	public DeleteActionDialog(IMasterPresenter presenter, Action action) {
		this.presenter = new DeleteActionPresenter(this, presenter, action);
		this.navigateTo(new DeleteActionConfirmStep(this));
	}

	@Override
	public DeleteActionPresenter getPresenter() {
		return this.presenter;
	}

	@Override
	protected String getDialogTitle() {
		return ActionBuilderMessages.REMOVE_ACTION_TITLE.get();
	}
}
