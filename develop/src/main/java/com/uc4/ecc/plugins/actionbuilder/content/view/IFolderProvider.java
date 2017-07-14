package com.uc4.ecc.plugins.actionbuilder.content.view;

import com.uc4.webui.vaadin.customwidgets.addressbar.data.IObject;
import com.uc4.webui.vaadin.customwidgets.addressbar.data.IObjectProvider;

public interface IFolderProvider extends IObjectProvider {

	/**
	 * Parent can be null in case of using root itself.
	 * 
	 * @param parent
	 * @param path
	 * @return
	 */
	public IObject getObjectByPath(IObject parent, String path);

}
