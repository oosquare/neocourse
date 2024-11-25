package io.github.oosquare.neocourse.ui.view.plan;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import lombok.Data;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.command.plan.AssignRequiredClassPeriodCommand;
import io.github.oosquare.neocourse.application.command.plan.PlanCommandService;
import io.github.oosquare.neocourse.application.query.plan.PlanQueryService;
import io.github.oosquare.neocourse.application.query.plan.PlanRepresentation;
import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountKind;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.utility.id.Id;

public class PlanEditDialog extends Dialog {

    @Data
    private static class ClassPeriodEditModel {

        private Integer classPeriod = 0;
    }

    @FunctionalInterface
    public interface CloseEventListener {

        void onCloseEvent();
    }

    private static final @NonNull Account CURRENT_ACCOUNT = Account.builder()
        .id(Id.of("account0"))
        .kind(AccountKind.ADMINISTRATOR)
        .username(Username.of("test-account"))
        .displayedUsername(DisplayedUsername.of("Test Account"))
        .encodedPassword(EncodedPassword.of("password"))
        .user(Id.of("test-user"))
        .build();

    private final @NonNull PlanCommandService planCommandService;
    private final @NonNull PlanQueryService planQueryService;
    private final @NonNull PlanRepresentation cachedPlan;

    private final @NonNull CloseEventListener closeEventListener;

    private final @NonNull TextField requiredClassPeriodField;
    private final @NonNull Button applyButton;
    private final @NonNull Binder<ClassPeriodEditModel> binder;

    public PlanEditDialog(
        @NonNull PlanCommandService planCommandService,
        @NonNull PlanQueryService planQueryService,
        @NonNull String planId,
        @NonNull CloseEventListener closeEventListener
    ) {
        this.planCommandService = planCommandService;
        this.planQueryService = planQueryService;
        this.closeEventListener = closeEventListener;

        var plan = this.planQueryService.getPlanById(Id.of(planId), this.getCurrentAccount());
        this.cachedPlan = plan;

        this.setHeaderTitle("Edit Plan");

        var idField = new TextField("ID");
        idField.setValue(plan.getId());
        idField.setReadOnly(true);

        var courseNameField = new TextField("Plan Name");
        courseNameField.setValue(plan.getPlanName());
        courseNameField.setReadOnly(true);

        var totalClassPeriodField = new TextField("Total Class Period");
        totalClassPeriodField.setValue(plan.getTotalClassPeriod().toString());
        totalClassPeriodField.setReadOnly(true);

        var requiredClassPeriodField = new TextField("Required Class Period");
        requiredClassPeriodField.setValue(plan.getRequiredClassPeriod().toString());
        this.requiredClassPeriodField = requiredClassPeriodField;

        var leftDialogLayout = createLeftDialogLayout(
            idField,
            courseNameField,
            totalClassPeriodField,
            requiredClassPeriodField
        );

        this.add(leftDialogLayout);

        var cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event -> this.close());
        this.getFooter().add(cancelButton);

        var applyButton = new Button("Apply");
        applyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        applyButton.addClickListener(event -> this.applyButtonOnClickEvent());
        this.getFooter().add(applyButton);
        this.applyButton = applyButton;

        this.binder = createBinder(this.requiredClassPeriodField);
    }

    private static VerticalLayout createLeftDialogLayout(
        TextField idField,
        TextField planNameField,
        TextField totalClassPeriodField,
        TextField requiredClassPeriodField
    ) {
        var layout = new VerticalLayout(
            idField,
            planNameField,
            totalClassPeriodField,
            requiredClassPeriodField
        );
        layout.setPadding(false);
        layout.setPadding(false);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        layout.getStyle().set("width", "18rem").set("max-width", "100%");
        return layout;
    }

    private static Binder<ClassPeriodEditModel> createBinder(TextField requiredClassPeriodField) {
        var binder = new Binder<ClassPeriodEditModel>();
        binder.forField(requiredClassPeriodField)
            .asRequired()
            .withConverter(new StringToIntegerConverter("Class period is not a number"))
            .withValidator(new IntegerRangeValidator("Class period should be positive", 1, null))
            .bind(ClassPeriodEditModel::getClassPeriod, ClassPeriodEditModel::setClassPeriod);
        return binder;
    }

    private void applyButtonOnClickEvent() {
        try {
            this.applyButton.setEnabled(false);

            var model = new ClassPeriodEditModel();
            this.binder.writeBean(model);

            this.modifyRequiredClassPeriod(model);

            this.close();
            this.closeEventListener.onCloseEvent();
        } catch (ValidationException ignored) {
            // Error message is already shown in UI. Nothing needed here.
        } finally {
            this.applyButton.setEnabled(true);
        }
    }

    private void modifyRequiredClassPeriod(ClassPeriodEditModel model) {
        if (model.getClassPeriod().equals(this.cachedPlan.getRequiredClassPeriod())) {
            return;
        }
        var command = AssignRequiredClassPeriodCommand.builder()
            .planId(Id.of(this.cachedPlan.getId()))
            .requiredClassPeriod(ClassPeriod.of(model.getClassPeriod()))
            .build();
        var account = this.getCurrentAccount();
        this.planCommandService.assignRequiredClassPeriod(command, account);
    }

    private Account getCurrentAccount() {
        return CURRENT_ACCOUNT;
    }
}
