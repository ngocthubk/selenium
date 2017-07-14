package com.uc4.ecc.plugins.actionbuilder.content;

import com.uc4.ecc.framework.commons.css.Icons;
import com.uc4.ecc.framework.commons.utils.BundleIcon;
import com.uc4.ecc.framework.commons.utils.BundleTheme;
import com.uc4.ecc.framework.core.interfaces.IIcon;

public class IconsActionBuilder extends Icons {

	private static final BundleTheme BUNDLE_THEME = new BundleTheme(IconsActionBuilder.class);
	private static final int SIZE_32 = 32;

	public static final IIcon ACTION_BUILDER_ICON =
			new BundleIcon(BUNDLE_THEME, "imgs/action_builder_24.png", SIZE_24).withSize(SIZE_16, "imgs/action_builder_16.png");
	public static final IIcon PACKAGE_ICON =
			new BundleIcon(BUNDLE_THEME, "imgs/package_24.png", SIZE_24).withSize(SIZE_16, "imgs/package_16.png");
	public static final IIcon DELETE_ICON = new BundleIcon(BUNDLE_THEME, "imgs/delete_16.png", SIZE_16);
	public static final IIcon EDIT_IN_NEW_WINDOW = new BundleIcon(BUNDLE_THEME, "imgs/open_in_window.png", SIZE_16);
	public static final IIcon LOADING_ICON = new BundleIcon(BUNDLE_THEME, "imgs/rectangle_animation_32.gif", SIZE_32);
}
