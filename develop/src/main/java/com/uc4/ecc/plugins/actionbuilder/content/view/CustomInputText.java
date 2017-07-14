package com.uc4.ecc.plugins.actionbuilder.content.view;

import org.apache.commons.lang3.StringUtils;

import com.uc4.ecc.framework.commons.utils.componentfactories.TextFields;
import com.uc4.ecc.plugins.actioncommon.ICommonView;
import com.uc4.ecc.plugins.actioncommon.model.ValidationCatalog;
import com.uc4.ecc.plugins.actioncommon.model.ValidationExecution;
import com.uc4.ecc.plugins.actioncommon.utils.Constants;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public final class CustomInputText extends VerticalLayout implements ICommonView {

	private String prefix;
	private final TextField displayField;
	private final TextField editField;

	public CustomInputText(String readOnlyText) {
		this.prefix = readOnlyText;
		this.displayField = TextFields.Default.create().upperCase(true, true);
		this.editField = TextFields.Default.create().upperCase(true, true);
		this.initUI();
		this.registerEventHandler();
	}

	@Override
	public void initUI() {
		this.displayField.setVisible(false);
		this.displayField.setImmediate(true);
		this.editField.setImmediate(true);
		this.addComponent(this.displayField);
		this.addComponent(this.editField);
		this.setSizeFull();
	}

	@Override
	public void registerEventHandler() {
		this.displayField.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				CustomInputText.this.updateDisplayFieldVisible(false);
				CustomInputText.this.editField.focus();
			}
		});
		this.editField.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {
				CustomInputText.this.updateDisplayFieldVisible(true);
			}
		});
		this.editField.addTextChangeListener(new TextChangeListener() {

			@Override
			public void textChange(TextChangeEvent event) {
				String text = event.getText();
				String newValue = StringUtils.isBlank(text) ? Constants.BLANK : CustomInputText.this.prefix + text;
				CustomInputText.this.displayField.setValue(newValue);
			}
		});
	}

	public void updatePrefix(String prefix) {
		this.prefix = prefix;
		this.updateValue(this.editField.getValue());
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void visible() {
		this.updateDisplayFieldVisible(true);
	}

	public boolean validate(String key) {
		ValidationExecution.validate(this.editField, ValidationCatalog.getValidators(key));
		return ValidationExecution.validate(this.displayField, ValidationCatalog.getValidators(key));
	}

	public String getDisplayValue() {
		return this.displayField.getValue();
	}

	public String getRawValue() {
		return this.editField.getValue();
	}

	public void updateValue(String newValue) {
		String rawValue = newValue;
		while (rawValue.indexOf(this.prefix) != -1) {
			rawValue = rawValue.replace(this.prefix, Constants.BLANK);
		}
		String displayValue = StringUtils.isBlank(rawValue) ? Constants.BLANK : this.prefix + rawValue;
		this.displayField.setValue(displayValue);
		this.editField.setValue(rawValue);
	}

	private void updateDisplayFieldVisible(boolean visible) {
		this.displayField.setVisible(visible);
		this.editField.setVisible(!visible);
	}

}
