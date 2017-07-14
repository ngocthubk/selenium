package com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.build;

import com.automic.apm.models.Pack;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.WizardPresenter;

public final class BuildWizardPresenter extends WizardPresenter<Pack, BuildWizard> {

	protected BuildWizardPresenter(Pack bean, BuildWizard currentView, IMasterPresenter masterTabPresenter) {
		super(Pack.class, bean, currentView, masterTabPresenter);
	}

}
