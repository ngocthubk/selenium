package com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.navigation;

import com.automic.apm.models.Pack;
import com.uc4.ecc.framework.commons.css.Icons;
import com.uc4.ecc.framework.core.interfaces.IIcon;
import com.uc4.ecc.plugins.actionbuilder.content.IconsActionBuilder;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.uicore.EventFactory;
import com.uc4.ecc.plugins.actioncommon.uicore.EventType;
import com.uc4.ecc.plugins.actioncommon.uicore.IEvent;
import com.uc4.ecc.plugins.actioncommon.uicore.IEventHandler;
import com.uc4.ecc.plugins.actioncommon.uicore.TreeItem;

public final class PackageItemView extends TreeItem<Pack> {

	public PackageItemView(Pack bean, IMasterPresenter masterPresenter) {
		super(bean, true);
		IEventHandler<Pack> handler = masterPresenter.getController(Pack.class);
		IEvent jumpToSource = EventFactory
				.initEvent(ActionBuilderMessages.BUTTON_JUMP_TO_SOURCE.get(), Icons.VERTICALNAVIGATION_NEXTPAGE, EventType.SHOW_DETAILS);
		IEvent cloneEvent =
				EventFactory.initEvent(ActionBuilderMessages.BUTTON_CLONE.get(), IconsActionBuilder.PACKAGE_ICON, EventType.DUPLICATE);
		this.getMenu().addAction(EventFactory.getMenuEntry(jumpToSource, handler));
		this.getMenu().addSeparator();
		this.getMenu().addAction(EventFactory.getMenuEntry(IEvent.EXPORT, handler));
		this.getMenu().addAction(EventFactory.getMenuEntry(cloneEvent, handler));
		this.initUI();
		this.registerEventHandler();
	}

	@Override
	protected String treeItemLabel() {
		return this.getBean().getName();
	}

	@Override
	protected IIcon treeItemIcon() {
		return IconsActionBuilder.PACKAGE_ICON;
	}

	@Override
	protected String treeItemCssClass() {
		return "package-treeitem";
	}

}
