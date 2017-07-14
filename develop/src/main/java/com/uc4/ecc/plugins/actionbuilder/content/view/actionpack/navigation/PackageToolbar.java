package com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.navigation;

import com.automic.apm.models.Action;
import com.automic.apm.models.Pack;
import com.google.common.eventbus.Subscribe;
import com.uc4.ecc.framework.commons.controls.collapsible.CollapsibleButtonGroupControl;
import com.uc4.ecc.framework.commons.css.Icons;
import com.uc4.ecc.framework.commons.utils.componentfactories.Buttons;
import com.uc4.ecc.framework.commons.utils.componentfactories.Separators;
import com.uc4.ecc.plugins.actionbuilder.content.IconsActionBuilder;
import com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.build.DownloadEvent;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.uicore.EventFactory;
import com.uc4.ecc.plugins.actioncommon.uicore.EventType;
import com.uc4.ecc.plugins.actioncommon.uicore.IEvent;
import com.uc4.ecc.plugins.actioncommon.uicore.IEventHandler;
import com.uc4.ecc.plugins.actioncommon.uicore.ToolbarPanel;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.JavaScript;

public final class PackageToolbar extends ToolbarPanel<Pack> {

	private final Button buildPackButton;
	private final Button clonePackButton;

	private final Button createActionButton;
	private final Button refreshButton;
	private final Button cloneActionButton;
	private final Button editActionButton;
	private final Button deleteActionButton;
	private Button hiddenExportPackButton;
	
	private static final String HIDDEN_EXPORT_PACK_BUTTON_ID = "hidden-export-pack-button";

	public PackageToolbar(Pack bean, IMasterPresenter masterPresenter) {
		super(Pack.class, bean, masterPresenter);
		IEventHandler<Pack> packHandler = masterPresenter.getController(Pack.class);
		this.buildPackButton = EventFactory.buildToolbarButton(IEvent.EXPORT, packHandler);
		this.clonePackButton = EventFactory.buildToolbarButton(
				EventFactory.initEvent(ActionBuilderMessages.BUTTON_CLONE_PACK.get(), IconsActionBuilder.PACKAGE_ICON, EventType.DUPLICATE),
				packHandler);
		IEventHandler<Action> actionHandler = masterPresenter.getController(Action.class);
		this.createActionButton = EventFactory.buildToolbarButton(
				EventFactory.initEvent(ActionBuilderMessages.BUTTON_CREATE_ACTION.get(), Icons.ADD, EventType.CREATE),
				actionHandler);
		this.editActionButton = EventFactory.buildToolbarButton(IEvent.EDIT, actionHandler);
		this.cloneActionButton = EventFactory.buildToolbarButton(
				EventFactory.initEvent(ActionBuilderMessages.BUTTON_CLONE.get(), Icons.DUPLICATE, EventType.DUPLICATE),
				actionHandler);
		this.deleteActionButton = EventFactory.buildToolbarButton(IEvent.DELETE, actionHandler);

		this.refreshButton = Buttons.Toolbar.create(Icons.REFRESH);
		this.initUI();
		this.registerEventHandler();
	}

	@Override
	public void initUI() {
		super.initUI();
		this.setHeaderString(this.getPresenter().getBean().getName());
		this.addLeftComponent(Separators.Toolbar.create());
		this.addLeftComponent(CollapsibleButtonGroupControl.from(this.createActionButton, this.clonePackButton, this.buildPackButton));
		this.addLeftComponent(Separators.Toolbar.create());
		this.addLeftComponent(CollapsibleButtonGroupControl.from(this.editActionButton, this.cloneActionButton, this.deleteActionButton));
		this.addRightComponent(CollapsibleButtonGroupControl.from(/* this.detailsButton, */ this.refreshButton));
	}

	@Override
	public void registerEventHandler() {
		this.refreshButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				PackageToolbar.this.refresh();
			}
		});
	}

	private void refresh() {
		IMasterPresenter masterTabPresenter = this.getPresenter().getMasterPresenter();
		masterTabPresenter.refreshContent();
	}

	@Override
	public void update(Pack bean) {
		Action currentAction = this.getPresenter().getMasterPresenter().getState(Action.class);
		boolean disabled = currentAction == null;
		this.editActionButton.setEnabled(!disabled);
		this.cloneActionButton.setEnabled(!disabled);
		this.deleteActionButton.setEnabled(!disabled);
	}
	
	@Subscribe
	public void handleDownloadEvent(DownloadEvent event) {
		if (hiddenExportPackButton != null) {
			this.removeComponent(hiddenExportPackButton);
		}
		hiddenExportPackButton = Buttons.Toolbar.create().hiddenPlaceholder();
		addRightComponent(hiddenExportPackButton, 0);
		hiddenExportPackButton.setId(HIDDEN_EXPORT_PACK_BUTTON_ID);
		event.getDownloader().getFileDownloader().extend(hiddenExportPackButton);
		JavaScript.getCurrent().execute("document.getElementById('" + HIDDEN_EXPORT_PACK_BUTTON_ID + "').click()");
	}

}
