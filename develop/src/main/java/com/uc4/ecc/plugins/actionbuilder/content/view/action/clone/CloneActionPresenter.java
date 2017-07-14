package com.uc4.ecc.plugins.actionbuilder.content.view.action.clone;

import com.automic.apm.models.Action;
import com.uc4.ecc.plugins.actionbuilder.content.view.action.ActionWizardPresenter;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;

public final class CloneActionPresenter extends ActionWizardPresenter<CloneActionWizard> {

	protected CloneActionPresenter(CloneActionWizard view, IMasterPresenter masterPresenter, Action action) {
		super(action, false, view, masterPresenter);
	}

	@Override
	public String getPrefix() {
		return (this.getBean().getPack().getActionPrefix()).toUpperCase();
	}
}
