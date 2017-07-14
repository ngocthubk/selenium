package com.uc4.ecc.plugins.actionbuilder.content;

import com.google.common.eventbus.EventBus;
import com.uc4.ecc.api.processautomation.interfaces.IProcessAssemblyNavigationContext;
import com.uc4.ecc.framework.core.context.Context;
import com.uc4.ecc.framework.core.interfaces.IContentPresenterProvider;
import com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.navigation.Sidebar;
import com.uc4.ecc.plugins.actioncommon.IMasterView;
import com.uc4.ecc.plugins.actioncommon.uicore.ContentPanel;
import com.uc4.ecc.plugins.actioncommon.uicore.INavigation;
import com.uc4.ecc.plugins.actioncommon.uicore.ToolbarPanel;

public final class ActionBuilderMasterView implements IMasterView, IContentPresenterProvider<ActionBuilderMasterPresenter> {

	private IProcessAssemblyNavigationContext context;
	private final ActionBuilderMasterPresenter presenter;
	private final INavigation navigation;
	private ContentPanel<?> content;
	private ToolbarPanel<?> toolbar;

	public ActionBuilderMasterView() {
		this.presenter = new ActionBuilderMasterPresenter(this);
		this.navigation = new Sidebar(this.presenter);
	}

	public void setup(IProcessAssemblyNavigationContext context) {
		this.context = context;
	}

	@Override
	public ActionBuilderMasterPresenter getPresenter() {
		return this.presenter;
	}

	@Override
	public void initContent(ContentPanel<?> iContent, ToolbarPanel<?> toolbar) {
		if (this.context == null || iContent == null) {
			return;
		}
		this.context.setContent(toolbar, iContent);
		this.content = iContent;
		if (this.toolbar != null) {
			Context.getInjectableInstance(EventBus.class).unregister(this.toolbar);
		}
		this.toolbar = toolbar;
		Context.getInjectableInstance(EventBus.class).register(this.toolbar);
	}

	@Override
	public ToolbarPanel<?> getToolbar() {
		return this.toolbar;
	}

	@Override
	public ContentPanel<?> getContent() {
		return this.content;
	}

	@Override
	public INavigation<?> getSidebar() {
		return this.navigation;
	}
}
