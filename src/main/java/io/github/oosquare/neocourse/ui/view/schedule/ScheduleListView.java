package io.github.oosquare.neocourse.ui.view.schedule;

import jakarta.annotation.security.RolesAllowed;
import java.util.Optional;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.command.schedule.ScheduleCommandService;
import io.github.oosquare.neocourse.application.query.course.CourseQueryService;
import io.github.oosquare.neocourse.application.query.schedule.ScheduleQueryService;
import io.github.oosquare.neocourse.application.query.schedule.ScheduleSummaryRepresentation;
import io.github.oosquare.neocourse.application.security.CurrentAccountAwareSupport;
import io.github.oosquare.neocourse.application.security.Roles;
import io.github.oosquare.neocourse.ui.component.ScheduleGrid;
import io.github.oosquare.neocourse.ui.layout.MainLayout;
import io.github.oosquare.neocourse.ui.view.evaluation.EvaluationView;

@Route(value = "schedules", layout = MainLayout.class)
@PageTitle("Schedules | NeoCourse")
@RolesAllowed({Roles.TEACHER, Roles.ADMINISTRATOR})
public class ScheduleListView extends VerticalLayout
    implements CurrentAccountAwareSupport {

    private final @NonNull ScheduleCommandService scheduleCommandService;
    private final @NonNull ScheduleQueryService scheduleQueryService;
    private final @NonNull CourseQueryService courseQueryService;

    private final @NonNull Grid<ScheduleSummaryRepresentation> scheduleGrid;

    public ScheduleListView(
        @NonNull ScheduleCommandService scheduleCommandService,
        @NonNull ScheduleQueryService scheduleQueryService,
        @NonNull CourseQueryService courseQueryService
    ) {
        this.scheduleCommandService = scheduleCommandService;
        this.scheduleQueryService = scheduleQueryService;
        this.courseQueryService = courseQueryService;

        var title = new H3("Schedules");

        var newScheduleButton = new Button("New Schedule");
        newScheduleButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newScheduleButton.addClickListener(event -> this.openScheduleNewDialog());

        this.scheduleGrid = this.createScheduleGrid();

        this.add(title, newScheduleButton, this.scheduleGrid);
        this.setSizeFull();

        this.updateView();
    }

    private Grid<ScheduleSummaryRepresentation> createScheduleGrid() {
        var scheduleGrid = new ScheduleGrid();
        scheduleGrid.addColumn(this.createPlanGridEditRender())
            .setHeader("Operation")
            .setAutoWidth(true)
            .setFlexGrow(0);
        scheduleGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        scheduleGrid.setSizeFull();
        return scheduleGrid;
    }

    private LitRenderer<ScheduleSummaryRepresentation> createPlanGridEditRender() {
        var buttons = """
            <div>
                <vaadin-button @click=${handleEditClick}>Edit</vaadin-button>
                <vaadin-button @click=${handleEvaluateClick}>Evaluate</vaadin-button>
            </div>
        """;
        return LitRenderer.<ScheduleSummaryRepresentation>of(buttons)
            .withFunction("handleEditClick", item -> this.openScheduleEditDialog(item.getId()))
            .withFunction("handleEvaluateClick", item -> this.navigateToEvaluationView(item.getId()));
    }

    private void openScheduleNewDialog() {
        var scheduleNewDialog = new ScheduleNewDialog(
            this.scheduleCommandService,
            this.courseQueryService,
            this::updateView
        );
        scheduleNewDialog.open();
    }

    private void openScheduleEditDialog(String scheduleId) {
        var scheduleEditDialog = new ScheduleEditDialog(
            this.scheduleCommandService,
            this.scheduleQueryService,
            scheduleId,
            this::updateView
        );
        scheduleEditDialog.open();
    }

    private void navigateToEvaluationView(String scheduleId) {
        Optional.ofNullable(UI.getCurrent()).ifPresent(ui -> {
            ui.navigate(EvaluationView.class, scheduleId);
        });
    }

    private void updateView() {
        Optional.ofNullable(UI.getCurrent()).ifPresent(ui -> ui.access(() -> {
            var account = this.getCurrentAccount();
            var schedules = this.scheduleQueryService.getAllSchedulesInSummaryRepresentation(account);
            this.scheduleGrid.setItems(schedules);
        }));
    }
}
