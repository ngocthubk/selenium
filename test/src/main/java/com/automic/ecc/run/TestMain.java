package com.automic.ecc.run;

import java.io.IOException;
import java.net.URISyntaxException;

public final class TestMain {

	private static final String JAVA_PACKAGE_TEST = "com.automic.ecc.testcase";
	private static final String TEST_PREFIX = "ActionBuilder-";

	public static void main(String[] args) throws IOException, URISyntaxException {
		new TestLauncher(TEST_PREFIX, JAVA_PACKAGE_TEST).execute();
	}

}
