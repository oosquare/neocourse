package io.github.oosquare.neocourse.ui.view.plan;

import jakarta.annotation.security.RolesAllowed;
import java.util.Optional;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.Route;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.command.plan.PlanCommandService;
import io.github.oosquare.neocourse.application.query.course.CourseQueryService;
import io.github.oosquare.neocourse.application.query.plan.PlanQueryService;
import io.github.oosquare.neocourse.application.query.plan.PlanSummaryRepresentation;
import io.github.oosquare.neocourse.application.security.CurrentAccountAwareSupport;
import io.github.oosquare.neocourse.application.security.Roles;
import io.github.oosquare.neocourse.ui.layout.MainLayout;

@Route(value = "plans", layout = MainLayout.class)
@RolesAllowed({Roles.TEACHER, Roles.ADMINISTRATOR})
public class PlanListView extends VerticalLayout
    implements CurrentAccountAwareSupport {

    private final @NonNull PlanCommandService planCommandService;
    private final @NonNull PlanQueryService planQueryService;
    private final @NonNull CourseQueryService courseQueryService;

    private final @NonNull Grid<PlanSummaryRepresentation> planGrid;

    public PlanListView(
        @NonNull PlanCommandService planCommandService,
        @NonNull PlanQueryService planQueryService,
        @NonNull CourseQueryService courseQueryService
    ) {
        this.planCommandService = planCommandService;
        this.planQueryService = planQueryService;
        this.courseQueryService = courseQueryService;

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
            .setAutoWidth(true)
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
            this.courseQueryService,
            planId,
            this::updateView
        );
        planEditDialog.open();
    }

    private void updateView() {
        Optional.ofNullable(UI.getCurrent()).ifPresent(ui -> ui.access(() -> {
            var account = this.getCurrentAccount();
            var plans = this.planQueryService.getAllPlansInSummaryRepresentation(account);
            this.planGrid.setItems(plans);
        }));
    }
}
