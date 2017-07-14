package com.uc4.ecc.plugins.actionbuilder.content.view.action.clone;

import java.util.Collection;

import com.automic.apm.models.Action;
import com.automic.apm.models.Pack;
import com.uc4.ecc.framework.commons.data.IValueProvider;
import com.uc4.ecc.framework.commons.layouts.SimpleFormLayout;
import com.uc4.ecc.framework.commons.layouts.SimpleFormLayout.HorizontalLabelAlignment;
import com.uc4.ecc.framework.commons.layouts.SimpleFormLayout.VerticalLabelAlignment;
import com.uc4.ecc.framework.commons.translations.CommonTranslationConstants;
import com.uc4.ecc.framework.commons.utils.componentfactories.ComboBoxes;
import com.uc4.ecc.framework.commons.utils.componentfactories.ExtendedComboBox;
import com.uc4.ecc.framework.commons.utils.componentfactories.TextFields;
import com.uc4.ecc.framework.core.interfaces.IText;
import com.uc4.ecc.plugins.actionbuilder.content.view.CustomInputText;
import com.uc4.ecc.plugins.actionbuilder.content.view.GenerateValueListener;
import com.uc4.ecc.plugins.actionbuilder.content.view.ListPackDelegate;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.apm.IAPMOperation;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.DummyStep;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.IWizardListener;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.IWizardStep;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.WizardState;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public final class CloneActionInputStep implements IWizardStep, IWizardListener {

	private final CloneActionWizard wizard;
	private final Action action;
	private IWizardStep next;
	private WizardState state;

	private TextField titleField;
	private ExtendedComboBox<Pack> packCombobox;
	private CustomInputText nameField;

	protected CloneActionInputStep(CloneActionWizard wizard, Action action) {
		this.wizard = wizard;
		this.action = action;
		this.next = new DummyStep();
		this.state = WizardState.OK;
		this.titleField = TextFields.Default.create();
		this.packCombobox = ComboBoxes.Searchable.create();
		this.packCombobox.addItem(action.getPack());
		this.packCombobox.select(action.getPack());
		this.nameField = new CustomInputText(this.wizard.getPresenter().getPrefix());
	}

	@Override
	public CloneActionWizard parent() {
		return this.wizard;
	}

	@Override
	public IWizardStep next() {
		return next;
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
		this.titleField.setValue(this.action.getTitle());
		this.titleField.focus();
		ListPackDelegate delegate = new ListPackDelegate();
		IAPMOperation operation = this.wizard.getPresenter().getMasterPresenter().getOperation();
		this.packCombobox.setLazyLoadValueProvider(new IValueProvider<Pack>() {

			@Override
			public Collection<Pack> getValues() {
				return delegate.execute(operation);
			}
		});
		this.packCombobox.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				Pack pack = (Pack) event.getProperty().getValue();
				nameField.updatePrefix(pack.getActionPrefix());
			}
		});
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		SimpleFormLayout simpleForm = new SimpleFormLayout(HorizontalLabelAlignment.LEFT);
		simpleForm.addEntry(ActionBuilderMessages.LABEL_PACKAGE, VerticalLabelAlignment.MIDDLE, this.packCombobox);
		simpleForm.addEntry(CommonTranslationConstants.TITLE, VerticalLabelAlignment.MIDDLE, this.titleField);
		simpleForm.addEntry(CommonTranslationConstants.NAME, VerticalLabelAlignment.MIDDLE, this.nameField);
		simpleForm.setSizeFull();
		layout.addComponent(simpleForm);
		return layout;
	}

	@Override
	public IText getStateTitle() {
		return ActionBuilderMessages.CREATE_ACTION_HEADER;
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
		boolean validate = this.parent().getPresenter().validate(this.titleField, this.nameField, null);
		if (validate) {
			this.state = WizardState.OK;
			Action cloneAction =
					new Action(this.packCombobox.getValue(), this.nameField.getDisplayValue().trim(), this.titleField.getValue().trim());
			for (String category : this.action.getCategories()) {
				cloneAction.addCategory(category);
			}
			this.next = new ProcessVerifyCloneAction(this.wizard, this.action, cloneAction);
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

}
