package com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.navigation;

import java.util.List;

import com.automic.apm.models.Pack;
import com.uc4.ecc.framework.commons.controls.navigationlist.INavigationListEntry;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.uicore.TreeList;

public final class PackageTree extends TreeList<Pack> {

	private final PackageTreePresenter presenter;

	public PackageTree(IMasterPresenter masterPresenter) {
		this.presenter = new PackageTreePresenter(this, masterPresenter);
		this.initUI();
		this.registerEventHandler();
	}

	@Override
	protected PackageItemView createTabEntry(Pack description) {
		return new PackageItemView(description, this.presenter.getMasterPresenter());
	}

	@Override
	public List<INavigationListEntry> entries() {
		throw new UnsupportedOperationException();
	}

	@Override
	public PackageTreePresenter getPresenter() {
		return this.presenter;
	}

}
