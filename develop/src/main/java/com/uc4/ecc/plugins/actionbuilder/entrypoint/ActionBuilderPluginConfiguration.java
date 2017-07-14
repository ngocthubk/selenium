package com.uc4.ecc.plugins.actionbuilder.entrypoint;

import com.uc4.webui.common.properties.Configurable;

// see also https://confluence.automic.com/x/SIkPB and https://confluence.automic.com/x/podrAw
public class ActionBuilderPluginConfiguration {

	public final Configurable<String> packageRootPath = new Configurable<>("package_location", "/PACKAGES");

	public final Configurable<String> enableScaffPackage = new Configurable<>("enable_scaff_package", "");

	public final Configurable<String> scaffPackageVersion = new Configurable<>("scaffolding.package_version", "1.0.0");
	public final Configurable<String> scaffPackageCategories = new Configurable<>("scaffolding.package_categories", "");
	public final Configurable<String> scaffPackageCompany = new Configurable<>("scaffolding.package_company", "Custom");
	public final Configurable<String> scaffPackageDependencies = new Configurable<>("scaffolding.package_dependencies", "");
	public final Configurable<String> scaffPackageDescription = new Configurable<>("scaffolding.package_description", "");
	public final Configurable<String> scaffPackageHomePage = new Configurable<>("scaffolding.package_homepage", "www.yourcompany.com");
	public final Configurable<String> scaffPackageTitlePrefix = new Configurable<>("scaffolding.package_title_prefix", "CUSTOM_");
}
