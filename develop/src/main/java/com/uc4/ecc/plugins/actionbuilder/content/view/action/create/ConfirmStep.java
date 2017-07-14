package com.uc4.ecc.plugins.actionbuilder.content.view.action.create;

import com.automic.apm.models.ActionTemplate;
import com.automic.apm.models.NewObjectInfo;
import com.automic.apm.tasks.ScaffoldAction;
import com.google.common.base.Function;
import com.uc4.ecc.framework.commons.translations.CommonTranslationConstants;
import com.uc4.ecc.framework.commons.utils.componentfactories.ExtendedLabel;
import com.uc4.ecc.framework.commons.utils.componentfactories.Labels;
import com.uc4.ecc.framework.core.interfaces.IText;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.apm.EncapsuleTask;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.IWizardStep;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.RealtimeWizardLoading;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.WizardState;
import com.uc4.ecc.plugins.actioncommon.utils.Constants;
import com.uc4.ecc.plugins.actioncommon.utils.HTMLUtils;
import com.uc4.ecc.plugins.actioncommon.utils.UIException;
import com.uc4.ecc.plugins.actioncommon.utils.UIException.UIOutOfSyncException;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public final class ConfirmStep extends RealtimeWizardLoading<CreateActionWizard> implements EncapsuleTask<ScaffoldAction> {

	private final IWizardStep prev;
	private final Label detailContent;
	private final ActionTemplate actionTemplate;

	private WizardState state;

	protected ConfirmStep(IWizardStep prevStep, ActionTemplate actionTemplate) {
		super(prevStep.parent());
		this.prev = prevStep;
		this.actionTemplate = actionTemplate;
		this.detailContent = new Label(Constants.BLANK, ContentMode.HTML);
	}

	@Override
	public IWizardStep next() {
		return new ProcessCreateActionStep(this.wizard, this.actionTemplate);
	}

	@Override
	public IWizardStep prev() {
		return this.prev;
	}

	@Override
	public WizardState getState() {
		return this.state;
	}

	@Override
	public AbstractLayout getContent() {
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		ExtendedLabel label = Labels.create(
				ActionBuilderMessages.CREATE_ACTION_LABEL_INFO1
						.format(this.actionTemplate.getTemplateDisplayName(), this.actionTemplate.getActionTitle()));
		ExtendedLabel label2 = Labels.create(ActionBuilderMessages.CREATE_ACTION_LABEL_INFO2);
		verticalLayout.addComponents(label, this.detailContent, this.loading, label2);
		verticalLayout.setComponentAlignment(this.loading, Alignment.MIDDLE_CENTER);
		return verticalLayout;
	}

	@Override
	public IText getStateTitle() {
		return null;
	}

	@Override
	public IText getNextButtonLabel() {
		return CommonTranslationConstants.ADD;
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
	public boolean isAlreadyFocus() {
		return false;
	}

	@Override
	public ScaffoldAction getTask() {
		return new ScaffoldAction(this.actionTemplate);
	}

	@Override
	public void afterSuccess(ScaffoldAction task) {
		this.state = WizardState.OK;
		this.loading.setVisible(false);
		this.detailContent.setValue(HTMLUtils.ul(task.getNewObjects(), false, new Function<NewObjectInfo, String>() {

			@Override
			public String apply(NewObjectInfo obj) {
				return String.format("%s (%s)", obj.getTargetName(), obj.getObjectType());
			}

		}));
		this.parent().getNextButton().setEnabled(true);
	}

	@Override
	public void afterError(UIException exception) {
		throw exception;
	}

	@Override
	public void onInit() {
		this.parent().getNextButton().setEnabled(false);
		this.loading.setVisible(true);
		this.start();
	}

	@Override
	public void onStart() {
		this.parent().getPresenter().getMasterPresenter().getOperation().validate(this);
	}

	@Override
	public void onError(Exception ex) {
		this.state = WizardState.ERROR;
		if (UIOutOfSyncException.class.isInstance(ex)) {
			this.parent().close();
		} else {
			this.parent().navigateToFirst();
		}
		this.parent().getPresenter().notifyError(ActionBuilderMessages.CREATE_ACTION_TOAST_ERROR.get(), ex);
	}

	@Override
	public void onFinish() {}
}
