package com.automic.ecc.testcase;

import java.util.Arrays;

import org.testng.annotations.Test;

import com.automic.ecc.core.actionLibraries.XPathKeys;
import com.automic.ecc.core.utils.Messages;
import com.automic.ecc.core.utils.TestConstants;
import com.automic.ecc.testdata.ActionTestDataProvider;
import com.automic.ecc.testdata.ActionTestDataProvider.ActionTestData;
import com.automic.ecc.testdata.ActionTestDataProvider.CloneActionTestData;

import testDrivers.WEBdriver.Visibility;
import testUtils.MessageLoggers;

public class CloneActionTest extends ActionTest {

	@Test(groups = {TestConstants.PUSH}, dataProvider = "cloneAction", dataProviderClass = ActionTestDataProvider.class, priority = 2)
	public void cloneAction(ActionTestData data, String sourceAction) {
		MessageLoggers.infoLogger("Clone action: " + data.getName() + " on toolbar");
		String actionFQN = this.getActionFQN(data);
		this.controller.goToNavigationTreeItem(data.getPackName());
		// this.controller.webAssertButtonDisabled("button.clone");
		this.controller.selectTableItem(sourceAction);
		this.controller.webClickButtonToolbar("button.clone");
		this.controller.webWaitForElementToAppear(
				this.controller.getChildElementMatchText(
						XPathKeys.POPUP_HEADER,
						Messages.getInstance().getMessage("label.dialog.header.clone_action")),
				30);
		this.controller.inputTextValuesInForm(Arrays.asList(data.getTitle(), data.getInputName()));
		this.controller.webClickButtonDialog("button.clone");
		this.controller.webAssertElementVisibility(
				this.controller.getLabelElement("label.behavior.action.clone.progress", sourceAction, actionFQN),
				Visibility.VISIBLE);
		// this.controller.webAssertButtonDisabled("button.close");
		this.controller.webWaitForElementToAppear(this.controller.getLabelElement("label.behavior.action.clone.success", actionFQN), 90);
		this.controller.webAssertElementVisibility(
				this.controller.getElementContainsText(
						XPathKeys.LABEL,
						Messages.getInstance().getMessage("label.behavior.action.clone.log.success", actionFQN)),
				Visibility.VISIBLE);
		this.controller.webClickButtonDialog("button.close");
		this.controller.webClickMenuItemOnTable(actionFQN, "menu.jump");
		// TODO assert in detail
	}

	@Test(dataProvider = "cloneAction", dataProviderClass = ActionTestDataProvider.class, priority = 1)
	public void cancelCloneAction(ActionTestData data, String originAction) {
		MessageLoggers.infoLogger("Clone action: " + data.getName() + " by menu item then cancel");
		this.controller.goToNavigationTreeItem(data.getPackName());
		this.controller.webClickMenuItemOnTable(originAction, "button.clone");
		this.controller.webWaitForElementToAppear(
				this.controller.getChildElementMatchText(
						XPathKeys.POPUP_HEADER,
						Messages.getInstance().getMessage("label.dialog.header.clone_action")),
				30);
		String input = this.controller.buildElement(0, XPathKeys.POPUP, XPathKeys.INPUT_TEXTS);
		this.controller.webSetText(input, "");
		this.controller.inputTextValuesInForm(Arrays.asList(data.getTitle(), data.getInputName()));
		this.controller.webClickButtonDialog("button.cancel");
		// TODO assert
	}

	@Test(dataProvider = "validateCloneAction", dataProviderClass = ActionTestDataProvider.class)
	public void cloneAbnormalAction(ActionTestData data, String sourceAction) {
		this.controller.goToNavigationTreeItem(data.getPackName());
		this.controller.webClickMenuItemOnTable(sourceAction, "button.clone");
		this.controller.webWaitForElementToAppear(
				this.controller.getChildElementMatchText(
						XPathKeys.POPUP_HEADER,
						Messages.getInstance().getMessage("label.dialog.header.clone_action")),
				30);
		if (data.getName().isEmpty())
			this.controller.inputTextValuesInForm(Arrays.asList(data.getTitle()));
		else
			this.controller.inputTextValuesInForm(Arrays.asList(data.getTitle(), data.getName()));
		this.controller.webClickButtonDialog("button.clone");
		this.controller.assertTooltipError(data.getValidations());
		this.controller.webClickButtonDialog("button.cancel");
	}

	@Test(dataProvider = "cloneExistedAction", dataProviderClass = ActionTestDataProvider.class)
	public void cloneExistedAction(ActionTestData data, String sourceAction) {
		this.controller.goToNavigationTreeItem(data.getPackName());
		this.controller.webClickMenuItemOnTable(sourceAction, "button.clone");

		this.controller.inputTextValuesInForm(Arrays.asList(data.getTitle(), data.getName()));
		this.controller.webClickButtonDialog("button.clone");
		this.controller.getLabelElement("label.behavior.action.clone.exists", data.getName());
		this.controller.webClickButtonDialog("button.close");

	}

	@Test(dataProvider = "clone2AnotherPack", dataProviderClass = ActionTestDataProvider.class)
	public void cloneToAnotherPack(CloneActionTestData data, String sourceAction) {
		MessageLoggers.infoLogger("Clone action: " + data.getName() + " on toolbar");
		String actionFQN = this.getActionFQN(data);
		this.controller.goToNavigationTreeItem(data.getPackName());
		// this.controller.webAssertButtonDisabled("button.clone");
		this.controller.selectTableItem(sourceAction);
		this.controller.webClickButtonToolbar("button.clone");
		this.controller
				.webAssertElementWithText(XPathKeys.POPUP_HEADER, Messages.getInstance().getMessage("label.dialog.header.clone_action"));
		this.controller.inputCombobox(data.getTargetPackName());
		this.controller.inputTextValuesInForm(Arrays.asList(data.getTitle()));
		this.controller.webClickButtonDialog("button.clone");
		this.controller.webAssertElementVisibility(
				this.controller.getLabelElement("label.behavior.action.clone.progress", sourceAction, actionFQN),
				Visibility.VISIBLE);
		// this.controller.webAssertButtonDisabled("button.close");
		this.controller.webWaitForElementToAppear(this.controller.getLabelElement("label.behavior.action.clone.success", actionFQN), 90);
		// TODO disable temporary
		// this.controller.webAssertElementVisibility(
		// this.controller.getChildElementContainsText(
		// XPathKeys.LABEL,
		// Messages.getInstance().getMessage("label.behavior.action.clone.log.success", actionFQN)),
		// Visibility.VISIBLE);
		this.controller.webClickButtonDialog("button.close");
		this.controller.webAssertItemsOnTable(actionFQN);
		// TODO assert in detail
	}

}
