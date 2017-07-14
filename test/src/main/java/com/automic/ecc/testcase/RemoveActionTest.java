package com.automic.ecc.testcase;

import org.testng.annotations.Test;

import com.automic.ecc.core.actionLibraries.XPathKeys;
import com.automic.ecc.core.utils.Messages;
import com.automic.ecc.core.utils.TestConstants;
import com.automic.ecc.testdata.ActionTestDataProvider;
import com.automic.ecc.testdata.ActionTestDataProvider.ActionTestData;

import testDrivers.Timeouts;
import testDrivers.WEBdriver.Visibility;
import testUtils.MessageLoggers;

public class RemoveActionTest extends ActionTest {

	@Test(groups = {TestConstants.PUSH}, dataProvider = "removeAction", dataProviderClass = ActionTestDataProvider.class, priority = 2)
	public void removeAction(ActionTestData data) {
		MessageLoggers.infoLogger("Delete action: " + data.getName() + " on toolbar");
		String actionFQN = this.getActionFQN(data);
		this.controller.goToNavigationTreeItem(data.getPackName());
		// this.controller.webAssertButtonDisabled("button.delete");
		this.controller.selectTableItem(actionFQN);
		this.controller.webClickButtonToolbar("button.delete");
		this.assertRemoveAction(actionFQN, data.getTitle());
	}

	@Test(groups = {TestConstants.PUSH}, dataProvider = "removeAction", dataProviderClass = ActionTestDataProvider.class, priority = 1)
	public void cancelRemoveAction(ActionTestData data) {
		MessageLoggers.infoLogger("Delete action: " + data.getName() + " by menu item then cancel");
		String actionFQN = this.getActionFQN(data);
		this.controller.goToNavigationTreeItem(data.getPackName());
		this.controller.selectTableItem(actionFQN);
		this.controller.webClickMenuItemOnTable(actionFQN, "button.delete");
		this.controller.webClickButtonDialog("button.no");
		this.controller.webWaitForElementToDissapear(
				this.controller.getChildElementContainsText(
						XPathKeys.POPUP_HEADER,
						Messages.getInstance().getMessage("label.dialog.header.delete_action")),
				Timeouts.DELETE_SAFETY_MARGIN);
		this.controller.webAssertItemsOnTable(actionFQN);
	}

	private void assertRemoveAction(String name, String title) {
		MessageLoggers.infoLogger("Remove an action: " + name);
		this.controller.webAssertElementVisibility(
				this.controller.getLabelElement("label.behavior.action.delete.confirm", title),
				Visibility.VISIBLE);
		this.controller.webClickButtonDialog("button.yes");
		this.controller.webWaitForElementToAppear(
				this.controller.getLabelElement("label.behavior.action.delete.title.success", title),
				Timeouts.OPEN_OBJECT);
		this.controller.webWaitForElementToAppear(
				this.controller.getChildElementContainsText(
						XPathKeys.NOTIFICATION_INFO,
						Messages.getInstance().getMessage("label.behavior.action.delete.notify.success")),
				Timeouts.OPEN_OBJECT);
		this.controller.webAssertElementVisibility(
				this.controller.getChildElementContainsText(
						XPathKeys.PANEL_LOG_LINE,
						Messages.getInstance().getMessage("label.behavior.action.delete.log.success", name)),
				Visibility.VISIBLE);
		this.controller.webClickButtonDialog("button.close");
		this.controller.webWaitForElementToDissapear(
				this.controller.getChildElementContainsText(
						XPathKeys.POPUP_HEADER,
						Messages.getInstance().getMessage("label.dialog.header.delete_action")),
				Timeouts.DELETE_SAFETY_MARGIN);
		this.controller
				.webWaitForElementToDissapear(this.controller.getChildElementContainsText(XPathKeys.TABLE_ROW, name), Timeouts.OPEN_OBJECT);
	}
}
