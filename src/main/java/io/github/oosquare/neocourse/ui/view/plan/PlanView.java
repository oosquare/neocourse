package io.github.oosquare.neocourse.ui.view.plan;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.command.plan.AddPlanCommand;
import io.github.oosquare.neocourse.application.command.plan.PlanCommandService;
import io.github.oosquare.neocourse.application.query.plan.PlanQueryService;
import io.github.oosquare.neocourse.application.query.plan.PlanRepresentation;
import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountKind;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;
import io.github.oosquare.neocourse.ui.layout.MainLayout;
import io.github.oosquare.neocourse.utility.id.Id;

@Route(value = "plans", layout = MainLayout.class)
public class PlanView extends VerticalLayout {

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

    private final @NonNull Grid<PlanRepresentation> planGrid;
    private final @NonNull PlanAddComponent planAddComponent;

    public PlanView(
        @NonNull PlanCommandService planCommandService,
        @NonNull PlanQueryService planQueryService
    ) {
        this.planCommandService = planCommandService;
        this.planQueryService = planQueryService;

        this.planGrid = new Grid<>(PlanRepresentation.class, false);
        this.planGrid.addColumn(PlanRepresentation::getId).setHeader("ID");
        this.planGrid.addColumn(PlanRepresentation::getName).setHeader("Plan Name");
        this.planGrid.addColumn(PlanRepresentation::getTotalClassPeriod).setHeader("Total Class Period");
        this.planGrid.addColumn(PlanRepresentation::getRequiredClassPeriod).setHeader("Required Class Period");
        this.planGrid.setItems(this.planQueryService.getAllPlans(CURRENT_ACCOUNT));
        this.planGrid.setSizeFull();
        this.planGrid.addSelectionListener(selection -> {
            selection.getFirstSelectedItem().ifPresent(this::selectPlan);
        });

        this.planAddComponent = new PlanAddComponent(this::addPlan);
        this.planAddComponent.setWidthFull();

        this.add(this.planGrid, this.planAddComponent);
        this.setSizeFull();
    }

    private void addPlan(@NonNull String planName) {
        var command = AddPlanCommand.builder()
            .planName(PlanName.of(planName))
            .build();
        this.planCommandService.addPlan(command, CURRENT_ACCOUNT);
        this.updateUi();
    }

    private void selectPlan(@NonNull PlanRepresentation selection) {
        getUI().ifPresent(ui -> ui.navigate(PlanEditView.class, selection.getId()));
    }

    private void updateUi() {
        getUI().ifPresent(ui -> ui.access(() -> {
            var plans = this.planQueryService.getAllPlans(CURRENT_ACCOUNT);
            this.planGrid.setItems(plans);
        }));
    }
}
