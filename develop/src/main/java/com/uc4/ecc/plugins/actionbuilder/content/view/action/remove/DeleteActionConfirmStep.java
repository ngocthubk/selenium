package com.uc4.ecc.plugins.actionbuilder.content.view.action.remove;

import com.uc4.ecc.framework.commons.translations.CommonTranslationConstants;
import com.uc4.ecc.framework.commons.utils.componentfactories.ExtendedLabel;
import com.uc4.ecc.framework.commons.utils.componentfactories.Labels;
import com.uc4.ecc.framework.commons.utils.componentfactories.Layouts;
import com.uc4.ecc.framework.core.interfaces.IText;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.IWizardListener;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.IWizardStep;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.WizardState;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.HorizontalLayout;

public final class DeleteActionConfirmStep implements IWizardStep {

	private final DeleteActionDialog wizard;

	protected DeleteActionConfirmStep(DeleteActionDialog wizard) {
		this.wizard = wizard;
	}

	@Override
	public DeleteActionDialog parent() {
		return this.wizard;
	}

	@Override
	public IWizardStep next() {
		return new ProcessDeleteAction(this.wizard);
	}

	@Override
	public IWizardStep prev() {
		return null;
	}

	@Override
	public IWizardListener getListener() {
		return null;
	}

	@Override
	public WizardState getState() {
		return WizardState.OK;
	}

	@Override
	public AbstractLayout getContent() {
		ExtendedLabel label = Labels.create(this.getStateTitle());
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();
		Layouts.Styles.on(layout).makeContent();
		layout.addComponent(label);
		return layout;
	}

	@Override
	public IText getStateTitle() {
		return ActionBuilderMessages.REMOVE_ACTION_HEADER.format(this.wizard.getPresenter().getBean().getTitle());
	}

	@Override
	public IText getNextButtonLabel() {
		return CommonTranslationConstants.YES;
	}

	@Override
	public IText getPrevButtonLabel() {
		return null;
	}

	@Override
	public IText getCancelButtonLabel() {
		return CommonTranslationConstants.NO;
	}

	@Override
	public boolean isAlreadyFocus() {
		return false;
	}

}
