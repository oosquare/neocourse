package io.github.oosquare.neocourse.ui.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import lombok.Data;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.query.plan.PlanQueryService;
import io.github.oosquare.neocourse.application.query.plan.PlanSummaryRepresentation;
import io.github.oosquare.neocourse.utility.id.Id;

public class StudentSignUpArea extends VerticalLayout {

    @Data
    public static class StudentSignUpEditModel {

        private String username;
        private String displayedUsername;
        private String password;
        private String planId;
    }

    private class PlanIdConverter implements Converter<PlanSummaryRepresentation, String> {

        @Override
        public Result<String> convertToModel(PlanSummaryRepresentation plan, ValueContext context) {
            return Result.ok(plan.getId());
        }

        @Override
        public PlanSummaryRepresentation convertToPresentation(String planId, ValueContext context) {
            return planQueryService.getPlanByIdInSummaryRepresentation(Id.of(planId));
        }
    }

    @FunctionalInterface
    public interface StudentSignUpListener {

        void signUp(StudentSignUpEditModel model);
    }

    private final @NonNull PlanQueryService planQueryService;

    private final @NonNull StudentSignUpListener studentSignUpListener;

    public StudentSignUpArea(
        @NonNull PlanQueryService planQueryService,
        @NonNull StudentSignUpListener studentSignUpListener
    ) {
        this.planQueryService = planQueryService;
        this.studentSignUpListener = studentSignUpListener;

        var usernameField = new TextField("Username");
        var displayedUsernameField = new TextField("Displayed Username");
        var passwordField = new PasswordField("Password");
        var planCombo = this.createPlanCombo();

        var fieldsLayout = new VerticalLayout(
            usernameField,
            displayedUsernameField,
            passwordField,
            planCombo
        );
        fieldsLayout.setSpacing(false);
        fieldsLayout.setPadding(false);
        fieldsLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        var binder = new Binder<StudentSignUpEditModel>();
        binder.forField(usernameField)
            .asRequired()
            .bind(StudentSignUpEditModel::getUsername, StudentSignUpEditModel::setUsername);
        binder.forField(displayedUsernameField)
            .asRequired()
            .bind(StudentSignUpEditModel::getDisplayedUsername, StudentSignUpEditModel::setDisplayedUsername);
        binder.forField(passwordField)
            .asRequired()
            .bind(StudentSignUpEditModel::getPassword, StudentSignUpEditModel::setPassword);
        binder.forField(planCombo)
            .asRequired()
            .withConverter(new PlanIdConverter())
            .bind(StudentSignUpEditModel::getPlanId, StudentSignUpEditModel::setPlanId);

        var signUpButton = this.createStudentSignUpButton(binder);

        this.add(fieldsLayout, signUpButton);
        this.setPadding(false);
        this.setAlignItems(FlexComponent.Alignment.STRETCH);
    }

    private Button createStudentSignUpButton(Binder<StudentSignUpEditModel> binder) {
        var signUpButton = new Button("Sign up");
        signUpButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        signUpButton.addClickListener(event -> {
            try {
                signUpButton.setEnabled(false);
                var model = new StudentSignUpEditModel();
                binder.writeBean(model);
                this.studentSignUpListener.signUp(model);
            } catch (ValidationException ignored) {
                // Error message is already shown in UI. Nothing needed here.
            } finally {
                signUpButton.setEnabled(true);
            }
        });
        return signUpButton;
    }

    private ComboBox<PlanSummaryRepresentation> createPlanCombo() {
        var plans = this.planQueryService.getAllPlansInSummaryRepresentation();

        var planCombo = new ComboBox<PlanSummaryRepresentation>("Plan");
        planCombo.setItems(plans);
        planCombo.setItemLabelGenerator(PlanSummaryRepresentation::getPlanName);
        return planCombo;
    }
}
