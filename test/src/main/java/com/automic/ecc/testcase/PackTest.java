package com.automic.ecc.testcase;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.automic.ecc.core.actionLibraries.XPathKeys;
import com.automic.ecc.core.utils.Messages;
import com.automic.ecc.core.utils.TestConstants;

import testUtils.MessageLoggers;

public class PackTest extends TestSuite {

	protected void createPackage(String title, String... name) {
		MessageLoggers.infoLogger("Create a package with title" + title);
		this.controller.webClickButton("button.create_pack");
		this.controller.webWaitForElementToAppear(
				this.controller.getChildElementMatchText(
						XPathKeys.POPUP_HEADER,
						Messages.getInstance().getMessage("label.dialog.header.create_pack")),
				30);
		if (name.length > 0)
			this.controller.inputTextValuesInForm(Arrays.asList(title, name[0]));
		else
			this.controller.inputTextValuesInForm(Arrays.asList(title));
		this.controller.webClickButtonDialog("button.create_pack");
	}

	protected final String getPackFQN(String name, String title) {
		String genarate = StringUtils.isBlank(name) ? title.replaceAll(TestConstants.REGEX, "_") : name;
		return "PCK.CUSTOM_" + genarate.toUpperCase();
	}

	protected final void assertPackInRegistry(String packName) {
		this.controller.goToNavigationTreeItem(Messages.getInstance().getMessage("explorer.packages"));
		this.controller.webDoubleClick(XPathKeys.TABLE_ROW, Messages.getInstance().getMessage("explorer.pack.registry"));
		this.controller.webAssertElementWithText(XPathKeys.MENU_LEFT, Messages.getInstance().getMessage("explorer.var.object.variable"));
		this.controller.webAssertItemsOnTable(packName);
	}

	protected final void assertPackStructure(String packName) {
		this.controller.webAssertItemsOnTable(
				Messages.getInstance().getMessage("explorer.pack.actions"),
				Messages.getInstance().getMessage("explorer.pack.config"),
				Messages.getInstance().getMessage("explorer.pack.documentation"),
				Messages.getInstance().getMessage("explorer.pack.resources"),
				Messages.getInstance().getMessage("explorer.pack.source"),
				Messages.getInstance().getMessage("explorer.pack.templates"),
				Messages.getInstance().getMessage("explorer.pack.metadata", packName));
	}

	protected final void assertPackSubFolder(String packName, String parent, String... children) {
		this.controller.goToNavigationTreeItem(Messages.getInstance().getMessage("explorer.packages"), packName);
		this.controller.goToTableItem(packName, parent);
		this.controller.webAssertItemsOnTable(children);
	}

	protected final void assertPack(String packName) {
		this.assertPackStructure(packName);
		this.assertPackSubFolder(
				packName,
				Messages.getInstance().getMessage("explorer.pack.config"),
				Messages.getInstance().getMessage("explorer.pack.config.object.ext_map", packName));
		this.assertPackSubFolder(
				packName,
				Messages.getInstance().getMessage("explorer.pack.documentation"),
				Messages.getInstance().getMessage("explorer.pack.documentation.object.doc", packName),
				Messages.getInstance().getMessage("explorer.pack.documentation.object.licenses", packName));
		this.assertPackSubFolder(
				packName,
				Messages.getInstance().getMessage("explorer.pack.resources"),
				Messages.getInstance().getMessage("explorer.pack.resources.interpreters"),
				Messages.getInstance().getMessage("explorer.pack.resources.libs"),
				Messages.getInstance().getMessage("explorer.pack.resources.scripts"));
		this.controller.goToNavigationTreeItem(Messages.getInstance().getMessage("explorer.packages"));
		this.assertPackInRegistry(packName);
	}

}
