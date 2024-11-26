package io.github.oosquare.neocourse.ui.component;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;

import io.github.oosquare.neocourse.application.query.schedule.ScheduleSummaryRepresentation;

public class ScheduleGrid extends Grid<ScheduleSummaryRepresentation> {

    public ScheduleGrid() {
        super(ScheduleSummaryRepresentation.class, false);

        this.addColumn(ScheduleSummaryRepresentation::getCourseName)
            .setHeader("Course Name");
        this.addColumn(ScheduleSummaryRepresentation::getTeacherDisplayedUsername)
            .setHeader("Teacher")
            .setAutoWidth(true)
            .setFlexGrow(0);
        this.addColumn(this.createPlanGridStartTimeRender())
            .setHeader("Start Time")
            .setAutoWidth(true)
            .setFlexGrow(0);
        this.addColumn(this.createPlanGridPeriodRender())
            .setHeader("Period")
            .setAutoWidth(true)
            .setFlexGrow(0);
        this.addColumn(ScheduleSummaryRepresentation::getPlace)
            .setHeader("Place")
            .setAutoWidth(true)
            .setFlexGrow(0);
        this.addColumn(ScheduleSummaryRepresentation::getCapacity)
            .setHeader("Capacity")
            .setAutoWidth(true)
            .setFlexGrow(0);
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
}
