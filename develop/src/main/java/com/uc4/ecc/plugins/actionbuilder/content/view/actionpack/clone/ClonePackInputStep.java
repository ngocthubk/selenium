package com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.clone;

import com.automic.apm.models.Pack;
import com.uc4.ecc.framework.commons.layouts.SimpleFormLayout;
import com.uc4.ecc.framework.commons.layouts.SimpleFormLayout.HorizontalLabelAlignment;
import com.uc4.ecc.framework.commons.layouts.SimpleFormLayout.VerticalLabelAlignment;
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

public final class ClonePackInputStep implements IWizardStep, IWizardListener {

	private final ClonePackWizard wizard;
	private final Pack originPack;
	private final ProcessClonePack next;
	private WizardState state;

	private TextField titleField;
	private CustomInputText nameField;

	protected ClonePackInputStep(ClonePackWizard wizard, Pack originPack) {
		this.wizard = wizard;
		this.originPack = originPack;
		this.next = new ProcessClonePack(this.wizard, originPack);
		this.state = WizardState.OK;
		this.titleField = TextFields.Default.create();
		this.nameField = new CustomInputText(this.wizard.getPresenter().getFQNPrefix());
	}

	@Override
	public ClonePackWizard parent() {
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
		GenerateValueListener listener = new GenerateValueListener(this.nameField);
		this.titleField.addTextChangeListener(listener);
		this.titleField.addValueChangeListener(listener);
		this.titleField.setValue(this.originPack.getTitle());
		this.titleField.focus();
		SimpleFormLayout simpleForm = new SimpleFormLayout(HorizontalLabelAlignment.LEFT);
		simpleForm.setSizeFull();
		simpleForm.addEntry(CommonTranslationConstants.TITLE, VerticalLabelAlignment.MIDDLE, this.titleField);
		simpleForm.addEntry(CommonTranslationConstants.NAME, VerticalLabelAlignment.MIDDLE, this.nameField);
		return simpleForm;
	}

	@Override
	public IText getStateTitle() {
		return ActionBuilderMessages.CLONE_PACK_HEADER;
	}

	@Override
	public IText getNextButtonLabel() {
		return ActionBuilderMessages.BUTTON_CLONE;
	}

	@Override
	public IText getPrevButtonLabel() {
		return null;
	}

	@Override
	public IText getCancelButtonLabel() {
		return CommonTranslationConstants.CANCEL;
	}

	@Override
	public void onNext() {
		if (this.validate()) {
			this.next.setClonedPackName(this.nameField.getDisplayValue().trim());
			this.next.setClonedPackTitle(this.titleField.getValue().trim());
			this.state = WizardState.OK;
		} else {
			this.state = WizardState.ERROR;
		}
	}

	@Override
	public void onBack() {}

	@Override
	public void onCancel() {}

	@Override
	public void onInit() {}

	@Override
	public boolean isAlreadyFocus() {
		return true;
	}

	private boolean validate() {
		String titleKey = ValidationFactory.generateValidationKey(Pack.class, ActionBuilderConstants.PROPS_TITLE);
		String nameKey = ValidationFactory.generateValidationKey(Pack.class, ActionBuilderConstants.PROPS_NAME);
		boolean validateTitle = ValidationExecution.validate(this.titleField, ValidationCatalog.getValidators(titleKey));
		boolean validateName = this.nameField.validate(nameKey);
		return validateTitle && validateName;
	}
}
