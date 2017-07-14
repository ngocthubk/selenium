package com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.navigation;

import com.automic.apm.models.Pack;
import com.uc4.ecc.plugins.actionbuilder.content.view.action.ActionDataTable;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.uicore.ContentPanel;

public final class PackageContentPanel extends ContentPanel<Pack> {

	private ActionDataTable table;

	public PackageContentPanel(Pack pack, IMasterPresenter masterPresenter) {
		super(Pack.class, pack, masterPresenter);
	}

	public void onLoad() {
		this.table = new ActionDataTable(this.getPresenter().getBean(), this.getPresenter().getMasterPresenter());
		this.table.bindingData();
	}

	@Override
	public void initUI() {
		super.initUI();
		this.layout.addComponent(this.table);
	}

	@Override
	public void update(Pack bean) {
		this.table.update(bean);
	}

}
