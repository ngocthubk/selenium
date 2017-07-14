package com.uc4.ecc.plugins.actionbuilder.content.view.action.create;

import com.automic.apm.models.Pack;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.Wizard;

public final class CreateActionWizard extends Wizard<CreateActionPresenter> {

	private final CreateActionPresenter presenter;

	public CreateActionWizard(IMasterPresenter masterTabPresenter, Pack pack) {
		this.presenter = new CreateActionPresenter(this, masterTabPresenter, pack);
		this.navigateTo(new ActionInputStep(this, pack));
	}

	@Override
	public CreateActionPresenter getPresenter() {
		return this.presenter;
	}

	@Override
	protected String getDialogTitle() {
		return ActionBuilderMessages.CREATE_ACTION_TITLE.get();
	}

}
