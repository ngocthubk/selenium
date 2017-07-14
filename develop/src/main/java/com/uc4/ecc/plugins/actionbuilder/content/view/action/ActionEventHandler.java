package com.uc4.ecc.plugins.actionbuilder.content.view.action;

import com.automic.apm.models.Action;
import com.automic.apm.models.Pack;
import com.automic.apm.tasks.GetAction;
import com.uc4.ecc.framework.core.interfaces.ContentDisplayMode;
import com.uc4.ecc.plugins.actionbuilder.content.view.action.clone.CloneActionWizard;
import com.uc4.ecc.plugins.actionbuilder.content.view.action.create.CreateActionWizard;
import com.uc4.ecc.plugins.actionbuilder.content.view.action.remove.DeleteActionDialog;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.apm.EncapsuleTask;
import com.uc4.ecc.plugins.actioncommon.ecc.AWIEvent;
import com.uc4.ecc.plugins.actioncommon.uicore.IEventHandler;
import com.uc4.ecc.plugins.actioncommon.utils.UIException;
import com.uc4.ecc.plugins.actioncommon.utils.UIException.UIOutOfSyncException;
import com.vaadin.ui.UI;

public final class ActionEventHandler implements IEventHandler<Action>, EncapsuleTask<GetAction> {

	private final IMasterPresenter masterPresenter;
	private Action action;

	public ActionEventHandler(IMasterPresenter presenter) {
		this.masterPresenter = presenter;
	}

	@Override
	public void onCreate() {
		Pack pack =
				this.action == null || this.action.getPack() == null ? this.masterPresenter.getState(Pack.class) : this.action.getPack();
		UI.getCurrent().addWindow(new CreateActionWizard(this.masterPresenter, pack));
	}

	@Override
	public void onEdit() {
		AWIEvent.getInstance().openObject(this.action.getName(), ContentDisplayMode.NEW_TAB);
	}

	@Override
	public void onDupliace() {
		UI.getCurrent().addWindow(new CloneActionWizard(this.masterPresenter, this.action));
	}

	@Override
	public void onDelete() {
		UI.getCurrent().addWindow(new DeleteActionDialog(this.masterPresenter, this.action));
	}

	@Override
	public void onInstall() {
		throw new UnsupportedOperationException("NOT SUPPORTED ON ACTION");
	}

	@Override
	public void onShowDetails() {
		this.masterPresenter.getOperation().execute(this);
	}

	@Override
	public void onShowInfo() {
		throw new UnsupportedOperationException("NOT SUPPORTED ON ACTION");
	}

	@Override
	public void onExport() {
		throw new UnsupportedOperationException("NOT SUPPORTED ON ACTION");
	}

	@Override
	public Action getBean() {
		return this.action;
	}

	@Override
	public void setBean(Action bean) {
		this.action = bean;
	}

	@Override
	public GetAction getTask() {
		return new GetAction(this.action.getName());
	}

	@Override
	public void afterSuccess(GetAction task) {
		String name = task.getAction().getName();
		try {
			AWIEvent.getInstance().navigateToFolder(task.getAction().getSourceFolder(), name);
		} catch (UIException ex) {
			handleException(name, ex);
		}
	}

	@Override
	public void afterError(UIException exception) {
		handleException(action.getName(), exception);
	}

	private void handleException(String name, UIException ex) {
		UIOutOfSyncException ex1 = new UIOutOfSyncException("Action " + name + " is no longer existed", ex);
		if (this.action.equals(this.masterPresenter.getState(Action.class))) {
			this.masterPresenter.updateState(Action.class, null);
		}
		this.masterPresenter.notifyError("Cannot jump to source of Action " + name, ex1);
	}
}
