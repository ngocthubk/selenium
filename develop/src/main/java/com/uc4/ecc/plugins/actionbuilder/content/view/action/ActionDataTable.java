package com.uc4.ecc.plugins.actionbuilder.content.view.action;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.automic.apm.models.Action;
import com.automic.apm.models.Pack;
import com.uc4.ecc.ae.commons.controls.popup.UC4ObjectViewPopupWrapper;
import com.uc4.ecc.framework.commons.controls.contextmenu.ContextMenu;
import com.uc4.ecc.framework.commons.controls.contextmenu.ContextMenuEntryPopup;
import com.uc4.ecc.framework.commons.controls.contextmenu.IPopupContextMenuEntryListener;
import com.uc4.ecc.framework.commons.controls.datatable.BeanDataTable;
import com.uc4.ecc.framework.commons.css.Icons;
import com.uc4.ecc.framework.commons.translations.CommonTranslationConstants.Actions.ObjectActions;
import com.uc4.ecc.framework.core.interfaces.ContentDisplayMode;
import com.uc4.ecc.framework.core.interfaces.IChildUI;
import com.uc4.ecc.framework.core.interfaces.IContentPresenterProvider;
import com.uc4.ecc.plugins.actionbuilder.content.IconsActionBuilder;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actionbuilder.utils.ActionBuilderConstants;
import com.uc4.ecc.plugins.actioncommon.ICommonView;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.ecc.AWIEvent;
import com.uc4.ecc.plugins.actioncommon.uicore.EventFactory;
import com.uc4.ecc.plugins.actioncommon.uicore.EventType;
import com.uc4.ecc.plugins.actioncommon.uicore.IEvent;
import com.uc4.ecc.plugins.actioncommon.uicore.IEventHandler;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.shared.MouseEventDetails.MouseButton;

public final class ActionDataTable extends BeanDataTable<Action>
		implements ICommonView, IContentPresenterProvider<ActionDataTablePresenter> {

	private final ActionDataTablePresenter presenter;
	private final ContextMenu<Action> menu;
	private final Map<String, String> headerMapping = new LinkedHashMap<String, String>();

	public ActionDataTable(Pack pack, IMasterPresenter masterTabPresenter) {
		super(Action.class);
		this.headerMapping.put(ActionBuilderConstants.PROPS_TITLE, ActionBuilderMessages.LABEL_TABLE_ACTION_TITLE.get());
		this.headerMapping.put(ActionBuilderConstants.PROPS_NAME, ActionBuilderMessages.LABEL_TABLE_ACTION_OBJECT.get());
		this.presenter = new ActionDataTablePresenter(pack, this, masterTabPresenter);
		this.menu = new ContextMenu<Action>();
		this.initUI();
		this.registerEventHandler();
	}

	@Override
	public void initUI() {
		this.refreshRowCache();
		this.setSizeFull();
		this.setImmediate(true);
		this.setColumnReorderingAllowed(true);
		this.setSortEnabled(true);
		this.setSelectable(true);
		this.setNullSelectionAllowed(false);
		this.setVisibleColumns(this.headerMapping.keySet().toArray());
		String[] headers = this.headerMapping.values().toArray(new String[this.headerMapping.size()]);
		this.setColumnHeaders(headers);
		IEventHandler<Action> handler = this.presenter.getMasterPresenter().getController(Action.class);
		IEvent duplicateEvent = EventFactory.initEvent(ActionBuilderMessages.BUTTON_CLONE.get(), Icons.DUPLICATE, EventType.DUPLICATE);
		IEvent jumpEvent = EventFactory
				.initEvent(ActionBuilderMessages.BUTTON_JUMP_TO_SOURCE.get(), Icons.VERTICALNAVIGATION_NEXTPAGE, EventType.SHOW_DETAILS);
		this.menu.addAction(EventFactory.getMenuEntry(IEvent.EDIT, handler));
		this.menu.addAction(
				new ContextMenuEntryPopup<Action>(
						ObjectActions.EDIT_IN_NEW_WINDOW,
						IconsActionBuilder.EDIT_IN_NEW_WINDOW,
						new IPopupContextMenuEntryListener<Action>() {

							@Override
							public void onWindowOpened(IChildUI windowUI, Action contextId) {
								windowUI.setContent(new UC4ObjectViewPopupWrapper(windowUI, windowUI.getPath()));
								AWIEvent.getInstance().openObject(contextId.getName(), ContentDisplayMode.POPUP);
							}

							@Override
							public void onWindowShow(IChildUI windowUI, Action contextId) {
								((UC4ObjectViewPopupWrapper) windowUI.getContent()).onShow();
							}

							@Override
							public void onWindowHide(IChildUI windowUI, Action contextId) {
								((UC4ObjectViewPopupWrapper) windowUI.getContent()).onHide();
							}

							@Override
							public void onWindowClosed(IChildUI windowUI) {
								((UC4ObjectViewPopupWrapper) windowUI.getContent()).onClosed();
							}

						}));
		this.menu.addSeparator();
		this.menu.addAction(EventFactory.getMenuEntry(duplicateEvent, handler));
		this.menu.addAction(EventFactory.getMenuEntry(IEvent.DELETE, handler));
		this.menu.addSeparator();
		this.menu.addAction(EventFactory.getMenuEntry(jumpEvent, handler));
	}

	@Override
	public void registerEventHandler() {
		this.addItemClickListener(new ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				IMasterPresenter masterPresenter = ActionDataTable.this.getPresenter().getMasterPresenter();
				IEventHandler<Action> actionHandler = masterPresenter.getController(Action.class);
				MouseButton button = event.getButton();
				Action action = (Action) event.getItemId();
				Action oldAction = actionHandler.getBean();
				if (event.isDoubleClick() || MouseButton.MIDDLE.equals(button)) {
					masterPresenter.updateState(Action.class, action);
					actionHandler.onEdit();
					masterPresenter.updateState(Action.class, oldAction);
					return;
				}
				if (MouseButton.LEFT.equals(button)) {
					if (!action.equals(oldAction)) {
						masterPresenter.updateState(Action.class, action);
						masterPresenter.refreshToolbar();
					}
					return;
				}
				if (MouseButton.RIGHT.equals(button)) {
					ActionDataTable.this.menu.show(action, event.getClientX(), event.getClientY());
				}
			}
		});
	}

	@Override
	public ActionDataTablePresenter getPresenter() {
		return this.presenter;
	}

	public void bindingData() {
		List<Action> packageActions = this.presenter.list();
		for (Action action : packageActions) {
			this.addRow(action);
		}
		Action currentAction = this.getPresenter().getMasterPresenter().getState(Action.class);
		if (currentAction != null) {
			this.select(currentAction);
		}
	}

	public void update(Pack pack) {
		this.removeAllItems();
		this.presenter.update(pack);
		this.bindingData();
	}
}
