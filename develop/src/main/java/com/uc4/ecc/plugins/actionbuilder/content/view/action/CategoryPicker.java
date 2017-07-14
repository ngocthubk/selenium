package com.uc4.ecc.plugins.actionbuilder.content.view.action;

import org.apache.commons.lang3.StringUtils;

import com.uc4.ecc.framework.commons.utils.componentfactories.TextFields;
import com.uc4.ecc.plugins.actionbuilder.content.view.IFolderProvider;
import com.uc4.ecc.plugins.actioncommon.ICommonView;
import com.uc4.ecc.plugins.actioncommon.model.ValidationCatalog;
import com.uc4.ecc.plugins.actioncommon.model.ValidationExecution;
import com.uc4.webui.vaadin.customwidgets.addressbar.AddressBar;
import com.uc4.webui.vaadin.customwidgets.addressbar.IAddressBarListener;
import com.uc4.webui.vaadin.customwidgets.addressbar.data.IObject;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public final class CategoryPicker extends VerticalLayout implements ICommonView {

	private final TextField categoryField;
	private final AddressBar categoryPicker;
	private final IFolderProvider provider;
	private TextFieldFinder textFieldFinder;
	private boolean programatic = false;

	public CategoryPicker(IFolderProvider provider) {
		this.categoryField = TextFields.Default.create().useUpperCaseConverter();
		this.categoryPicker = new AddressBar();
		this.provider = provider;
		this.initUI();
		this.registerEventHandler();
	}

	@Override
	public void initUI() {
		this.setSizeFull();
		this.categoryField.setVisible(false);
		this.categoryPicker.setVisible(true);
		this.categoryPicker.setImmediate(true);
		this.categoryPicker.setWidth(100, Unit.PERCENTAGE);
		this.categoryPicker.setNavigationButtonsVisible(false);
		this.categoryPicker.setLocateButtonVisible(false);
		this.categoryPicker.setSearchButtonVisible(false);
		this.categoryPicker.setHistoryButtonsVisible(false);
		this.categoryPicker.setTextInputEnabled(true);
		this.categoryPicker.setObjectProvider(this.provider);
		this.categoryPicker.navigateTo(this.provider.getRootObject());
		this.addComponents(this.categoryPicker, this.categoryField);
	}

	@Override
	public void registerEventHandler() {
		this.addLayoutClickListener(this.getLayoutClickListener());
		this.categoryPicker.addAddressBarListener(this.getAddressListener());
		this.categoryField.addTextChangeListener(this.getTextChangeListener());
		this.categoryField.addValueChangeListener(this.getValueChangeListener());
		this.categoryField.addBlurListener(this.getBlurListener());
	}

	private BlurListener getBlurListener() {
		return new BlurListener() {

			@Override
			public void blur(BlurEvent event) {
				CategoryPicker.this.navigateTo(CategoryPicker.this.categoryField.getValue(), false);
			}
		};
	}

	public String getValue() {
		return this.categoryField.getValue();
	}

	public boolean validate(String key) {
		boolean validate = ValidationExecution.validate(this.categoryField, ValidationCatalog.getValidators(key));
		this.navigateTo(this.categoryField.getValue(), true);
		return validate;
	}

	private void navigateTo(String value, boolean validate) {
		if (!validate && (StringUtils.isBlank(value) || "/".equals(value))) {
			this.categoryPicker.navigateTo(this.provider.getRootObject());
			this.updateState(true);
			return;
		}
		IObject child = this.provider.getObjectByPath(null, value);
		if (child == null) {
			this.updateState(false);
			return;
		}
		this.categoryPicker.navigateTo(child);
		this.updateState(true);
	}

	private void updateState(boolean pickerVisible) {
		this.categoryPicker.setVisible(pickerVisible);
		this.categoryField.setVisible(!pickerVisible);
	}

	private ValueChangeListener getValueChangeListener() {
		return new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				String value = (String) event.getProperty().getValue();
				if (CategoryPicker.this.programatic) {
					CategoryPicker.this.programatic = false;
					CategoryPicker.this.navigateTo(value, false);
				}
			}

		};
	}

	private TextChangeListener getTextChangeListener() {
		return new TextChangeListener() {

			@Override
			public void textChange(TextChangeEvent event) {
				CategoryPicker.this.navigateTo(event.getText(), false);
			}
		};
	}

	private IAddressBarListener getAddressListener() {
		return new IAddressBarListener() {

			@Override
			public void searchButtonClicked(IObject currentNavigation) {}

			@Override
			public void navigationChanged(IObject currentNavigation) {
				CategoryPicker.this.categoryField.setValue(CategoryPickerProvider.generateTree(currentNavigation));
			}

			@Override
			public void locateButtonClicked(IObject currentNavigation) {}
		};
	}

	private LayoutClickListener getLayoutClickListener() {
		return new LayoutClickListener() {

			private TextField field;

			@Override
			public void layoutClick(LayoutClickEvent event) {
				if (this.field != null) {
					return;
				}
				CategoryPicker.this.textFieldFinder = new TextFieldFinder(CategoryPicker.this.categoryPicker);
				this.field = CategoryPicker.this.textFieldFinder.getField();
				if (this.field != null) {
					// TextFields.adapt(textField);
					this.field.addTextChangeListener(this.initTextChangeListener());
				}
			}

			private TextChangeListener initTextChangeListener() {
				return new TextChangeListener() {

					@Override
					public void textChange(TextChangeEvent event) {
						CategoryPicker.this.programatic = true;
						CategoryPicker.this.categoryField.setValue(event.getText().toUpperCase());
					}
				};
			}
		};
	}

}
