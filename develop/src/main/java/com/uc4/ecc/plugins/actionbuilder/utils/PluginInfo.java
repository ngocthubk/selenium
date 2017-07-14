package com.uc4.ecc.plugins.actionbuilder.utils;

/**
 * Defines some mandatory information for plug-in
 */
public final class PluginInfo {

	public final static String PLUGIN_ID = "Action.Builder";
	public final static String PLUGIN_INSTANCE_IDENTIFIER = "ActionBuilderPluginInstance";
	public final static String PLUGIN_INTO_PA_IDENTIFIER = "action-packs";

	public final static String VERSION = "1.0.0";

	public static String generateMetadata(String key) {
		return PLUGIN_ID + "." + key;
	}
}
