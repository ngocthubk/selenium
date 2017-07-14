package com.uc4.ecc.plugins.actionbuilder.content.view.action;

import java.util.Iterator;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;

public final class TextFieldFinder {

	private final TextField pathField;

	public TextFieldFinder(CssLayout container) {
		this.pathField = this.findTextField(container);
	}

	private TextField findTextField(CssLayout component) {
		Iterator<Component> iterator = component.iterator();
		while (iterator.hasNext()) {
			Component child = iterator.next();
			if (CssLayout.class.isInstance(child)) {
				TextField textField = this.findTextField((CssLayout) child);
				if (textField == null) {
					continue;
				}
				return textField;
			}
			if (TextField.class.isInstance(child)) {
				return (TextField) child;
			}
		}
		return null;
	}

	public String getCurrentValue() {
		return this.pathField.getValue();
	}

	public TextField getField() {
		return this.pathField;
	}
}
