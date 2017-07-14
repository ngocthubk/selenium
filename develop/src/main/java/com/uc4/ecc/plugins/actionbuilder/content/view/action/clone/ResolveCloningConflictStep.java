package com.uc4.ecc.plugins.actionbuilder.content.view.action.clone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.automic.apm.internal.DependencyItem;
import com.automic.apm.internal.dependencies.PackDependency;
import com.automic.apm.models.Action;
import com.automic.apm.models.ClonePlan;
import com.automic.apm.models.ClonePlan.ClonePlanItem;
import com.automic.apm.models.ConflictItem.ConflictResolution;
import com.automic.apm.models.ConflictItem.ConflictState;
import com.uc4.ecc.framework.commons.utils.componentfactories.ComboBoxes;
import com.uc4.ecc.framework.commons.utils.componentfactories.ExtendedComboBox;
import com.uc4.ecc.framework.commons.utils.componentfactories.ExtendedTextField;
import com.uc4.ecc.framework.commons.utils.componentfactories.Labels;
import com.uc4.ecc.framework.commons.utils.componentfactories.TextFields;
import com.uc4.ecc.framework.core.interfaces.IText;
import com.uc4.ecc.framework.core.text.StaticText;
import com.uc4.ecc.plugins.actionbuilder.i18n.ActionBuilderMessages;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.DummyStep;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.IWizardListener;
import com.uc4.ecc.plugins.actioncommon.uicore.wizard.IWizardStep;
import com.uc4.ecc.plugins.actioncommon.utils.HTMLUtils;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

// TODO duplicate code. Refactor later
public class ResolveCloningConflictStep extends DummyStep implements IWizardListener {

	private final CloneActionWizard wizard;
	private IWizardStep next;
	private final ClonePlan clonePlan;
	private final Action sourceAction;
	private final Action targetAction;
	private final Map<String, List<ObjectProperty<?>>> propsMap = new HashMap<>();
	private final Map<PackDependency, List<ObjectProperty<?>>> dependenciesMap = new HashMap<>();

	public ResolveCloningConflictStep(CloneActionWizard wizard, Action sourceAction, Action targetAction, ClonePlan clonePlan) {
		this.wizard = wizard;
		this.clonePlan = clonePlan;
		this.sourceAction = sourceAction;
		this.targetAction = targetAction;
		this.next = new DummyStep();
	}

	@Override
	public void onNext() {
		for (ClonePlanItem clonePlanItem : this.clonePlan.getItems()) {
			if (ConflictState.CONFLICT != clonePlanItem.getConflictState()) {
				continue;
			}
			List<ObjectProperty<?>> props = this.propsMap.get(clonePlanItem.getTargetName());
			if (props == null || props.isEmpty()) {
				continue;
			}
			ConflictResolution resolution = (ConflictResolution) props.get(2).getValue();
			clonePlanItem.setConflictResolution(resolution);
			if (ConflictResolution.RENAME == resolution) {
				clonePlanItem.setTargetName((String) props.get(1).getValue());
			}
		}
		for (DependencyItem dependencyItem : this.clonePlan.getDependencyItems()) {
			if (ConflictState.CONFLICT != dependencyItem.getConflictState()) {
				continue;
			}
			List<ObjectProperty<?>> props = this.dependenciesMap.get(dependencyItem.getPackDependency());
			if (props == null || props.isEmpty()) {
				continue;
			}
			ConflictResolution resolution = (ConflictResolution) props.get(1).getValue();
			dependencyItem.setConflictResolution(resolution);
		}
		this.next = new ProcessVerifyCloneAction(this.wizard, this.sourceAction, this.targetAction, this.clonePlan);
	}

	@Override
	public void onBack() {}

	@Override
	public void onCancel() {}

	@Override
	public void onInit() {
		this.wizard.setHeightUndefined();
		this.wizard.setWidth(50.0f, Unit.PERCENTAGE);
		this.wizard.center();
		this.wizard.setContentWidth(100.0f, Unit.PERCENTAGE);
	}

	@Override
	public CloneActionWizard parent() {
		return this.wizard;
	}

	@Override
	public IWizardStep next() {
		return this.next;
	}

	@Override
	public IWizardListener getListener() {
		return this;
	}

	@Override
	public AbstractLayout getContent() {
		GridLayout grid = new GridLayout(3, 1);
		grid.setSizeFull();
		grid.setSpacing(true);
		grid.setMargin(true);
		grid.setColumnExpandRatio(0, 0.4f);
		grid.setColumnExpandRatio(1, 0.4f);
		grid.setColumnExpandRatio(2, 0.2f);

		int row = addDescription(grid);
		if (!this.clonePlan.getDependencyItems().isEmpty()) {
			grid.insertRow(row);
			grid.addComponent(Labels.create(ActionBuilderMessages.CLONE_ACTION_RESOLVE_DEPENDENCY_HEADER), 0, row, 2, row);
			row++;
			List<DependencyItem> dependencyItems = this.clonePlan.getDependencyItems();
			for (DependencyItem dependencyItem : dependencyItems) {
				if (ConflictState.CONFLICT != dependencyItem.getConflictState()) {
					continue;
				}
				dependencyItem.setConflictResolution(ConflictResolution.SKIP);
				List<ObjectProperty<?>> props = new ArrayList<>();
				ObjectProperty<String> conflictProp = new ObjectProperty<String>(dependencyItem.getPackDependency().toString());
				ObjectProperty<ConflictResolution> resolutionProp =
						new ObjectProperty<ConflictResolution>(dependencyItem.getConflictResolution());
				props.add(conflictProp);
				props.add(resolutionProp);
				this.dependenciesMap.put(dependencyItem.getPackDependency(), props);
				ExtendedTextField conflictField = TextFields.UpperCase.create().bind(conflictProp);
				conflictField.enable(false);
				ExtendedComboBox<ConflictResolution> resoultion = ComboBoxes.Simple.create(ConflictResolution.class).bind(resolutionProp);
				resoultion.addItems(ConflictResolution.SKIP, ConflictResolution.REMOVE);
				grid.insertRow(row);
				grid.addComponent(conflictField, 0, row);
				grid.addComponent(resoultion, 2, row);
				row++;
			}
		}

		List<ClonePlanItem> items = this.clonePlan.getItems();
		if (!items.isEmpty()) {
			grid.insertRow(row);
			grid.addComponent(Labels.create(ActionBuilderMessages.CLONE_ACTION_RESOLVE_OBJECT_HEADER), 0, row, 2, row);
			row++;
			grid.insertRow(row);
			grid.addComponent(Labels.create(ActionBuilderMessages.CLONE_ACTION_RESOLVE_CONFLICT_OBJECT), 0, row);
			grid.addComponent(Labels.create(ActionBuilderMessages.CLONE_ACTION_RESOLVE_SUGGESTION), 1, row);
			grid.addComponent(Labels.create(ActionBuilderMessages.CLONE_ACTION_RESOLVE_DECISION), 2, row);
			row++;
			for (ClonePlanItem clonePlanItem : items) {
				if (ConflictState.CONFLICT != clonePlanItem.getConflictState()) {
					continue;
				}
				clonePlanItem.setConflictResolution(ConflictResolution.RENAME);
				List<ObjectProperty<?>> props = new ArrayList<>();
				ObjectProperty<String> conflictProp = new ObjectProperty<String>(clonePlanItem.getTargetName());
				ObjectProperty<String> suggestProp = new ObjectProperty<String>(clonePlanItem.getAvailableName());
				ObjectProperty<ConflictResolution> resolutionProp =
						new ObjectProperty<ConflictResolution>(clonePlanItem.getConflictResolution());
				props.add(conflictProp);
				props.add(suggestProp);
				props.add(resolutionProp);
				this.propsMap.put(clonePlanItem.getTargetName(), props);
				ExtendedTextField conflictField = TextFields.UpperCase.create().bind(conflictProp);
				conflictField.setDescription(clonePlanItem.getTargetName());
				conflictField.enable(false);
				final ExtendedTextField suggestionField = TextFields.UpperCase.create().bind(suggestProp);
				suggestionField.setDescription(clonePlanItem.getAvailableName());
				suggestionField.addTextChangeListener(event -> {
					suggestionField.setDescription(event.getText().toUpperCase());
				});
				ExtendedComboBox<ConflictResolution> resoultion = ComboBoxes.Simple.create(ConflictResolution.class).bind(resolutionProp);
				resoultion.addItems(ConflictResolution.RENAME, ConflictResolution.OVERWRITE, ConflictResolution.SKIP);
				resoultion.addValueChangeListener(event -> {
					suggestionField.setEnabled((ConflictResolution) event.getProperty().getValue() == ConflictResolution.RENAME);
				});
				grid.insertRow(row);
				grid.addComponent(conflictField, 0, row);
				grid.addComponent(suggestionField, 1, row);
				grid.addComponent(resoultion, 2, row);
				row++;
			}
		}
		return grid;
	}

	private int addDescription(GridLayout gridLayout) {
		int row = 0;
		gridLayout.addComponent(new Label(HTMLUtils.hr(), ContentMode.HTML), 0, row, 2, row);
		row++;
		
		gridLayout.insertRow(row);
		gridLayout.addComponent(Labels.create(ActionBuilderMessages.CLONE_ACTION_RESOLVE_DESCRIPTION_TOP_HEADER), 0, row, 2, row);
		row++;

		gridLayout.insertRow(row);
		gridLayout.addComponent(Labels.create(ActionBuilderMessages.CLONE_ACTION_RESOLVE_DESCRIPTION_FOLLOWING_OPTIONS), 0, row, 2, row);
		row++;

		gridLayout.insertRow(row);
		gridLayout.addComponent(Labels.HTML.create(HTMLUtils.ol(Arrays.asList(
				ActionBuilderMessages.CLONE_ACTION_RESOLVE_RENAME.get(),
				ActionBuilderMessages.CLONE_ACTION_RESOLVE_OVERWRITE.get(),
				ActionBuilderMessages.CLONE_ACTION_RESOLVE_SKIP.get()), true)), 0, row, 2, row);
		row++;

		gridLayout.insertRow(row);
		gridLayout.addComponent(new Label(HTMLUtils.hr(), ContentMode.HTML), 0, row, 2, row);
		row++;
		
		return row;
	}

	@Override
	public IText getStateTitle() {
		return new StaticText("");
	}

	@Override
	public IText getNextButtonLabel() {
		return ActionBuilderMessages.BUTTON_RESOLVE;
	}

	@Override
	public IText getCancelButtonLabel() {
		return ActionBuilderMessages.BUTTON_ABORT;
	}

}
