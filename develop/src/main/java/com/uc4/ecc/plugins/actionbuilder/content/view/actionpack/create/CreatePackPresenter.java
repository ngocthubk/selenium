package com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.create;

import com.uc4.ecc.plugins.actionbuilder.entrypoint.ActionBuilderPluginInstance;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.apm.APMFactory;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.WizardPresenter;

public final class CreatePackPresenter extends WizardPresenter<Void, CreatePackDialog> {

	protected CreatePackPresenter(CreatePackDialog confirmDialog, IMasterPresenter masterTabPresenter) {
		super(Void.class, null, confirmDialog, masterTabPresenter);
	}

	protected String getFQNPrefix() {
		return APMFactory.getAPMOperation().getPackClientSettings().getPackPrefix()
				+ ActionBuilderPluginInstance.pluginConfig.scaffPackageTitlePrefix.value();
	}

}
