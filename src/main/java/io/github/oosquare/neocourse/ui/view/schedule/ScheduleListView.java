package io.github.oosquare.neocourse.ui.view.schedule;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.command.schedule.ScheduleCommandService;
import io.github.oosquare.neocourse.application.query.course.CourseQueryService;
import io.github.oosquare.neocourse.application.query.schedule.ScheduleQueryService;
import io.github.oosquare.neocourse.application.query.schedule.ScheduleSummaryRepresentation;
import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountKind;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.ui.layout.MainLayout;
import io.github.oosquare.neocourse.utility.id.Id;

@Route(value = "schedules", layout = MainLayout.class)
public class ScheduleListView extends VerticalLayout {

    private static final @NonNull Account CURRENT_ACCOUNT = Account.builder()
        .id(Id.of("account1"))
        .kind(AccountKind.TEACHER)
        .username(Username.of("test-teacher"))
        .displayedUsername(DisplayedUsername.of("Test Teacher"))
        .encodedPassword(EncodedPassword.of("password"))
        .user(Id.of("test-teacher"))
        .build();

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

        var newScheduleButton = new Button("New Schedule");
        newScheduleButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newScheduleButton.addClickListener(event -> this.openScheduleNewDialog());

        this.scheduleGrid = this.createScheduleGrid();

        this.add(newScheduleButton, this.scheduleGrid);
        this.setSizeFull();

        this.updateView();
    }

    private Grid<ScheduleSummaryRepresentation> createScheduleGrid() {
        var scheduleGrid = new Grid<>(ScheduleSummaryRepresentation.class, false);
        scheduleGrid.addColumn(ScheduleSummaryRepresentation::getCourseName)
            .setHeader("Course Name");
        scheduleGrid.addColumn(ScheduleSummaryRepresentation::getTeacherDisplayedUsername)
            .setHeader("Teacher")
            .setAutoWidth(true)
            .setFlexGrow(0);
        scheduleGrid.addColumn(this.createPlanGridStartTimeRender())
            .setHeader("Start Time")
            .setAutoWidth(true)
            .setFlexGrow(0);
        scheduleGrid.addColumn(this.createPlanGridPeriodRender())
            .setHeader("Period")
            .setFlexGrow(0);
        scheduleGrid.addColumn(ScheduleSummaryRepresentation::getPlace)
            .setHeader("Place")
            .setFlexGrow(0);
        scheduleGrid.addColumn(ScheduleSummaryRepresentation::getCapacity)
            .setHeader("Capacity")
            .setFlexGrow(0);
        scheduleGrid.addColumn(this.createPlanGridEditRender())
            .setHeader("Operation")
            .setFlexGrow(0);
        scheduleGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        scheduleGrid.setSizeFull();
        return scheduleGrid;
    }

    private LocalDateTimeRenderer<ScheduleSummaryRepresentation> createPlanGridStartTimeRender() {
        return new LocalDateTimeRenderer<>(
            schedule -> schedule.getStartTime().toLocalDateTime(),
            () -> DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
        );
    }

    private TextRenderer<ScheduleSummaryRepresentation> createPlanGridPeriodRender() {
        return new TextRenderer<>(schedule -> schedule.getPeriod().toMinutes() + " min");
    }

    private LitRenderer<ScheduleSummaryRepresentation> createPlanGridEditRender() {
        var button = "<vaadin-button @click=${handleClick}>Edit</vaadin-button>";
        return LitRenderer.<ScheduleSummaryRepresentation>of(button)
            .withFunction("handleClick", item -> this.openScheduleEditDialog(item.getId()));
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

    private void updateView() {
        Optional.ofNullable(UI.getCurrent()).ifPresent(ui -> ui.access(() -> {
            var account = this.getCurrentAccount();
            var schedules = this.scheduleQueryService.getAllSchedulesInSummaryRepresentation(account);
            this.scheduleGrid.setItems(schedules);
        }));
    }

    private Account getCurrentAccount() {
        return CURRENT_ACCOUNT;
    }
}
