package com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.navigation;

import java.util.List;

import com.automic.apm.models.Action;
import com.automic.apm.models.Pack;
import com.uc4.ecc.plugins.actionbuilder.content.view.ListPackDelegate;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.uicore.INavigationPresenter;

public final class PackageTreePresenter implements INavigationPresenter<Pack> {

	private final PackageTree view;
	private final IMasterPresenter masterPresenter;
	private final ListPackDelegate delegate;

	protected PackageTreePresenter(PackageTree currentView, IMasterPresenter masterPresenter) {
		this.view = currentView;
		this.masterPresenter = masterPresenter;
		this.delegate = new ListPackDelegate();
	}

	public IMasterPresenter getMasterPresenter() {
		return this.masterPresenter;
	}

	public PackageTree getView() {
		return this.view;
	}

	@Override
	public void onNavigate(Pack entry) {
		this.getMasterPresenter().updateState(Pack.class, entry);
		this.getMasterPresenter().updateState(Action.class, null);
		PackageContentPanel content = new PackageContentPanel(entry, this.getMasterPresenter());
		PackageToolbar toolbar = new PackageToolbar(entry, this.getMasterPresenter());
		this.getMasterPresenter().init(content, toolbar);
	}

	@Override
	public List<Pack> initEntries() {
		return delegate.execute(this.masterPresenter.getOperation());
	}

}
