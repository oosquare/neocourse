package io.github.oosquare.neocourse.ui.view.plan;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import lombok.Data;
import lombok.NonNull;

public class PlanAddComponent extends VerticalLayout {

    @FunctionalInterface
    public interface AddEventListener {

        void onAddEvent(@NonNull String planName);
    }

    @Data
    private static class PlanEditModel {

        private String planName = "";
    }

    private final @NonNull TextField planNameField;
    private final @NonNull Button addButton;
    private final @NonNull Button resetButton;

    private final @NonNull Binder<PlanEditModel> binder;

    private final @NonNull AddEventListener addEventListener;

    public PlanAddComponent(@NonNull AddEventListener addEventListener) {
        this.addEventListener = addEventListener;

        this.planNameField = new TextField("Plan Name");

        this.addButton = new Button("Add", this::addButtonOnClick);
        this.addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        this.resetButton = new Button("Reset", this::resetButtonOnClick);

        this.add(
            new HorizontalLayout(this.planNameField),
            new HorizontalLayout(this.addButton, this.resetButton)
        );

        this.binder = new Binder<>();
        this.binder.forField(this.planNameField)
            .asRequired()
            .bind(PlanEditModel::getPlanName, PlanEditModel::setPlanName);
    }

    private void addButtonOnClick(@NonNull ClickEvent<Button> event) {
        try {
            this.addButton.setEnabled(false);

            var model = new PlanEditModel();
            this.binder.writeBean(model);
            this.addEventListener.onAddEvent(model.getPlanName());

            this.resetFields();
        } catch (ValidationException ignored) {
            // Error message is already shown in UI. Nothing needed here.
        } finally {
            this.addButton.setEnabled(true);
        }
    }

    private void resetButtonOnClick(@NonNull ClickEvent<Button> event) {
        this.resetFields();
    }

    private void resetFields() {
        this.planNameField.setValue("");
    }
}