package com.uc4.ecc.plugins.actionbuilder.content;

import com.automic.apm.models.Action;
import com.automic.apm.models.Pack;
import com.uc4.ecc.framework.commons.controls.messageprovider.INotificationMessageCallback;
import com.uc4.ecc.framework.commons.translations.CommonTranslationConstants;
import com.uc4.ecc.plugins.actionbuilder.content.view.action.ActionEventHandler;
import com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.PackageEventHandler;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actionbuilder.utils.ActionBuilderConstants;
import com.uc4.ecc.plugins.actioncommon.CommonMasterPresenter;
import com.uc4.ecc.plugins.actioncommon.IMasterView;
import com.uc4.ecc.plugins.actioncommon.model.ValidationCatalog;
import com.uc4.ecc.plugins.actioncommon.model.ValidationFactory;

final class ActionBuilderMasterPresenter extends CommonMasterPresenter {

	public ActionBuilderMasterPresenter(IMasterView view) {
		super(view);
		this.addHandler(Pack.class, new PackageEventHandler(this));
		this.addHandler(Action.class, new ActionEventHandler(this));
	}

	@Override
	public INotificationMessageCallback callbackIfOutSync() {
		return new INotificationMessageCallback() {

			@Override
			public void retry() {}

			@Override
			public void close() {
				ActionBuilderMasterPresenter.this.refreshSidebar();
				ActionBuilderMasterPresenter.this.navigateTo(Pack.class, null);
			}
		};

	}

	@Override
	public void initValidations() {
		this.initPackageValidator();
		this.initActionValidator();
	}

	private void initPackageValidator() {
		ValidationCatalog.addValidations(
				ValidationFactory.generateValidationKey(Pack.class, ActionBuilderConstants.PROPS_NAME),
				ValidationFactory.getNotNullValidator(CommonTranslationConstants.NAME.get()),
				ValidationCatalog.PACKAGE_MAX_LENGTH_VALIDATOR,
				ValidationCatalog.FOLDER_REGEXP_VALIDATOR);
		ValidationCatalog.addValidations(
				ValidationFactory.generateValidationKey(Pack.class, ActionBuilderConstants.PROPS_TITLE),
				ValidationFactory.getNotNullValidator(CommonTranslationConstants.TITLE.get()),
				ValidationFactory.getMaxLengthValidator(1, 255),
				ValidationFactory.getEmptyValidator(CommonTranslationConstants.TITLE.get()));
	}

	private void initActionValidator() {
		ValidationCatalog.addValidations(
				ValidationFactory.generateValidationKey(Action.class, ActionBuilderConstants.PROPS_NAME),
				ValidationFactory.getNotNullValidator(CommonTranslationConstants.NAME.get()),
				ValidationCatalog.ACTION_MAX_LENGTH_VALIDATOR,
				ValidationCatalog.FOLDER_REGEXP_VALIDATOR);
		ValidationCatalog.addValidations(
				ValidationFactory.generateValidationKey(Action.class, ActionBuilderConstants.PROPS_CATEGORY),
				ValidationFactory.getNotNullValidatorWithMessage(ActionBuilderMessages.CREATE_ACTION_VALID_CATEGORY_EMPTY.get()),
				ValidationFactory.getRegexValidator(
						ActionBuilderConstants.UC4_CATEGORY_REGEX,
						ActionBuilderMessages.CREATE_ACTION_VALID_CATEGORY_REGEX.get()));
		ValidationCatalog.addValidations(
				ValidationFactory.generateValidationKey(Action.class, ActionBuilderConstants.PROPS_TITLE),
				ValidationFactory.getNotNullValidator(CommonTranslationConstants.TITLE.get()),
				ValidationFactory.getMaxLengthValidator(1, 255),
				ValidationFactory.getEmptyValidator(CommonTranslationConstants.TITLE.get()));
	}

}
