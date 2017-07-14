package com.uc4.ecc.plugins.actionbuilder.content.view.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.uc4.api.objects.IFolder;
import com.uc4.ecc.backends.dataservice.folder.IFolderService;
import com.uc4.ecc.framework.commons.css.Icons;
import com.uc4.ecc.framework.core.context.Context;
import com.uc4.ecc.framework.core.interfaces.IIcon;
import com.uc4.ecc.plugins.actionbuilder.content.view.IFolderProvider;
import com.uc4.ecc.plugins.actioncommon.apm.APMFactory;
import com.uc4.webui.vaadin.customwidgets.addressbar.data.IObject;

public final class CategoryPickerProvider implements IFolderProvider {

	private final IObject packRoot;
	private IFolderService service;

	public CategoryPickerProvider() {
		String path = APMFactory.getAPMOperation().getPackClientSettings().getActionPath();
		Context.injectInto(this);
		IFolder folder = this.service.getFolder(path, false);
		if (folder == null) {
			this.packRoot = new FolderTreeObject(null, null);
		} else {
			this.packRoot = new FolderTreeObject(folder, folder);
		}
	}

	@Inject
	public void setFolderSerivce(IFolderService service) {
		this.service = service;
	}

	@Override
	public IObject getRootObject() {
		return this.packRoot;
	}

	@Override
	public IObject getObjectByPath(IObject parent, String path) {
		List<? extends IObject> children;
		if (parent == null) {
			children = this.getRootObject().getChildren();
		} else {
			children = parent.getChildren();
		}
		String[] extractPath = this.extractPath(path);
		String parentName = extractPath[0];
		String childPath = extractPath[1];
		for (IObject iObject : children) {
			if (iObject.getName().equals(parentName)) {
				if (StringUtils.isBlank(childPath)) {
					return iObject;
				}
				IObject child = this.getObjectByPath(iObject, childPath);
				if (child == null) {
					continue;
				}
				return child;
			}
		}
		return null;
	}

	private String[] extractPath(String path) {
		int index = path.indexOf("/");
		if (index == -1) {
			return new String[] {path, ""};
		}
		if (index == 0) {
			int nextIndex = path.indexOf("/", index + 1);
			String parent = path.substring(index + 1, nextIndex == -1 ? path.length() : nextIndex);
			String child = nextIndex == -1 ? "" : path.substring(nextIndex, path.length());
			return new String[] {parent, child};
		}

		String parent = path.substring(0, index);
		String child = path.substring(index + 1, path.length());
		return new String[] {parent, child};
	}

	public static String generateTree(IObject folderTree) {
		StringBuilder builder = new StringBuilder();
		IObject obj = folderTree;
		while (obj.getParent() != null) {
			builder.insert(0, "/" + obj.getName());
			obj = obj.getParent();
		}
		return builder.toString();
	}

	private class FolderTreeObject implements IObject {

		private final IFolder folder;
		private final IFolder rootFolder;

		private FolderTreeObject(IFolder folder, IFolder rootFolder) {
			this.folder = folder;
			this.rootFolder = rootFolder;
		}

		@Override
		public String getId() {
			return this.folder.getId();
		}

		@Override
		public String getName() {
			return this.isRoot() ? "" : this.folder.getName();
		}

		@Override
		public String getTitle() {
			return this.isRoot() ? "" : this.folder.getTitle();
		}

		@Override
		public IIcon getIcon() {
			return this.isRoot() ? Icons.HOME : Icons.FOLDER_CLOSED;
		}

		@Override
		public boolean isRoot() {
			return this.folder == null || this.folder.equals(this.rootFolder) || this.folder.getParent() == null;
		}

		@Override
		public IObject getParent() {
			if (this.isRoot()) {
				return null;
			}
			return new FolderTreeObject(this.folder.getParent(), this.rootFolder);
		}

		@Override
		public boolean hasChildren() {
			return this.folder != null && this.folder.subfolder() != null && this.folder.subfolder().hasNext();
		}

		/**
		 * getChildren involves transforming the subfolder list. list is copied here, so the
		 * elements are non-lazy, but the conversion from ifolder to object is lazy (non-sideeffect)
		 */
		@Override
		public List<? extends IObject> getChildren() {
			if (!this.hasChildren()) {
				return new ArrayList<>();
			}
			Iterator<IFolder> subfolders = this.folder.subfolder();
			return Lists.transform(ImmutableList.copyOf(subfolders), new Function<IFolder, FolderTreeObject>() {

				@Override
				public FolderTreeObject apply(IFolder input) {
					return new FolderTreeObject(input, FolderTreeObject.this.rootFolder);
				}
			});
		}

		@Override
		public String toString() {
			return "FolderTreeObject [Id: " + this.getId() + ", Name: " + this.getName() + ", Root: " + this.isRoot() + "]";
		}

		@Override
		public List<? extends IObject> getChildrenExcluding(String... arg0) {
			return new ArrayList<>();
		}

	}

}
