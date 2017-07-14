package com.automic.ecc.testcase;

import java.util.Arrays;

import org.testng.annotations.Test;

import testDrivers.Timeouts;
import testDrivers.WEBdriver.Visibility;
import testUtils.MessageLoggers;

import com.automic.ecc.core.actionLibraries.XPathKeys;
import com.automic.ecc.core.utils.Messages;
import com.automic.ecc.core.utils.TestConstants;
import com.automic.ecc.testdata.PackTestDataProvider;

// TODO Export with warning
public class ExportPackageTest extends PackTest {

	@Test(groups = {TestConstants.PUSH}, dataProvider = "exportNormalPack", dataProviderClass = PackTestDataProvider.class)
	public void exportPack(String packName) {
		MessageLoggers.infoLogger("Export pack: " + packName + " on toolbar");
		this.controller.goToNavigationTreeItem(packName);
		this.controller.webClickButtonToolbar("button.export");
		this.controller.webAssertElementWithText(XPathKeys.POPUP_HEADER, Messages.getInstance().getMessage("label.dialog.header.export"));
		this.controller.webAssertElementVisibility(this.controller.getLabelElement("label.export.messages"), Visibility.VISIBLE);
		this.controller
				.webAssertListInLabel(Arrays.asList("label.export.messages.1", "label.export.messages.2", "label.export.messages.3"));
		this.controller.webClickButtonDialog("button.export");
		// cannot download for now because must rewrite WebDriver, also remove file after download
		this.controller.webAssertElementWithText(XPathKeys.BUTTON_DIALOG, Messages.getInstance().getMessage("button.download"));
	}

	@Test(dataProvider = "exportNormalPack", dataProviderClass = PackTestDataProvider.class)
	public void cancelExportPackage(String packName) {
		MessageLoggers.infoLogger("Export pack: " + packName + " on navigation then cancel");
		this.controller.goToNavigationTreeItem(packName);
		this.controller.webClickMenuItemOnNavigationTree(packName, "button.export");
		this.controller.webClickButtonDialog("button.cancel");
		this.controller.webWaitForElementToDissapear(
				this.controller.getChildElementContainsText(
						XPathKeys.POPUP_HEADER,
						Messages.getInstance().getMessage("label.dialog.header.export")),
				Timeouts.DELETE_SAFETY_MARGIN);
	}

}
