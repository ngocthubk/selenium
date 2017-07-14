package com.uc4.ecc.plugins.actionbuilder.utils;

public final class ActionBuilderConstants {

	public static final String NOTIFICATION_ID = "actionBuilderNotification";
	public static final String BLANK = "";
	public static final String ALL_SPACES_REGEX = "\\s+";
	public static final String UNDERSCORE = "_";
	public static final String UC4_FOLDER_INVALID_CHAR_REGEX = "[^A-Za-z0-9@_\\$\\.\\-\\#]";
	public static final String UC4_CATEGORY_REGEX = "^\\/?[A-Za-z0-9@_\\$\\.\\-]{1,180}(\\/{1}[A-Za-z0-9@_\\$\\.\\-]{1,180})*\\/?$";
	public static final String HYPHEN = "-";

	public static final String PROPS_CATEGORY = "category";
	public static final String PROPS_TITLE = "title";
	public static final String PROPS_NAME = "name";

	public static final class METADATA {

		public final static String VERSION_KEY = "Version";
		public final static String LIBRARY_KEY = "Library";
		public final static String EXPORTED_KEY = "Exported";

	}

}
