package com.automic.ecc.testcase;

import java.util.Arrays;

import org.testng.annotations.Test;

import com.automic.ecc.core.actionLibraries.XPathKeys;
import com.automic.ecc.core.utils.Messages;
import com.automic.ecc.core.utils.TestConstants;
import com.automic.ecc.testdata.PackTestDataProvider;

import testDrivers.WEBdriver.Visibility;
import testUtils.MessageLoggers;

public final class ClonePackTest extends PackTest {

	/**
	 *
	 * @param originPack
	 * @param title
	 * @param name
	 * @see PackTestDataProvider#cloneNormalPack()
	 */
	@Test(groups = {
			TestConstants.PUSH,
			"ClonePackTest"}, dataProvider = "cloneNormalPack", dataProviderClass = PackTestDataProvider.class, priority = 2)
	public void clonePack(String originPack, String title, String name) {
		MessageLoggers.infoLogger("Clone pack: " + originPack + " on toolbar");
		this.controller.goToNavigationTreeItem(originPack);
		this.controller.webClickButtonToolbar("button.clone_pack");
		this.controller.webWaitForElement(
				this.controller.getChildElementMatchText(
						XPathKeys.POPUP_HEADER,
						Messages.getInstance().getMessage("label.dialog.header.clone_pack")),
				30);
		this.controller.inputTextValuesInForm(Arrays.asList(title, name));
		this.controller.webClickButtonDialog("button.clone");
		String packName = this.getPackFQN(name, title);
		INSTALLED_PACK_IN_TC.add(packName);
		this.controller.webAssertElementVisibility(
				this.controller.getLabelElement("label.behavior.pack.clone.progress", originPack, packName),
				Visibility.VISIBLE);
		// this.controller.webAssertButtonDisabled("button.close");
		this.controller.webWaitForElementToAppear(this.controller.getLabelElement("label.behavior.pack.clone.success", packName), 360);
		this.controller.webAssertElementVisibility(
				this.controller.getChildElementContainsText(
						XPathKeys.LABEL,
						Messages.getInstance().getMessage("label.behavior.pack.clone.log.success", packName, originPack)),
				Visibility.VISIBLE);
		this.controller.webClickButtonDialog("button.close");
		this.controller.webWaitImplicit(500);
		this.controller.webClickMenuItemOnNavigationTree(packName, "menu.jump");
		this.assertPack(packName);
	}

	/**
	 *
	 * @param originPack
	 * @param title
	 * @param name
	 * @see PackTestDataProvider#cloneNormalPack()
	 */
	@Test(groups = {"ClonePackTest"}, dataProvider = "cloneNormalPack", dataProviderClass = PackTestDataProvider.class, priority = 1)
	public void cancelClonePack(String originPack, String title, String name) {
		MessageLoggers.infoLogger("Clone pack: " + originPack + " by menu item then cancel");
		this.controller.webClickMenuItemOnNavigationTree(originPack, "button.clone");
		this.controller.inputTextValuesInForm(Arrays.asList(title, name));
		this.controller.webClickButtonDialog("button.cancel");
		this.controller.goToNavigationTreeItem(originPack);
	}

}
