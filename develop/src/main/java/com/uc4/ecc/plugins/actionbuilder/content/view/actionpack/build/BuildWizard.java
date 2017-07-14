package com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.build;

import com.automic.apm.models.Pack;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.Wizard;

public final class BuildWizard extends Wizard<BuildWizardPresenter> {

	private final BuildWizardPresenter presenter;

	public BuildWizard(IMasterPresenter masterPresenter, Pack pack) {
		this.presenter = new BuildWizardPresenter(pack, this, masterPresenter);
		this.navigateTo(new BuildStep(this, pack));
	}

	@Override
	public BuildWizardPresenter getPresenter() {
		return this.presenter;
	}

	@Override
	protected String getDialogTitle() {
		return ActionBuilderMessages.BUILD_PACK_TITLE.format("").get();
	}

}
