package com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.create;

import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.Wizard;

public final class CreatePackDialog extends Wizard<CreatePackPresenter> {

	private final CreatePackPresenter createPackagePresenter;

	public CreatePackDialog(IMasterPresenter masterTabPresenter) {
		this.createPackagePresenter = new CreatePackPresenter(this, masterTabPresenter);
		this.navigateTo(new CreatePackInputStep(this));
	}

	@Override
	public CreatePackPresenter getPresenter() {
		return this.createPackagePresenter;
	}

	@Override
	protected String getDialogTitle() {
		return ActionBuilderMessages.CREATE_PACK_TITLE.get();
	}

}
