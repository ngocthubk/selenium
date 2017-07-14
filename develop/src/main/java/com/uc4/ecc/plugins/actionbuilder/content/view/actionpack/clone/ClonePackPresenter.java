package com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.clone;

import com.automic.apm.models.Pack;
import com.uc4.ecc.plugins.actionbuilder.entrypoint.ActionBuilderPluginInstance;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.apm.APMFactory;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.WizardPresenter;

public final class ClonePackPresenter extends WizardPresenter<Pack, ClonePackWizard> {

	protected ClonePackPresenter(ClonePackWizard view, IMasterPresenter masterPresenter, Pack pack) {
		super(Pack.class, pack, false, view, masterPresenter);
	}

	protected String getFQNPrefix() {
		return APMFactory.getAPMOperation().getPackClientSettings().getPackPrefix()
				+ ActionBuilderPluginInstance.pluginConfig.scaffPackageTitlePrefix.value();
	}

}
