package com.uc4.ecc.plugins.actionbuilder.content.view.action.create;

import java.util.List;

import com.automic.apm.models.ActionTemplate;
import com.automic.apm.models.Pack;
import com.automic.apm.models.templates.CliActionTemplate;
import com.automic.apm.models.templates.CompositeActionTemplate;
import com.automic.apm.models.templates.RestActionTemplate;
import com.google.common.collect.Lists;
import com.uc4.ecc.framework.commons.layouts.SimpleFormLayout;
import com.uc4.ecc.framework.commons.layouts.SimpleFormLayout.HorizontalLabelAlignment;
import com.uc4.ecc.framework.commons.layouts.SimpleFormLayout.VerticalLabelAlignment;
import com.uc4.ecc.framework.commons.translations.CommonTranslationConstants;
import com.uc4.ecc.framework.commons.utils.componentfactories.ComboBoxes;
import com.uc4.ecc.framework.commons.utils.componentfactories.TextFields;
import com.uc4.ecc.framework.core.interfaces.IText;
import com.uc4.ecc.plugins.actionbuilder.content.view.CustomInputText;
import com.uc4.ecc.plugins.actionbuilder.content.view.GenerateValueListener;
import com.uc4.ecc.plugins.actionbuilder.content.view.action.CategoryPicker;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.DummyStep;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.IWizardListener;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.IWizardStep;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.WizardState;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;

public final class ActionInputStep implements IWizardStep, IWizardListener {

	private final CreateActionWizard wizard;
	private IWizardStep next;
	private WizardState state;

	private ComboBox comboBoxType;
	private TextField titleField;
	private CustomInputText nameField;
	private CategoryPicker categoryPicker;

	protected ActionInputStep(CreateActionWizard wizard, Pack pack) {
		this.wizard = wizard;
		this.next = new DummyStep();
		this.state = WizardState.OK;

		this.comboBoxType = ComboBoxes.Simple.create(ActionTemplate.class);

		ActionTemplate first = null;
		for (ActionTemplate template : this.loadTemplates(pack)) {
			if (first == null) {
				first = template;
			}
			this.comboBoxType.addItem(template);
			this.comboBoxType.setItemCaption(template, template.getTemplateDisplayName());
		}
		this.comboBoxType.select(first);

		this.titleField = TextFields.Default.create();
		this.nameField = new CustomInputText(this.wizard.getPresenter().getPrefix());
		this.categoryPicker = new CategoryPicker(this.parent().getPresenter().getCategoryProvider());
	}

	private List<ActionTemplate> loadTemplates(Pack pack) {
		return Lists.newArrayList(new CliActionTemplate(pack), new RestActionTemplate(pack), new CompositeActionTemplate(pack));
	}

	@Override
	public CreateActionWizard parent() {
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
		this.titleField.addTextChangeListener(new GenerateValueListener(this.nameField));
		this.titleField.focus();
		SimpleFormLayout simpleForm = new SimpleFormLayout(HorizontalLabelAlignment.LEFT);
		simpleForm.setSizeFull();
		simpleForm.addEntry(ActionBuilderMessages.CREATE_ACTION_TYPE, VerticalLabelAlignment.MIDDLE, this.comboBoxType);
		simpleForm.addEntry(CommonTranslationConstants.TITLE, VerticalLabelAlignment.MIDDLE, this.titleField);
		simpleForm.addEntry(ActionBuilderMessages.CREATE_ACTION_LABEL_CATEGORY, VerticalLabelAlignment.MIDDLE, this.categoryPicker);
		simpleForm.addEntry(CommonTranslationConstants.NAME, VerticalLabelAlignment.MIDDLE, this.nameField);
		return simpleForm;
	}

	@Override
	public IText getStateTitle() {
		return null;
	}

	@Override
	public IText getNextButtonLabel() {
		return ActionBuilderMessages.BUTTON_NEXT;
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
		boolean validate = this.parent().getPresenter().validate(this.titleField, this.nameField, this.categoryPicker);
		if (!validate) {
			this.state = WizardState.ERROR;
			return;
		}

		ActionTemplate actionTemplate = (ActionTemplate) this.comboBoxType.getValue();
		actionTemplate.setActionName(this.nameField.getDisplayValue().trim());
		actionTemplate.setActionTitle(this.titleField.getValue().trim());

		actionTemplate.getCategories().clear();
		actionTemplate.addCategory(this.categoryPicker.getValue().trim());
		this.next = RestActionTemplate.class.isInstance(actionTemplate)
				? new ExtraActionInputStep(this, actionTemplate)
				: new ConfirmStep(this, actionTemplate);
		this.state = WizardState.OK;
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

}
