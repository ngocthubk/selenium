package com.uc4.ecc.plugins.actionbuilder.content.view.action.create;

import com.automic.apm.models.Pack;
import com.uc4.ecc.plugins.actionbuilder.content.view.action.ActionWizardPresenter;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;

public final class CreateActionPresenter extends ActionWizardPresenter<CreateActionWizard> {

	private final Pack pack;

	protected CreateActionPresenter(CreateActionWizard currentComponent, IMasterPresenter masterTabPresenter, Pack pack) {
		super(null, true, currentComponent, masterTabPresenter);
		this.pack = pack;
	}

	@Override
	public String getPrefix() {
		return (this.pack.getActionPrefix()).toUpperCase();
	}

}
