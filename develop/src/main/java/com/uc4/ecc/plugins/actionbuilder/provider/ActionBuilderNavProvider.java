package com.uc4.ecc.plugins.actionbuilder.provider;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.uc4.ecc.api.processautomation.interfaces.IProcessAssemblyNavigationExtension;
import com.uc4.ecc.api.processautomation.interfaces.IProcessAssemblyNavigationExtensionProvider;
import com.uc4.ecc.plugins.actionbuilder.content.ActionBuilderNavExtension;

@Component
@Service
public final class ActionBuilderNavProvider implements IProcessAssemblyNavigationExtensionProvider {

	@Override
	public IProcessAssemblyNavigationExtension provideProcessAssemblyNavigationExtension() {
		return new ActionBuilderNavExtension();
	}

}
