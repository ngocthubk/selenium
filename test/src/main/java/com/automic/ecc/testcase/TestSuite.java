package com.automic.ecc.testcase;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.automic.ecc.core.actionLibraries.ConnConfig;
import com.automic.ecc.core.actionLibraries.WebUIActions;
import com.automic.ecc.core.actionLibraries.XPathKeys;
import com.automic.ecc.core.utils.APMUtils;
import com.automic.ecc.core.utils.CmdLineUtils;
import com.automic.ecc.core.utils.Messages;
import com.automic.ecc.tests.ui.keys.EccHomeKeys;

import testDrivers.WEBdriver;
import testUtils.MessageLoggers;

public class TestSuite {

	protected final WebUIActions controller;
	protected static final Set<String> INSTALLED_PACK_IN_TC = new HashSet<String>();

	protected TestSuite() {
		this.controller = WebUIActions.getInstance();
	}

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() {
		ConnConfig connection = this.controller.getConnection();
		List<String> commands = APMUtils.buildInstallCommand(connection);
		int status = CmdLineUtils.execute(new File(connection.getAPMHome()), commands);
		if (status != 0) {
			this.controller.throwException("Cannot install require packages");
		}
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() {
		ConnConfig connection = this.controller.getConnection();
		List<String> commands = APMUtils.buildRemoveCommand(connection, INSTALLED_PACK_IN_TC);
		int status = CmdLineUtils.execute(new File(connection.getAPMHome()), commands);
		if (status != 0) {
			this.controller.throwException("Cannot remove require packages");
		}
	}

	@BeforeMethod(alwaysRun = true)
	public void precondition(ITestContext context) {
		this.controller.getAeActions().register(context);
		this.login();
		this.goToActionBuilder();
	}

	@AfterMethod(alwaysRun = true)
	public final void tearDown() {
		this.controller.cleanupCloseBrowser();
	}

	private void login() {
		MessageLoggers.infoLogger("Loading Action Builder with URL fragment and login");
		ConnConfig connection = this.controller.getConnection();
		try {
			this.controller.login(
					connection.getHub(),
					connection.getBrowser(),
					connection.getURL(),
					connection.getLanguage(),
					connection.getAWIConn(),
					connection.getClient(),
					connection.getUser(),
					connection.getDepartment(),
					connection.getPassword());
		} catch (InterruptedException e) {
			WEBdriver.throwException("Cannot login into system", e);
		}
	}

	protected final void goToActionBuilder() {
		this.controller.webClick(EccHomeKeys.ADD_PERSPECTIVE);
		this.controller.webClick(EccHomeKeys.ADD_PERSPECTIVE_PROCESS_ASSEMBLY);
		this.controller.webClickElementWithText(XPathKeys.ACCORDION, Messages.getInstance().getMessage("label.accordion.packs"));
		MessageLoggers.infoLogger("Action Builder is up");
	}

}
