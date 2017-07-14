package com.uc4.ecc.plugins.actionbuilder.content.view;

import java.util.ArrayList;
import java.util.List;

import com.automic.apm.models.Pack;
import com.automic.apm.tasks.ListPackages;
import com.uc4.ecc.plugins.actioncommon.apm.EncapsuleTask;
import com.uc4.ecc.plugins.actioncommon.apm.IAPMOperation;
import com.uc4.ecc.plugins.actioncommon.utils.UIException;

public final class ListPackDelegate implements EncapsuleTask<ListPackages> {

	private final List<Pack> packs = new ArrayList<>();

	@Override
	public ListPackages getTask() {
		return new ListPackages();
	}

	@Override
	public void afterSuccess(ListPackages task) {
		this.packs.clear();
		this.packs.addAll(task.getPackages());
	}

	@Override
	public void afterError(UIException exception) {
		this.packs.clear();
	}

	public List<Pack> execute(IAPMOperation operation) {
		operation.execute(this);
		return this.packs;
	}

}
