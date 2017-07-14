package com.automic.ecc.testdata;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.DataProvider;

import com.automic.ecc.core.utils.Messages;
import com.automic.ecc.core.utils.TestConstants;

public final class ActionTestDataProvider {

	public static class ActionTestData {

		private final String packName;
		private final String type;
		private final String title;
		private final String inputName;
		private final String name;
		private final String category;
		private final boolean categoryExisted;
		private final List<String> validations;

		public ActionTestData(String packName, String title, String name) {
			this(packName, "", title, name, "", false);
		}

		public ActionTestData(String packName, String type, String title, String name, String category, boolean categoryExisted) {
			this.packName = packName;
			this.type = type;
			this.title = title;
			this.inputName = name;
			this.name = (StringUtils.isBlank(name) ? this.title.replaceAll(TestConstants.REGEX, "_") : name).toUpperCase();
			this.category = category;
			this.categoryExisted = categoryExisted;
			this.validations = new ArrayList<String>();
		}

		public String getPackName() {
			return this.packName;
		}

		public String getType() {
			return this.type;
		}

		public String getTitle() {
			return this.title;
		}

		public String getInputName() {
			return this.inputName;
		}

		public String getName() {
			return this.name;
		}

		public String getCategory() {
			return this.category;
		}

		public boolean isCategoryExisted() {
			return this.categoryExisted;
		}

		public void addValidation(String... messages) {
			for (String message : messages) {
				this.validations.add(message);
			}
		}

		public List<String> getValidations() {
			return this.validations;
		}

		@Override
		public String toString() {
			return "ActionTestData [packName=" + packName + ", type=" + type + ", title=" + title + ", inputName=" + inputName + ", name="
					+ name + ", category=" + category + ", categoryExisted=" + categoryExisted + ", validations=" + validations + "]";
		}

	}

	public static class CloneActionTestData extends ActionTestData {

		private final String targetPackName;

		public CloneActionTestData(String packName, String targetPackName, String title) {
			super(packName, title, "");
			this.targetPackName = targetPackName;
		}

		public String getTargetPackName() {
			return targetPackName;
		}

		@Override
		public String toString() {
			return "CloneActionTestData [targetPackName=" + targetPackName + ", " + super.toString() + "]";
		}

	}

	@DataProvider(name = "normalAction")
	public static Object[][] normalAction() {
		ActionTestData data1 = new ActionTestData("PCK.CUSTOM_TEST", "CLI", "Action-CLI-1", "ACTION-CLI-1", "AUTO", false);
		ActionTestData data2 =
				new ActionTestData("PCK.CUSTOM_TEST", "CLI", "Action-~!@#$%^&*()-Special", "ACTION-__@#$______-SPECIAL", "AUTO", true);
		ActionTestData data3 = new ActionTestData("PCK.CUSTOM_TEST", "REST", "Action-REST-1", "ACTION-REST-1", "AUTO/REST", false);
		return new Object[][] {{data1}, {data2}, {data3}};
	}

	@DataProvider(name = "existedAction")
	public static Object[][] existedAction() {
		ActionTestData data1 = new ActionTestData("PCK.CUSTOM_TEST", "CLI", "REMOVE_ACTION", "REMOVE_ACTION", "AUTO", false);

		return new Object[][] {{data1}};
	}

	@DataProvider(name = "abnormalAction")
	public static Object[][] abnormalAction() {
		ActionTestData data1 = new ActionTestData("PCK.CUSTOM_TEST", "CLI", "", "", "", false);
		data1.addValidation(
				Messages.getInstance().getMessage("label.behavior.action.validate.title.empty"),
				Messages.getInstance().getMessage("label.behavior.action.validate.category.empty"),
				Messages.getInstance().getMessage("label.behavior.action.validate.name.empty"));
		ActionTestData data2 = new ActionTestData("PCK.CUSTOM_TEST", "CLI", "Invalid Category", "INVALID", "A~!@#$%^&*()", false);
		data2.addValidation(Messages.getInstance().getMessage("label.behavior.action.validate.category.invalid"));
		ActionTestData data3 = new ActionTestData(
				"PCK.CUSTOM_TEST",
				"CLI",
				"QWERTYUIOPASDFGHJKLZXCVBNQWERTYUIOPASDFGHJKLZXCVBNQWERTYUIOPASDFGHJKLZXCVBNQWERTYUIOPASDFGHJKLZXCVBNQWERT9081RTYUIOPASDFGHJKLZXCVBNQWER",
				"QWERTYUIOPASDFGHJKLZXCVBNQWERTYUIOPASDFGHJKLZXCVBNQWERTYUIOPASDFGHJKLZXCVBNQWERTYUIOPASDFGHJKLZXCVBNQWERT9081RTYUIOPASDFGHJKLZXCVBNQWER",
				"AUTO",
				false);
		data3.addValidation(Messages.getInstance().getMessage("label.behavior.action.validate.name.max"));
		return new Object[][] {{data1}, {data2}, {data3}};
	}

	@DataProvider(name = "removeAction")
	public static Object[][] removeAction() {
		ActionTestData data = new ActionTestData("PCK.CUSTOM_TEST", "REMOVE_ACTION", "REMOVE_ACTION");
		return new Object[][] {{data}};
	}

	@DataProvider(name = "cloneAction")
	public static Object[][] cloneAction() {
		ActionTestData data = new ActionTestData("PCK.AUTOMIC_CONNECTIVITY", "Clone PING", "CLONE_PING");
		return new Object[][] {{data, "PCK.AUTOMIC_CONNECTIVITY.PUB.ACTION.PING"}};
	}

	@DataProvider(name = "clone2AnotherPack")
	public static Object[][] clone2AnotherPack() {
		CloneActionTestData data = new CloneActionTestData("PCK.AUTOMIC_CONNECTIVITY", "PCK.CUSTOM_TEST", "Clone CONNECTIVITY To TEST");
		return new Object[][] {{data, "PCK.AUTOMIC_CONNECTIVITY.PUB.ACTION.PING"}};
	}

	@DataProvider(name = "validateCloneAction")
	public static Object[][] cloneAbnormalAction() {
		ActionTestData data = new ActionTestData("PCK.AUTOMIC_CONNECTIVITY", "A~!@#$%^&*(){}';|><?/", "A~!@#$%^&*(){}';|><?/NAME");
		data.addValidation(Messages.getInstance().getMessage("label.behavior.action.validate.name.invalid"));
		ActionTestData data2 = new ActionTestData("PCK.AUTOMIC_CONNECTIVITY", "", "");
		data2.addValidation(
				Messages.getInstance().getMessage("label.behavior.action.validate.title.empty"),
				Messages.getInstance().getMessage("label.behavior.action.validate.name.empty"));
		return new Object[][] {{data, "PCK.AUTOMIC_CONNECTIVITY.PUB.ACTION.PING"}, {data2, "PCK.AUTOMIC_CONNECTIVITY.PUB.ACTION.PING"}};
	}

	@DataProvider(name = "cloneExistedAction")
	public static Object[][] cloneExistedAction() {
		ActionTestData data = new ActionTestData("PCK.AUTOMIC_CONNECTIVITY", "PING", "PING");
		data.addValidation(Messages.getInstance().getMessage("label.behavior.action.clone.exists"));
		return new Object[][] {{data, "PCK.AUTOMIC_CONNECTIVITY.PUB.ACTION.PING"}};
	}
}
