package com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.build;

import com.automic.apm.models.Pack;
import com.uc4.ecc.framework.commons.translations.CommonTranslationConstants;
import com.uc4.ecc.framework.commons.utils.componentfactories.Labels;
import com.uc4.ecc.framework.core.interfaces.IText;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.IWizardListener;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.IWizardStep;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.Wizard;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.WizardPresenter;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.WizardState;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public final class BuildStep implements IWizardStep {

	private final Pack pack;
	private final BuildWizard wizard;

	protected BuildStep(BuildWizard wizard, Pack pack) {
		this.pack = pack;
		this.wizard = wizard;
	}

	@Override
	public Wizard<? extends WizardPresenter> parent() {
		return this.wizard;
	}

	@Override
	public IWizardStep next() {
		return new ProcessBuild(this.wizard, this.pack);
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
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSizeFull();
		Label firstLine = Labels.create(ActionBuilderMessages.BUILD_PACK_LABEL_INFO1);
		Label firstItem = Labels.HTML.create(ActionBuilderMessages.BUILD_PACK_LABEL_INFO2);
		verticalLayout.addComponents(firstLine, firstItem);
		return verticalLayout;
	}

	@Override
	public IText getStateTitle() {
		return null;
	}

	@Override
	public IText getNextButtonLabel() {
		return CommonTranslationConstants.EXPORT;
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
	public boolean isAlreadyFocus() {
		return false;
	}

}
