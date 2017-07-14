package com.uc4.ecc.plugins.actionbuilder.entrypoint;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.automic.apm.ValidationError;
import com.automic.apm.exceptions.ApmException;
import com.automic.apm.internal.PropertySerializers;
import com.automic.apm.models.Version;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.uc4.ecc.backends.dataservice.folder.IFolderService;
import com.uc4.ecc.backends.dataservice.request.IRequestService;
import com.uc4.ecc.backends.impl.dataservice.folder.IFolderTreeCache;
import com.uc4.ecc.framework.commons.utils.ConfigurationHelper;
import com.uc4.ecc.framework.core.context.Context;
import com.uc4.ecc.framework.core.interfaces.IPerspective;
import com.uc4.ecc.framework.core.interfaces.IPluginInstance;
import com.uc4.ecc.framework.core.interfaces.IPluginInstanceLifecycle;
import com.uc4.ecc.plugins.actionbuilder.utils.PluginInfo;
import com.uc4.ecc.plugins.actioncommon.apm.APMFactory;
import com.uc4.ecc.plugins.actioncommon.utils.LoggingCategory;
import com.uc4.webui.common.identifiable.IIdentifiable;
import com.uc4.webui.common.properties.PropertyHelper;

/**
 * Plugin Instance. Manage the creation of the Perspectives here.
 */
public final class ActionBuilderPluginInstance implements IPluginInstance, IPluginInstanceLifecycle, IIdentifiable {

	private static final String CONFIG_FILE_NAME = "actionbuilder.properties.sample";
	private static final String SAMPLE_EXTENSION = ".sample";

	public static ActionBuilderPluginConfiguration pluginConfig;
	private IRequestService requestService;
	private IFolderService folderService;
	private IFolderTreeCache folderTreeCache;

	@Inject
	public void setRequestService(IRequestService requestService) {
		this.requestService = requestService;
	}

	@Inject
	public void setFolderService(IFolderService folderService) {
		this.folderService = folderService;
	}

	@Inject
	public void setFolderTreeCache(IFolderTreeCache folderTreeCache) {
		this.folderTreeCache = folderTreeCache;
	}

	@Override
	public void start() {
		LoggingCategory.getLogger().debug("Action Builder Plugin Instance start event");
		Context.injectInto(this);
		pluginConfig = this.loadPluginProperties();
		optimizePluginConfigs();
		APMFactory.initializeAPM(this.requestService, this.folderService, this.folderTreeCache, pluginConfig.packageRootPath.getValue());
	}

	@Override
	public void stop() {
		LoggingCategory.getLogger().debug("Action Builder Plugin Instance stop event");
	}

	@Override
	public Collection<IPerspective> getPerspectives() {
		return new ArrayList<IPerspective>();
	}

	@Override
	public String getIdentifier() {
		return PluginInfo.PLUGIN_INSTANCE_IDENTIFIER;
	}

	private ActionBuilderPluginConfiguration loadPluginProperties() {
		try {
			ConfigurationHelper configurationHelper = new ConfigurationHelper(ActionBuilderPlugin.class);
			return PropertyHelper.createConfiguration(
					ActionBuilderPluginConfiguration.class,
					this.createWorkingFileFromSample(configurationHelper.getResource(CONFIG_FILE_NAME)));
		} catch (Exception ex) {
			LoggingCategory.getLogger().error("Failure when loading plugin config", ex);
			return new ActionBuilderPluginConfiguration();
		}
	}

	private URL createWorkingFileFromSample(URL sampleUrl) {
		try {
			if (!sampleUrl.getPath().contains(SAMPLE_EXTENSION)) {
				return sampleUrl;
			}
			String workingPath = URLDecoder.decode(
					sampleUrl.getPath().substring(0, sampleUrl.getPath().indexOf(SAMPLE_EXTENSION)),
					StandardCharsets.UTF_8.name());
			File workingFile = new File(workingPath);
			if (!workingFile.exists()) {
				FileUtils.copyFile(FileUtils.toFile(sampleUrl), workingFile);
			}
			return new File(workingPath).toURI().toURL();
		} catch (Exception ex) {
			LoggingCategory.getLogger().error("Failure when creating config file", ex);
			return sampleUrl;
		}
	}

	/**
	 * Check validation of static variable pluginConfig. To avoid some config invalided
	 */
	private void optimizePluginConfigs() {
		// pack version config
		if (StringUtils.isBlank(pluginConfig.scaffPackageVersion.value())) {
			// Fall back to default value
			pluginConfig.scaffPackageVersion.setLoadedValue(null);
			LoggingCategory.getLogger().warn(
					String.format(
							"actionbuilder.properties contains no value for scaffolding.package_version - defaulting to '%s'.",
							pluginConfig.scaffPackageVersion.value()));
		}
		try {
			Version.parseVersion(pluginConfig.scaffPackageVersion.value(), true);
		} catch (ApmException ex) {
			// Fall back to default value
			pluginConfig.scaffPackageVersion.setLoadedValue(null);
			LoggingCategory.getLogger().warn(
					String.format(
							"actionbuilder.properties contains a malformed version number string for scaffolding.package_version - "
									+ "falling back to '%s'.",
							pluginConfig.scaffPackageVersion.value()));
		}
		// pack dependencies config
		String[] allDependencies = pluginConfig.scaffPackageDependencies.value().split(",");
		List<String> invalidDependencies = Lists.newArrayList();
		List<String> validDependencies = Lists.newArrayList();
		for (String dependency : allDependencies) {
			if (!validateSingleDependency(dependency)) {
				invalidDependencies.add(dependency);
			} else {
				validDependencies.add(dependency);
			}
		}
		if (invalidDependencies.size() > 0) {
			LoggingCategory.getLogger().warn(
					String.format(
							"actionbuilder.properties contains a malformed string for scaffolding.package_dependencies - "
									+ "removing the following string(s) '%s'.",
							Joiner.on(",").skipNulls().join(invalidDependencies)));
			pluginConfig.scaffPackageDependencies.setLoadedValue(Joiner.on(",").skipNulls().join(validDependencies));
		}
	}

	private boolean validateSingleDependency(String singleDependency) {
		try {
			List<ValidationError> error = Lists.newArrayList();
			PropertySerializers.DEPENDENCIES.deserialize(singleDependency, error, null);
			if (error.size() > 0) {
				return false;
			}
		} catch (Exception ex) {
			return false;
		}
		return true;
	}
}
