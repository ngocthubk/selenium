package com.uc4.ecc.plugins.actionbuilder.content.view.action.create;

import com.automic.apm.models.ActionTemplate;
import com.automic.apm.models.TemplateProperty;
import com.automic.apm.models.TemplateProperty.SingleChoiceOption;
import com.automic.apm.models.templates.RestActionTemplate;
import com.uc4.ecc.framework.commons.layouts.SimpleFormLayout;
import com.uc4.ecc.framework.commons.layouts.SimpleFormLayout.HorizontalLabelAlignment;
import com.uc4.ecc.framework.commons.layouts.SimpleFormLayout.VerticalLabelAlignment;
import com.uc4.ecc.framework.commons.translations.CommonTranslationConstants;
import com.uc4.ecc.framework.commons.utils.componentfactories.ComboBoxes;
import com.uc4.ecc.framework.core.interfaces.IText;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.DummyStep;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.IWizardListener;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.IWizardStep;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.WizardState;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.ComboBox;

public final class ExtraActionInputStep implements IWizardListener, IWizardStep {

	private final CreateActionWizard parent;
	private final IWizardStep prev;
	private final ActionTemplate actionTemplate;
	private final ComboBox comboBoxAuthType;
	private IWizardStep next;

	protected ExtraActionInputStep(IWizardStep prev, ActionTemplate actionTemplate) {
		this.parent = prev.parent();
		this.next = new DummyStep();
		this.prev = prev;
		this.actionTemplate = actionTemplate;
		this.comboBoxAuthType = ComboBoxes.Simple.create(String.class);

		TemplateProperty.SingleChoice authProperty =
				actionTemplate.getProperty(RestActionTemplate.AUTHENTICATION_MODE_PROPERTY, TemplateProperty.SingleChoice.class);

		this.comboBoxAuthType.setNullSelectionAllowed(false);
		for (SingleChoiceOption opt : authProperty.getOptions()) {
			this.comboBoxAuthType.addItem(opt.getValue());
			this.comboBoxAuthType.setItemCaption(opt.getValue(), opt.getCaption());
			if (opt.isDefault()) {
				this.comboBoxAuthType.select(opt.getValue());
			}
		}
	}

	@Override
	public CreateActionWizard parent() {
		return this.parent;
	}

	@Override
	public IWizardStep next() {
		return this.next;
	}

	@Override
	public IWizardStep prev() {
		return this.prev;
	}

	@Override
	public IWizardListener getListener() {
		return this;
	}

	@Override
	public WizardState getState() {
		return WizardState.OK;
	}

	@Override
	public AbstractLayout getContent() {
		SimpleFormLayout simpleForm = new SimpleFormLayout(HorizontalLabelAlignment.LEFT);
		simpleForm.setSizeFull();
		simpleForm.addHeader(this.getStateTitle());
		simpleForm.addEntry(ActionBuilderMessages.CREATE_ACTION_LABEL_AUTHENTICATION, VerticalLabelAlignment.MIDDLE, this.comboBoxAuthType);
		return simpleForm;
	}

	@Override
	public boolean isAlreadyFocus() {
		return false;
	}

	@Override
	public IText getStateTitle() {
		return ActionBuilderMessages.CREATE_ACTION_LABEL_REST;
	}

	@Override
	public IText getNextButtonLabel() {
		return ActionBuilderMessages.BUTTON_NEXT;
	}

	@Override
	public IText getPrevButtonLabel() {
		return CommonTranslationConstants.BACK;
	}

	@Override
	public IText getCancelButtonLabel() {
		return CommonTranslationConstants.CANCEL;
	}

	@Override
	public void onNext() {
		String authMode = (String) this.comboBoxAuthType.getValue();
		this.actionTemplate.setProperty(RestActionTemplate.AUTHENTICATION_MODE_PROPERTY, authMode);
		this.next = new ConfirmStep(this, this.actionTemplate);
	}

	@Override
	public void onBack() {}

	@Override
	public void onCancel() {}

	@Override
	public void onInit() {}

}
