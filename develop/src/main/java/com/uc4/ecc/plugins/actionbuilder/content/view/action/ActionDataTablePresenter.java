package com.uc4.ecc.plugins.actionbuilder.content.view.action;

import java.util.ArrayList;
import java.util.List;

import com.automic.apm.models.Action;
import com.automic.apm.models.Pack;
import com.automic.apm.tasks.ListActions;
import com.uc4.ecc.plugins.actioncommon.CommonPresenter;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.apm.EncapsuleTask;
import com.uc4.ecc.plugins.actioncommon.apm.IAPMOperation;
import com.uc4.ecc.plugins.actioncommon.utils.UIException;

public final class ActionDataTablePresenter extends CommonPresenter<Pack, ActionDataTable> implements EncapsuleTask<ListActions> {

	private final List<Action> actions;

	protected ActionDataTablePresenter(Pack pack, ActionDataTable currentComponent, IMasterPresenter masterTabPresenter) {
		super(Pack.class, pack, currentComponent, masterTabPresenter);
		this.actions = new ArrayList<Action>();
	}

	protected List<Action> list() {
		IAPMOperation operation = this.getMasterPresenter().getOperation();
		operation.execute(this);
		return this.actions;
	}

	@Override
	public ListActions getTask() {
		return new ListActions(this.getBean());
	}

	@Override
	public void afterSuccess(ListActions task) {
		this.actions.clear();
		this.actions.addAll(task.getActions());
	}

	@Override
	public void afterError(UIException exception) {
		this.actions.clear();
	}

}
