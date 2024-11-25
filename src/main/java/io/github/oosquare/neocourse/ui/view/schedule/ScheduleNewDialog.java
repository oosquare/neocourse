package io.github.oosquare.neocourse.ui.view.schedule;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import lombok.Data;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.command.schedule.AddScheduleCommand;
import io.github.oosquare.neocourse.application.command.schedule.ScheduleCommandService;
import io.github.oosquare.neocourse.application.query.course.CourseQueryService;
import io.github.oosquare.neocourse.application.query.course.CourseRepresentation;
import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountKind;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.schedule.model.Capacity;
import io.github.oosquare.neocourse.domain.schedule.model.Place;
import io.github.oosquare.neocourse.utility.id.Id;

public class ScheduleNewDialog extends Dialog {

    @Data
    private static class ScheduleEditModel {

        private String courseId;
        private LocalDateTime startTime;
        private String place;
        private Integer capacity;
    }

    @FunctionalInterface
    public interface CloseEventListener {

        void onCloseEvent();
    }

    private class CourseIdConverter implements Converter<CourseRepresentation, String> {

        @Override
        public Result<String> convertToModel(CourseRepresentation value, ValueContext context) {
            return Result.ok(value.getId());
        }

        @Override
        public CourseRepresentation convertToPresentation(String courseId, ValueContext context) {
            return courseQueryService.getCourseById(Id.of(courseId), getCurrentAccount());
        }
    }

    private static final @NonNull Account CURRENT_ACCOUNT = Account.builder()
        .id(Id.of("account1"))
        .kind(AccountKind.TEACHER)
        .username(Username.of("test-teacher"))
        .displayedUsername(DisplayedUsername.of("Test Teacher"))
        .encodedPassword(EncodedPassword.of("password"))
        .user(Id.of("test-teacher"))
        .build();

    private final @NonNull ScheduleCommandService scheduleCommandService;
    private final @NonNull CourseQueryService courseQueryService;

    private final @NonNull CloseEventListener closeEventListener;

    private final @NonNull Button addButton;
    private final @NonNull Binder<ScheduleEditModel> binder;

    public ScheduleNewDialog(
        @NonNull ScheduleCommandService scheduleCommandService,
        @NonNull CourseQueryService courseQueryService,
        @NonNull CloseEventListener closeEventListener
    ) {
        this.scheduleCommandService = scheduleCommandService;
        this.courseQueryService = courseQueryService;
        this.closeEventListener = closeEventListener;

        this.setHeaderTitle("New Schedule");

        var courseCombo = this.createCourseCombo();
        var startTimePicker = new DateTimePicker("Start Time");
        var placeField = new TextField("Place");
        var capacityField = new TextField("Capacity");

        var dialogLayout = createDialogLayout(courseCombo, startTimePicker, placeField, capacityField);
        this.add(dialogLayout);

        var cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event -> this.close());
        this.getFooter().add(cancelButton);

        var addButton = new Button("Add");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(event -> this.addButtonOnClickEvent());
        this.getFooter().add(addButton);
        this.addButton = addButton;

        this.binder = this.createBinder(courseCombo, startTimePicker, placeField, capacityField);
    }

    private ComboBox<CourseRepresentation> createCourseCombo() {
        var courses = this.courseQueryService.getAllCourses(this.getCurrentAccount());

        var courseComboBox = new ComboBox<CourseRepresentation>("Course");
        courseComboBox.setItems(courses);
        courseComboBox.setItemLabelGenerator(CourseRepresentation::getCourseName);
        return courseComboBox;
    }

    private static VerticalLayout createDialogLayout(
        ComboBox<CourseRepresentation> courseCombo,
        DateTimePicker startTimePicker,
        TextField placeField,
        TextField capacityField
    ) {
        var layout = new VerticalLayout(courseCombo, startTimePicker, placeField, capacityField);
        layout.setPadding(false);
        layout.setPadding(false);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        layout.getStyle().set("width", "18rem").set("max-width", "100%");
        return layout;
    }

    private Binder<ScheduleEditModel> createBinder(
        ComboBox<CourseRepresentation> courseCombo,
        DateTimePicker startTimePicker,
        TextField placeField,
        TextField capacityField
    ) {
        var binder = new Binder<ScheduleEditModel>();
        binder.forField(courseCombo)
            .asRequired()
            .withConverter(new CourseIdConverter())
            .bind(ScheduleEditModel::getCourseId, ScheduleEditModel::setCourseId);
        binder.forField(startTimePicker)
            .asRequired()
            .bind(ScheduleEditModel::getStartTime, ScheduleEditModel::setStartTime);
        binder.forField(placeField)
            .asRequired()
            .bind(ScheduleEditModel::getPlace, ScheduleEditModel::setPlace);
        binder.forField(capacityField)
            .asRequired()
            .withConverter(new StringToIntegerConverter("Capacity is not a number"))
            .withValidator(new IntegerRangeValidator("Capacity should be positive", 1, null))
            .bind(ScheduleEditModel::getCapacity, ScheduleEditModel::setCapacity);
        return binder;
    }

    private void addButtonOnClickEvent() {
        try {
            this.addButton.setEnabled(false);

            var model = new ScheduleEditModel();
            this.binder.writeBean(model);

            this.addSchedule(model);

            this.close();
            this.closeEventListener.onCloseEvent();
        } catch (ValidationException ignored) {
            // Error message is already shown in UI. Nothing needed here.
        } finally {
            this.addButton.setEnabled(true);
        }
    }

    private void addSchedule(ScheduleEditModel model) {
        var command = AddScheduleCommand.builder()
            .courseId(Id.of(model.getCourseId()))
            .startTime(model.getStartTime().atZone(ZoneId.systemDefault()))
            .place(Place.of(model.getPlace()))
            .capacity(Capacity.of(model.getCapacity()))
            .build();
        var account = this.getCurrentAccount();
        this.scheduleCommandService.addSchedule(command, account);
    }

    private Account getCurrentAccount() {
        return CURRENT_ACCOUNT;
    }
}
