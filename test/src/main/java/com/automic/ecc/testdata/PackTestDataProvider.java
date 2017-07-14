package com.automic.ecc.testdata;

import java.util.Arrays;

import org.testng.annotations.DataProvider;

import com.automic.ecc.testcase.ScaffoldPackageTest;

public class PackTestDataProvider {

	/**
	 * @see ScaffoldPackageTest#testCreateNormalPackage(String, String)
	 * @return
	 */
	@DataProvider(name = "normalPack")
	public static Object[][] normalPack() {
		String[] data1 = {"Sample", "Sample"};
		String[] data2 = {"", "!@#$%^&*()~-Special"};
		return new Object[][] {data1, data2};
	}

	/**
	 * @see ScaffoldPackageTest#testCreateAbnormalPack(String, String, String)
	 * @return
	 */
	@DataProvider(name = "abnormalPack")
	public static Object[][] abnormalPack() {
		Object[] data1 = {
				"",
				"qwertyuiopasdfghjklzxcvbnmmmmmqwertyuiopasdf12345678",
				Arrays.asList("Title must not be empty", "The maximum length is 50 characters")};
		Object[] data2 = {"", "xyz", Arrays.asList("Title must not be empty", "")};
		return new Object[][] {data1, data2};
	}

	@DataProvider(name = "exportNormalPack")
	public static Object[][] exportNormalPack() {
		return new Object[][] {new String[] {"PCK.ITPA_SHARED"}};
	}

	@DataProvider(name = "existedPack")
	public static Object[][] existedPack() {
		return new Object[][] {new String[] {"TEST", "TEST"}};
	}

	@DataProvider(name = "cloneNormalPack")
	public static Object[][] cloneNormalPack() {
		String[] data1 = {"PCK.AUTOMIC_CONNECTIVITY", "CONNECTIVITY-CLONE", "CONNECTIVITY-CLONE"};
		return new Object[][] {data1};
	}

}
