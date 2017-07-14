package com.uc4.ecc.plugins.actionbuilder.entrypoint;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import com.uc4.ecc.framework.core.interfaces.IPlugin;
import com.uc4.ecc.framework.core.interfaces.IPluginInstance;
import com.uc4.ecc.framework.core.interfaces.IPluginLifecycle;
import com.uc4.ecc.plugins.actionbuilder.utils.PluginInfo;
import com.uc4.ecc.plugins.actioncommon.utils.LoggingCategory;

/**
 * The Plugin Class. Needed for Plugin auto-discovery - the @Service annotation will result in
 * automatic wiring by OSGI. Developers should implement createPluginInstance() to create plugin
 * instances upon session creation.
 *
 */
@Component
@Service(value = IPlugin.class)
public final class ActionBuilderPlugin implements IPlugin, IPluginLifecycle {

	public ActionBuilderPlugin() {
		LoggingCategory.initialize(PluginInfo.PLUGIN_ID);
	}

	@Override
	public IPluginInstance createPluginInstance() {
		return new ActionBuilderPluginInstance();
	}

	@Override
	public void start() {
		LoggingCategory.getLogger().debug("Action builder plugin start event");
	}

	@Override
	public void stop() {
		LoggingCategory.getLogger().debug("Action builder plugin stop event");
	}

}
