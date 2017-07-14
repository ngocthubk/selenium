package com.uc4.ecc.plugins.actionbuilder.content.view.actionpack;

import com.automic.apm.models.Pack;
import com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.build.BuildWizard;
import com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.clone.ClonePackWizard;
import com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.create.CreatePackDialog;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.ecc.AWIEvent;
import com.uc4.ecc.plugins.actioncommon.uicore.IEventHandler;
import com.uc4.ecc.plugins.actioncommon.utils.UIException;
import com.uc4.ecc.plugins.actioncommon.utils.UIException.UIOutOfSyncException;
import com.vaadin.ui.UI;

public final class PackageEventHandler implements IEventHandler<Pack> {

	private final IMasterPresenter masterPresenter;
	private Pack pack;

	public PackageEventHandler(IMasterPresenter masterPresenter) {
		this.masterPresenter = masterPresenter;
	}

	@Override
	public void onCreate() {
		UI.getCurrent().addWindow(new CreatePackDialog(this.masterPresenter));
	}

	@Override
	public void onEdit() {
		throw new UnsupportedOperationException("NOT SUPPORTED");
	}

	@Override
	public void onDupliace() {
		UI.getCurrent().addWindow(new ClonePackWizard(this.masterPresenter, this.pack));
	}

	@Override
	public void onDelete() {
		throw new UnsupportedOperationException("NOT YET IMPLEMENTED");
	}

	@Override
	public void onInstall() {
		throw new UnsupportedOperationException("NOT YET IMPLEMENTED");
	}

	@Override
	public void onShowDetails() {
		String name = this.pack.getName();
		try {
			AWIEvent.getInstance().navigateToFolder(this.pack.getPath(), name);
		} catch (UIException ex) {
			UIOutOfSyncException ex1 = new UIOutOfSyncException("The Pack does no longer exist. Your Pack list is out of sync");
			if (this.pack.equals(this.masterPresenter.getState(Pack.class))) {
				this.masterPresenter.updateState(Pack.class, null);
			}
			this.masterPresenter.notifyError(ActionBuilderMessages.JUMP_TO_SOURCE_TOAST_ERROR.get(), ex1);
		}
	}

	@Override
	public void onShowInfo() {
		throw new UnsupportedOperationException("NOT SUPPORTED");
	}

	@Override
	public void onExport() {
		UI.getCurrent().addWindow(new BuildWizard(this.masterPresenter, this.pack));
	}

	@Override
	public Pack getBean() {
		return this.pack;
	}

	@Override
	public void setBean(Pack bean) {
		this.pack = bean;
	}

}
