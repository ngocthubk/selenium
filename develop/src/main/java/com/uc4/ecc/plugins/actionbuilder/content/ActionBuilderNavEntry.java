package com.uc4.ecc.plugins.actionbuilder.content;

import com.uc4.ecc.api.processautomation.interfaces.IProcessAssemblyNavigationContext;
import com.uc4.ecc.api.processautomation.interfaces.IProcessAssemblyNavigationEntry;
import com.uc4.ecc.api.processautomation.util.ProcessAssemblyNavigationPosition;
import com.uc4.ecc.framework.core.interfaces.IIcon;
import com.uc4.ecc.framework.core.interfaces.INavigationPositioned;
import com.uc4.ecc.framework.core.interfaces.IText;
import com.uc4.ecc.framework.core.ui.navigation.NavigationPosition;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actionbuilder.utils.PluginInfo;
import com.vaadin.ui.Component;

public final class ActionBuilderNavEntry implements IProcessAssemblyNavigationEntry, INavigationPositioned {

	private final ActionBuilderMasterView masterView;

	public ActionBuilderNavEntry() {
		this.masterView = new ActionBuilderMasterView();
	}

	@Override
	public String getIdentifier() {
		return PluginInfo.PLUGIN_INTO_PA_IDENTIFIER;
	}

	@Override
	public IText getDisplayName() {
		return ActionBuilderMessages.TITLE_PLUGIN;
	}

	@Override
	public IIcon getIcon() {
		return IconsActionBuilder.ACTION_BUILDER_ICON;
	}

	@Override
	public void onCollapse() {

	}

	@Override
	public Component onExpand(IProcessAssemblyNavigationContext context) {
		this.masterView.setup(context);
		return this.masterView.getSidebar().getView();
	}

	@Override
	public NavigationPosition getPosition() {
		return new ProcessAssemblyNavigationPosition.Plugin(9000);
	}

}
