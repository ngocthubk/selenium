package com.uc4.ecc.plugins.actionbuilder.content.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.uc4.ecc.plugins.actionbuilder.utils.ActionBuilderConstants;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;

public final class GenerateValueListener implements TextChangeListener, ValueChangeListener {

	private final CustomInputText customInput;
	private String previousName;

	public GenerateValueListener(CustomInputText customInput) {
		this.customInput = customInput;
	}

	@Override
	public void textChange(TextChangeEvent event) {
		this.processGenerate(event.getText());
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		this.processGenerate((String) event.getProperty().getValue());
	}

	private void processGenerate(String originValue) {
		String oldGenerateValue = this.customInput.getRawValue();
		String newGenerateValue = this.generateValue(originValue.trim());
		if (StringUtils.isBlank(oldGenerateValue) || oldGenerateValue.equals(this.previousName)) {
			this.customInput.updateValue(newGenerateValue);
			this.customInput.visible();
		}
		this.previousName = newGenerateValue;
	}

	private String generateValue(String originValue) {
		Pattern p = Pattern.compile(ActionBuilderConstants.UC4_FOLDER_INVALID_CHAR_REGEX);
		Matcher m = p.matcher(originValue);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, ActionBuilderConstants.UNDERSCORE);
		}
		m.appendTail(sb);
		return sb.toString().toUpperCase();
	}

}
