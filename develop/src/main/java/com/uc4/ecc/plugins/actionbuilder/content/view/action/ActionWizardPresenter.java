package com.uc4.ecc.plugins.actionbuilder.content.view.action;

import com.automic.apm.models.Action;
import com.uc4.ecc.plugins.actionbuilder.content.view.CustomInputText;
import com.uc4.ecc.plugins.actionbuilder.content.view.IFolderProvider;
import com.uc4.ecc.plugins.actionbuilder.utils.ActionBuilderConstants;
import com.uc4.ecc.plugins.actioncommon.IMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.model.ValidationCatalog;
import com.uc4.ecc.plugins.actioncommon.model.ValidationExecution;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.Wizard;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.WizardPresenter;
import com.vaadin.ui.TextField;

public abstract class ActionWizardPresenter<V extends Wizard> extends WizardPresenter<Action, V> {

	protected ActionWizardPresenter(Action action, boolean globalMode, V currentView, IMasterPresenter masterPresenter) {
		super(Action.class, action, globalMode, currentView, masterPresenter);
	}

	public abstract String getPrefix();

	public final IFolderProvider getCategoryProvider() {
		return new CategoryPickerProvider();
	}

	public final boolean validate(TextField titleField, CustomInputText nameField, CategoryPicker categoryPicker) {
		String prefix = Action.class.getName().concat(ActionBuilderConstants.HYPHEN);
		String titleKey = prefix.concat(ActionBuilderConstants.PROPS_TITLE);
		String nameKey = prefix.concat(ActionBuilderConstants.PROPS_NAME);
		String categoryKey = prefix.concat(ActionBuilderConstants.PROPS_CATEGORY);
		boolean titleValid = ValidationExecution.validate(titleField, ValidationCatalog.getValidators(titleKey));
		boolean nameValid = nameField.validate(nameKey);
		boolean categoryValid = categoryPicker == null ? true : categoryPicker.validate(categoryKey);
		return titleValid && nameValid && categoryValid;
	}
}
