package com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.create;

import com.automic.apm.models.Pack;
import com.uc4.ecc.framework.commons.layouts.SimpleFormLayout;
import com.uc4.ecc.framework.commons.layouts.SimpleFormLayout.HorizontalLabelAlignment;
import com.uc4.ecc.framework.commons.translations.CommonTranslationConstants;
import com.uc4.ecc.framework.commons.utils.componentfactories.TextFields;
import com.uc4.ecc.framework.core.interfaces.IText;
import com.uc4.ecc.plugins.actionbuilder.content.view.CustomInputText;
import com.uc4.ecc.plugins.actionbuilder.content.view.GenerateValueListener;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actionbuilder.utils.ActionBuilderConstants;
import com.uc4.ecc.plugins.actioncommon.model.ValidationCatalog;
import com.uc4.ecc.plugins.actioncommon.model.ValidationExecution;
import com.uc4.ecc.plugins.actioncommon.model.ValidationFactory;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.IWizardListener;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.IWizardStep;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.WizardState;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.TextField;

public final class CreatePackInputStep implements IWizardStep, IWizardListener {

	private final CreatePackDialog wizard;
	private final TextField titleField;
	private final CustomInputText nameField;
	private ProcessCreatePack next;
	private WizardState state;

	protected CreatePackInputStep(CreatePackDialog wizard) {
		this.wizard = wizard;
		this.titleField = TextFields.Default.create();
		this.nameField = new CustomInputText(this.wizard.getPresenter().getFQNPrefix());
		this.next = new ProcessCreatePack(wizard);
	}

	@Override
	public void onNext() {
		if (this.validate()) {
			this.next.setName(this.nameField.getDisplayValue().trim());
			this.next.setTitle(this.titleField.getValue().trim());
			this.state = WizardState.OK;
		} else {
			this.state = WizardState.ERROR;
		}
	}

	@Override
	public void onBack() {

	}

	@Override
	public void onCancel() {

	}

	@Override
	public void onInit() {

	}

	@Override
	public CreatePackDialog parent() {
		return this.wizard;
	}

	@Override
	public IWizardStep next() {
		return this.next;
	}

	@Override
	public IWizardStep prev() {
		return null;
	}

	@Override
	public IWizardListener getListener() {
		return this;
	}

	@Override
	public WizardState getState() {
		return this.state;
	}

	@Override
	public AbstractLayout getContent() {
		this.titleField.focus();
		this.titleField.setImmediate(true);
		this.titleField.addTextChangeListener(new GenerateValueListener(this.nameField));
		SimpleFormLayout simpleForm = new SimpleFormLayout(HorizontalLabelAlignment.LEFT);
		simpleForm.setSizeFull();
		simpleForm.addHeader(this.getStateTitle());
		simpleForm.addEntry(CommonTranslationConstants.TITLE, SimpleFormLayout.VerticalLabelAlignment.MIDDLE, this.titleField);
		simpleForm.addEntry(CommonTranslationConstants.NAME, SimpleFormLayout.VerticalLabelAlignment.MIDDLE, this.nameField);
		return simpleForm;
	}

	@Override
	public boolean isAlreadyFocus() {
		return true;
	}

	@Override
	public IText getStateTitle() {
		return ActionBuilderMessages.CREATE_PACK_HEADER;
	}

	@Override
	public IText getNextButtonLabel() {
		return ActionBuilderMessages.BUTTON_CREATE_PACK;
	}

	@Override
	public IText getPrevButtonLabel() {
		return null;
	}

	@Override
	public IText getCancelButtonLabel() {
		return CommonTranslationConstants.CANCEL;
	}

	private boolean validate() {
		String titleKey = ValidationFactory.generateValidationKey(Pack.class, ActionBuilderConstants.PROPS_TITLE);
		String nameKey = ValidationFactory.generateValidationKey(Pack.class, ActionBuilderConstants.PROPS_NAME);
		boolean validateTitle = ValidationExecution.validate(this.titleField, ValidationCatalog.getValidators(titleKey));
		boolean validateName = this.nameField.validate(nameKey);
		return validateTitle && validateName;
	}
}
