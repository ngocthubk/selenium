package com.automic.ecc.testcase;

import org.testng.annotations.Test;

import com.automic.ecc.core.actionLibraries.XPathKeys;
import com.automic.ecc.core.utils.TestConstants;
import com.automic.ecc.testdata.ActionTestDataProvider;
import com.automic.ecc.testdata.ActionTestDataProvider.ActionTestData;

import testDrivers.WEBdriver.Visibility;
import testUtils.MessageLoggers;

public final class ScaffoldActionTest extends ActionTest {

	/**
	 * Create a normal Action
	 * @param data
	 * @see ActionTestDataProvider#normalAction()
	 */
	@Test(groups = {TestConstants.PUSH}, dataProvider = "normalAction", dataProviderClass = ActionTestDataProvider.class)
	public void testNormalAction(ActionTestData data) {
		MessageLoggers.infoLogger("Create, browse and verify actions");
		this.controller.goToNavigationTreeItem(data.getPackName());
		this.createAction(data);
		if (data.getType().equals("REST")) {
			this.controller
					.webAssertElementVisibility(this.controller.getLabelElement("label.behavior.action.add.rest.auth"), Visibility.VISIBLE);
			this.controller.webClickButtonDialog("button.next");
		}
		this.assertCreateAction(data);
	}

	/**
	 * Create an Action with its name existed
	 * @param data
	 * @see ActionTestDataProvider#normalAction()
	 */
	@Test(groups = {TestConstants.PUSH}, dataProvider = "existedAction", dataProviderClass = ActionTestDataProvider.class)
	public void testExistedAction(ActionTestData data) {
		this.controller.goToNavigationTreeItem(data.getPackName());
		this.createAction(data);
		if (data.getType().equals("REST")) {
			this.controller.webClickButtonDialog("button.next");
		}
		this.controller.webWaitForElementToAppear(XPathKeys.NOTIFICATION_WARNING, 60);
		this.controller.webClickButtonDialog("button.close");
		this.controller.webClickButtonDialog("button.cancel");
	}

	/**
	 *
	 * @param data
	 * @see ActionTestDataProvider#abnormalAction()
	 */
	@Test(dataProvider = "abnormalAction", dataProviderClass = ActionTestDataProvider.class)
	public void testAbnormalAction(ActionTestData data) {
		this.controller.goToNavigationTreeItem(data.getPackName());
		this.createAction(data);
		this.controller.assertTooltipError(data.getValidations());
		this.controller.webClickButtonDialog("button.cancel");
	}

	@Test(groups = {
			"ActionTest"}, dataProvider = "createAction", dataProviderClass = ActionTestDataProvider.class, priority = 1, enabled = false)
	public void cancelAtStepOne() {}

	@Test(groups = {
			"ActionTest"}, dataProvider = "createAction", dataProviderClass = ActionTestDataProvider.class, priority = 2, enabled = false)
	public void cancelAtStepTwo() {}

}
