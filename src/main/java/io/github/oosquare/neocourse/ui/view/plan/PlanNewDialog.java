package io.github.oosquare.neocourse.ui.view.plan;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import lombok.Data;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.command.plan.AddPlanCommand;
import io.github.oosquare.neocourse.application.command.plan.PlanCommandService;
import io.github.oosquare.neocourse.application.security.CurrentAccountAwareSupport;
import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountKind;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;
import io.github.oosquare.neocourse.utility.id.Id;

public class PlanNewDialog extends Dialog
    implements CurrentAccountAwareSupport {

    @Data
    private static class PlanEditModel {

        private String planName = "";
    }

    @FunctionalInterface
    public interface CloseEventListener {

        void onCloseEvent();
    }

    private final @NonNull PlanCommandService planCommandService;

    private final @NonNull CloseEventListener closeEventListener;

    private final @NonNull Button addButton;
    private final @NonNull Binder<PlanEditModel> binder;

    public PlanNewDialog(
        @NonNull PlanCommandService planCommandService,
        @NonNull CloseEventListener closeEventListener
    ) {
        this.planCommandService = planCommandService;
        this.closeEventListener = closeEventListener;

        this.setHeaderTitle("New Plan");

        var planNameField = new TextField("Plan Name");

        var dialogLayout = createDialogLayout(planNameField);
        this.add(dialogLayout);

        var cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event -> this.close());
        this.getFooter().add(cancelButton);

        var addButton = new Button("Add");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(event -> this.addButtonOnClickEvent());
        this.getFooter().add(addButton);
        this.addButton = addButton;

        this.binder = createBinder(planNameField);
    }

    private static VerticalLayout createDialogLayout(TextField planNameField) {
        var layout = new VerticalLayout(planNameField);
        layout.setPadding(false);
        layout.setPadding(false);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        layout.getStyle().set("width", "18rem").set("max-width", "100%");
        return layout;
    }

    private static Binder<PlanEditModel> createBinder(TextField planNameField) {
        var binder = new Binder<PlanEditModel>();
        binder.forField(planNameField)
            .asRequired()
            .bind(PlanEditModel::getPlanName, PlanEditModel::setPlanName);
        return binder;
    }

    private void addButtonOnClickEvent() {
        try {
            this.addButton.setEnabled(false);

            var model = new PlanEditModel();
            this.binder.writeBean(model);

            var command = AddPlanCommand.builder()
                .planName(PlanName.of(model.getPlanName()))
                .build();
            var account = this.getCurrentAccount();
            this.planCommandService.addPlan(command, account);

            this.close();
            this.closeEventListener.onCloseEvent();
        } catch (ValidationException ignored) {
            // Error message is already shown in UI. Nothing needed here.
        } finally {
            this.addButton.setEnabled(true);
        }
    }
}
