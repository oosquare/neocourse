package io.github.oosquare.neocourse.ui.view.schedule;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.command.schedule.RemoveScheduleCommand;
import io.github.oosquare.neocourse.application.command.schedule.ScheduleCommandService;
import io.github.oosquare.neocourse.application.query.schedule.ScheduleQueryService;
import io.github.oosquare.neocourse.application.query.schedule.ScheduleSummaryRepresentation;
import io.github.oosquare.neocourse.application.security.CurrentAccountAwareSupport;
import io.github.oosquare.neocourse.ui.component.CloseCallbackDialog;
import io.github.oosquare.neocourse.utility.id.Id;

public class ScheduleEditDialog extends CloseCallbackDialog
    implements CurrentAccountAwareSupport {

    private final @NonNull ScheduleCommandService scheduleCommandService;
    private final @NonNull String scheduleId;

    private final @NonNull Button removeButton;

    public ScheduleEditDialog(
        @NonNull ScheduleCommandService scheduleCommandService,
        @NonNull ScheduleQueryService scheduleQueryService,
        @NonNull String scheduleId,
        @NonNull CloseEventListener closeEventListener
    ) {
        super(closeEventListener);
        this.scheduleCommandService = scheduleCommandService;
        this.scheduleId = scheduleId;

        this.setHeaderTitle("Edit Schedule");

        var schedule = scheduleQueryService.getScheduleByIdInSummaryRepresentation(
            Id.of(scheduleId),
            this.getCurrentAccount()
        );

        var dialogLayout = createDialogLayout(schedule);
        this.add(dialogLayout);

        var cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event -> this.close());
        this.getFooter().add(cancelButton);

        var removeButton = new Button("Remove");
        removeButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        removeButton.addClickListener(event -> this.removeButtonOnClickEvent());
        this.getFooter().add(removeButton);
        this.removeButton = removeButton;
    }

    private static HorizontalLayout createDialogLayout(ScheduleSummaryRepresentation schedule) {
        var idField = new TextField("ID");
        idField.setValue(schedule.getId());
        idField.setReadOnly(true);

        var courseNameField = new TextField("Course Name");
        courseNameField.setValue(schedule.getCourseName());
        courseNameField.setReadOnly(true);

        var teacherField = new TextField("Teacher");
        teacherField.setValue(schedule.getTeacherDisplayedUsername());
        teacherField.setReadOnly(true);

        var startTimeField = new TextField("Start Time");
        var localStartTime = schedule.getStartTime().toLocalDateTime();
        var formatted = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(localStartTime);
        startTimeField.setValue(formatted);
        startTimeField.setReadOnly(true);

        var periodField = new TextField("Period");
        periodField.setValue(schedule.getPeriod().toMinutes() + " min");
        periodField.setReadOnly(true);

        var placeField = new TextField("Place");
        placeField.setValue(schedule.getPlace());
        placeField.setReadOnly(true);

        var capacityField = new TextField("Capacity");
        capacityField.setValue(schedule.getCapacity().toString());
        capacityField.setReadOnly(true);

        var leftLayout = new VerticalLayout(idField, courseNameField, teacherField, placeField);
        leftLayout.setSpacing(false);
        leftLayout.setPadding(false);
        leftLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        leftLayout.setWidth("18rem");
        leftLayout.setMaxWidth("50%");

        var rightLayout = new VerticalLayout(startTimeField, periodField, capacityField);
        rightLayout.setSpacing(false);
        rightLayout.setPadding(false);
        rightLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        rightLayout.setWidth("18rem");
        rightLayout.setMaxWidth("50%");

        var dialogLayout = new HorizontalLayout(leftLayout, rightLayout);
        dialogLayout.setPadding(false);
        return dialogLayout;
    }

    private void removeButtonOnClickEvent() {
        try {
            this.removeButton.setEnabled(false);

            this.removeSchedule();

            this.close();
        } finally {
            this.removeButton.setEnabled(true);
        }
    }

    private void removeSchedule() {
        var command = RemoveScheduleCommand.builder()
            .scheduleId(Id.of(scheduleId))
            .build();
        var account = this.getCurrentAccount();
        this.scheduleCommandService.removeSchedule(command, account);
    }

}
