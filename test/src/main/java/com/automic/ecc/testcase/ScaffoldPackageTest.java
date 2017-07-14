package com.automic.ecc.testcase;

import java.util.List;

import org.testng.annotations.Test;

import com.automic.ecc.core.actionLibraries.XPathKeys;
import com.automic.ecc.core.utils.Messages;
import com.automic.ecc.core.utils.TestConstants;
import com.automic.ecc.testdata.PackTestDataProvider;

import testDrivers.WEBdriver.Visibility;
import testUtils.MessageLoggers;

public class ScaffoldPackageTest extends PackTest {

	/**
	 *
	 * @param name
	 * @param title
	 * @see PackTestDataProvider#normalPack()
	 */
	@Test(groups = {TestConstants.PUSH}, dataProvider = "normalPack", dataProviderClass = PackTestDataProvider.class)
	public void testCreateNormalPackage(String name, String title) {
		String packName = this.getPackFQN(name, title);
		INSTALLED_PACK_IN_TC.add(packName);
		if (name == "")
			this.createPackage(title);
		else
			this.createPackage(title, name);
		this.controller.webAssertElementWithText(
				XPathKeys.NOTIFICATION_INFO,
				Messages.getInstance().getMessage("label.behavior.pack.create.success"));
		// TODO
		MessageLoggers.infoLogger("Assert title and name");
		this.controller.webClickMenuItemOnNavigationTree(packName, "menu.jump");
		this.assertPack(packName);
	}

	/**
	 *
	 * @param name
	 * @param title
	 * @see PackTestDataProvider#normalPack()
	 */
	@Test(dataProvider = "existedPack", dataProviderClass = PackTestDataProvider.class)
	public void testCreateExistedPackage(String name, String title) {
		if (name == "")
			this.createPackage(title);
		else
			this.createPackage(title, name);
		this.controller.webAssertElementVisibility(XPathKeys.NOTIFICATION_WARNING, Visibility.VISIBLE);
		this.controller.webClickButtonDialog("button.close");
	}

	/**
	 *
	 * @param name
	 * @param title
	 * @param messages
	 * @see PackTestDataProvider#abnormalPack()
	 */
	@Test(dataProvider = "abnormalPack", dataProviderClass = PackTestDataProvider.class)
	public void testCreateAbnormalPack(String title, String name, List<String> messages) {
		this.createPackage(title, name);
		this.controller.assertTooltipError(messages);
		this.controller.webClickButtonDialog("button.cancel");
	}

}
