package com.uc4.ecc.plugins.actionbuilder.content;

import java.util.Arrays;
import java.util.List;

import com.uc4.ecc.api.processautomation.interfaces.IProcessAssemblyNavigationEntry;
import com.uc4.ecc.api.processautomation.interfaces.IProcessAssemblyNavigationExtension;

public final class ActionBuilderNavExtension implements IProcessAssemblyNavigationExtension {

	private final IProcessAssemblyNavigationEntry actionBuilderNavEntry;

	public ActionBuilderNavExtension() {
		this.actionBuilderNavEntry = new ActionBuilderNavEntry();
	}

	@Override
	public List<IProcessAssemblyNavigationEntry> getProcessAssemblyNavigationEntries() {
		return Arrays.asList(this.actionBuilderNavEntry);
	}

}
