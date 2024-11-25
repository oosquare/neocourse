package io.github.oosquare.neocourse.ui.view.plan;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.Route;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.command.plan.PlanCommandService;
import io.github.oosquare.neocourse.application.query.plan.PlanQueryService;
import io.github.oosquare.neocourse.application.query.plan.PlanSummaryRepresentation;
import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountKind;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.ui.layout.MainLayout;
import io.github.oosquare.neocourse.utility.id.Id;

@Route(value = "plans", layout = MainLayout.class)
public class PlanListView extends VerticalLayout {

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

    private final @NonNull Grid<PlanSummaryRepresentation> planGrid;

    public PlanListView(
        @NonNull PlanCommandService planCommandService,
        @NonNull PlanQueryService planQueryService
    ) {
        this.planCommandService = planCommandService;
        this.planQueryService = planQueryService;

        var newPlanButton = new Button("New Plan");
        newPlanButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newPlanButton.addClickListener(event -> this.openPlanNewDialog());

        this.planGrid = createPlanGrid();

        this.add(newPlanButton, this.planGrid);
        this.setSizeFull();

        this.updateView();
    }

    private Grid<PlanSummaryRepresentation> createPlanGrid() {
        var planGrid = new Grid<>(PlanSummaryRepresentation.class, false);
        planGrid.addColumn(PlanSummaryRepresentation::getPlanName)
            .setHeader("Plan Name");
        planGrid.addColumn(PlanSummaryRepresentation::getTotalClassPeriod)
            .setHeader("Total Class Period");
        planGrid.addColumn(PlanSummaryRepresentation::getRequiredClassPeriod)
            .setHeader("Required Class Period");
        planGrid.addColumn(createPlanGridEditRender())
            .setHeader("Operation")
            .setFlexGrow(0);
        planGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        planGrid.setSizeFull();
        return planGrid;
    }

    private LitRenderer<PlanSummaryRepresentation> createPlanGridEditRender() {
        var button = "<vaadin-button @click=${handleClick}>Edit</vaadin-button>";
        return LitRenderer.<PlanSummaryRepresentation>of(button)
            .withFunction("handleClick", item -> this.openPlanEditDialog(item.getId()));
    }

    private void openPlanNewDialog() {
        var planNewDialog = new PlanNewDialog(this.planCommandService, this::updateView);
        planNewDialog.open();
    }

    private void openPlanEditDialog(String planId) {
        var planEditDialog = new PlanEditDialog(
            this.planCommandService,
            this.planQueryService,
            planId,
            this::updateView
        );
        planEditDialog.open();
    }

    private void updateView() {
        getUI().ifPresent(ui -> ui.access(() -> {
            var account = this.getCurrentAccount();
            var plans = this.planQueryService.getAllPlansInSummaryRepresentation(account);
            this.planGrid.setItems(plans);
        }));
    }

    private Account getCurrentAccount() {
        return CURRENT_ACCOUNT;
    }
}