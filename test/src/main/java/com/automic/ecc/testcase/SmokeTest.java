package com.automic.ecc.testcase;

import org.testng.annotations.Test;

import com.automic.ecc.core.actionLibraries.XPathKeys;
import com.automic.ecc.core.utils.Messages;
import com.automic.ecc.core.utils.TestConstants;
import com.automic.ecc.testdata.ActionTestDataProvider.ActionTestData;

import testUtils.MessageLoggers;

public class SmokeTest extends ActionTest {

	PackTest pack = new PackTest();

	/**
	 * Full scenario for action
	 * Step 1: Create a package
	 * Step 2: Create an action
	 * @throws Exception
	 */
	@Test(groups = {TestConstants.SMOKE})
	public void fullScenario() throws Exception {
		MessageLoggers.infoLogger("Create, browse and verify actions");
		String title = "CLI";
		String category = "AUTO";
		String packageTitle = "SMOKE";
		String packageName = "PCK.CUSTOM_SMOKE";
		INSTALLED_PACK_IN_TC.add(packageName);
		ActionTestData data = new ActionTestData(packageName, "CLI", title, title, category, false);

		// Step 1
		pack.createPackage(packageTitle);
		this.controller.webWaitForElementToDissapear(
				this.controller.getChildElementMatchText(
						XPathKeys.POPUP_HEADER,
						Messages.getInstance().getMessage("label.dialog.header.create_pack")),
				90);
		this.controller.webClickMenuItemOnNavigationTree(packageName, "menu.jump");
		pack.assertPack(packageName);
		this.controller.webClickElementWithText(XPathKeys.TOP_TAB, Messages.getInstance().getMessage("explorer.tab"));;
		this.controller.webClickElementWithText(XPathKeys.ACCORDION, Messages.getInstance().getMessage("label.accordion.packs"));
		this.controller.goToNavigationTreeItem(packageName);
		// Step 2
		this.createAction(data);
		this.assertCreateAction(data);

	}
}
