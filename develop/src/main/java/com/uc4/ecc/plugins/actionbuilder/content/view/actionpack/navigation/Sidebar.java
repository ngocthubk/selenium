package com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.navigation;

import java.util.List;

import com.automic.apm.models.Pack;
import com.uc4.ecc.framework.commons.controls.asyncwidget.AsyncBuilder;
import com.uc4.ecc.framework.commons.controls.asyncwidget.AsyncLoadingContainer;
import com.uc4.ecc.framework.commons.controls.navigationlist.INavigationListEntry;
import com.uc4.ecc.framework.commons.css.Icons;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.ICommonView;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.uicore.EventFactory;
import com.uc4.ecc.plugins.actioncommon.uicore.EventType;
import com.uc4.ecc.plugins.actioncommon.uicore.IAsyncLoading;
import com.uc4.ecc.plugins.actioncommon.uicore.INavigation;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;

// To pin "Create Pack" button on top of view
public final class Sidebar extends AsyncLoadingContainer implements INavigation<Pack>, ICommonView, IAsyncLoading {

	private static final String UC4_COMMON_NAVIGATION_VIEW_TOOLBAR_LAYOUT = "uc4_common_navigationViewToolbarLayout";
	private final IMasterPresenter controller;
	private final Button createPackageButton;
	private final PackageTree packageTree;
	private final VerticalLayout layout;

	public Sidebar(IMasterPresenter masterPresenter) {
		this.controller = masterPresenter;
		this.packageTree = new PackageTree(masterPresenter);
		this.layout = new VerticalLayout();
		this.createPackageButton = EventFactory.<Pack> buildToolbarButton(
				EventFactory.initEvent(ActionBuilderMessages.BUTTON_CREATE_PACK.get(), Icons.ADD, EventType.CREATE),
				masterPresenter.getController(Pack.class));
	}

	@Override
	public Component getView() {
		AsyncBuilder.create(this, this::onLoad, this::onRender).submit();
		this.setBaseComponent(layout);
		return this;
	}

	@Override
	public void refresh() {
		this.packageTree.refresh();
	}

	@Override
	public void navigateTo(Pack bean) {
		this.packageTree.navigateTo(bean);
	}

	@Override
	public void initUI() {
		this.setHeight(99.5f, Unit.PERCENTAGE);
		this.layout.removeAllComponents();
		CssLayout createPackageWrapper = new CssLayout();
		createPackageWrapper.setWidth(100, Unit.PERCENTAGE);
		createPackageWrapper.addStyleName(UC4_COMMON_NAVIGATION_VIEW_TOOLBAR_LAYOUT);
		createPackageWrapper.addComponent(this.createPackageButton);
		this.layout.addComponent(createPackageWrapper);
		this.layout.addComponent(this.packageTree.getView());
		this.layout.setComponentAlignment(this.packageTree.getView(), Alignment.TOP_LEFT);
		this.layout.setExpandRatio(this.packageTree.getView(), 1);
		this.layout.setSizeFull();
	}

	@Override
	public void registerEventHandler() {}

	@Override
	public List<INavigationListEntry> entries() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onLoad() {
		this.refresh();
		this.navigateTo(this.controller.getState(Pack.class));
	}

	@Override
	public void onRender() {
		this.initUI();
		this.registerEventHandler();
	}

}
