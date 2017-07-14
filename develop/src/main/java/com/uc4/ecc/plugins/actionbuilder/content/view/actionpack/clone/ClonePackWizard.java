package com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.clone;

import com.automic.apm.models.Pack;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.Wizard;

public final class ClonePackWizard extends Wizard<ClonePackPresenter> {

	private final ClonePackPresenter presenter;

	public ClonePackWizard(IMasterPresenter masterTabPresenter, Pack pack) {
		this.presenter = new ClonePackPresenter(this, masterTabPresenter, pack);
		this.navigateTo(new ClonePackInputStep(this, this.getPresenter().getBean()));
	}

	@Override
	public ClonePackPresenter getPresenter() {
		return this.presenter;
	}

	@Override
	protected String getDialogTitle() {
		return ActionBuilderMessages.CLONE_PACK_TITLE.get();
	}

}
