package com.automic.ecc.testcase;

import java.util.Arrays;

import com.automic.ecc.core.actionLibraries.XPathKeys;
import com.automic.ecc.core.utils.Messages;
import com.automic.ecc.testdata.ActionTestDataProvider.ActionTestData;
import com.automic.ecc.testdata.ActionTestDataProvider.CloneActionTestData;

import testDrivers.WEBdriver.Visibility;
import testUtils.MessageLoggers;

public class ActionTest extends TestSuite {

	protected final String getActionFQN(ActionTestData data) {
		return Messages.getInstance().getMessage("explorer.action.jobp.object", data.getPackName(), data.getName());
	}

	protected final String getActionFQN(CloneActionTestData data) {
		return Messages.getInstance().getMessage("explorer.action.jobp.object", data.getTargetPackName(), data.getName());
	}

	protected final void assertActionInProcessAssembly(ActionTestData data, String actionFQN) {
		this.assertActionSource(data, actionFQN);
		this.assertActionInPack(data, actionFQN);
		this.assertActionInGlobal(data, actionFQN);
	}

	private void assertActionSource(ActionTestData data, String actionFQN) {
		this.controller.webAssertItemsOnTable(Messages.getInstance().getMessage("explorer.action.source.internal"), actionFQN);
		this.assertActionSourceStructure();
		this.assertActionPromptset(data);
		if (!data.getType().equals("REST")) {
			this.assertJobs(data);
		}
	}

	protected void assertActionSourceStructure() {
		this.controller.goToTableItem(Messages.getInstance().getMessage("explorer.action.source.internal"));
		this.controller.webAssertItemsOnTable(
				Messages.getInstance().getMessage("explorer.action.source.internal.includes"),
				Messages.getInstance().getMessage("explorer.action.source.internal.jobs"),
				Messages.getInstance().getMessage("explorer.action.source.internal.promptsets"),
				Messages.getInstance().getMessage("explorer.action.source.internal.rollback"),
				Messages.getInstance().getMessage("explorer.action.source.internal.variables"));
	}

	// TODO assert detail base on workflow of jobp, job rest
	private final void assertActionPromptset(ActionTestData data) {
		this.controller.goToNavigationTreeItem(
				Messages.getInstance().getMessage("explorer.pack.source"),
				data.getName(),
				Messages.getInstance().getMessage("explorer.action.source.internal"),
				Messages.getInstance().getMessage("explorer.action.source.internal.promptsets"));
		this.controller.webAssertItemsOnTable(
				Messages.getInstance().getMessage("explorer.action.promptset.object", data.getPackName(), data.getName()));
	}

	private void assertJobs(ActionTestData data) {
		this.controller.goToNavigationTreeItem(
				data.getName(),
				Messages.getInstance().getMessage("explorer.pack.source"),
				Messages.getInstance().getMessage("explorer.action.source.internal"),
				Messages.getInstance().getMessage("explorer.action.source.internal.jobs"));
		String winJobs = Messages.getInstance().getMessage("explorer.action.jobs.object.windows", data.getPackName(), data.getName());
		String unixJobs = Messages.getInstance().getMessage("explorer.action.jobs.object.unix", data.getPackName(), data.getName());
		this.controller.webAssertItemsOnTable(winJobs, unixJobs);
		this.assertJobsDetail(winJobs);
		this.assertJobsDetail(unixJobs);
	}

	private void assertJobsDetail(String winJobs) {
		this.controller.webDoubleClick(XPathKeys.TABLE_ROW, winJobs);
		// TODO assert detail base on type of jobs: unix, windows, rest
		this.controller.webClick(XPathKeys.FIRST_TAB);
	}

	private void assertActionInPack(ActionTestData data, String actionFQN) {
		this.assertActionInCategory(data, actionFQN, data.getPackName());
	}

	private void assertActionInGlobal(ActionTestData data, String actionFQN) {
		this.assertActionInCategory(data, actionFQN, Messages.getInstance().getMessage("explorer.packages"));
	}

	private void assertActionInCategory(ActionTestData data, String actionFQN, String root) {
		String[] paths = data.getCategory().split("/");
		this.controller.goToNavigationTreeItem(root, Messages.getInstance().getMessage("explorer.pack.actions"));
		this.controller.goToNavigationTreeItem(paths);
		this.controller.webAssertItemsOnTable(actionFQN);
	}

	protected void createAction(ActionTestData data) {
		MessageLoggers.infoLogger("Create an action with title " + data.getTitle());
		this.controller.webClickButtonToolbar("button.add_action");
		this.controller.webWaitForElementToAppear(
				this.controller.getChildElementMatchText(
						XPathKeys.POPUP_HEADER,
						Messages.getInstance().getMessage("label.dialog.header.create_action")),
				30);
		this.controller.inputCombobox(data.getType());
		this.controller.inputTextValuesInForm(Arrays.asList(data.getTitle(), data.getInputName()));
		this.controller.inputAddressBar(data.getCategory(), data.isCategoryExisted());
		this.controller.webClickButtonDialog("button.next");
	}

	protected void assertCreateAction(ActionTestData data) {
		String actionFQN = this.getActionFQN(data);
		this.controller.webAssertElementVisibility(
				this.controller.getLabelElement("label.behavior.action.add.confirm", data.getType(), data.getTitle()),
				Visibility.VISIBLE);
		// TODO assert new items will be added
		this.controller.webWaitForElement(XPathKeys.LABEL_LI, 90);
		this.controller.webClickButtonDialog("button.add");
		this.controller
				.webWaitForElementToAppear(this.controller.getLabelElement("label.behavior.action.add.success", data.getTitle()), 180);
		this.controller.webAssertElementVisibility(
				this.controller.getChildElementContainsText(
						XPathKeys.LABEL,
						Messages.getInstance().getMessage("label.behavior.action.add.log.success", actionFQN)),
				Visibility.VISIBLE);
		MessageLoggers.infoLogger("Assert title and name");
		this.controller.webClickButtonDialog("button.close");
		this.controller.webAssertItemsOnTable(data.getTitle(), actionFQN);
		this.controller.webClickMenuItemOnTable(actionFQN, "menu.jump");
		// TODO FIX temporary: #WEBUI-6177
		// this.assertActionInProcessAssembly(data, actionFQN);
	}

}
