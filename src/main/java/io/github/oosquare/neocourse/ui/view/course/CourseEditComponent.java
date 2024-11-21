package io.github.oosquare.neocourse.ui.view.course;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import lombok.Data;
import lombok.NonNull;

public class CourseEditComponent extends VerticalLayout {

    @FunctionalInterface
    public interface AddEventListener {

        void onAddEvent(@NonNull String courseName, int classPeriod);
    }

    @Data
    private static class CourseEditModel {

        private String courseName = "";
        private Integer classPeriod = 0;
    }

    private final @NonNull TextField courseNameField;
    private final @NonNull TextField classPeriodField;
    private final @NonNull Button addButton;
    private final @NonNull Button resetButton;

    private final @NonNull Binder<CourseEditModel> binder;

    private final @NonNull AddEventListener addEventListener;

    public CourseEditComponent(@NonNull AddEventListener addEventListener) {
        this.addEventListener = addEventListener;

        this.courseNameField = new TextField("Course Name");
        this.classPeriodField = new TextField("Class Period");
        this.addButton = new Button("Add", this::addButtonOnClick);
        this.resetButton = new Button("Reset", this::resetButtonOnClick);

        this.add(
            new HorizontalLayout(this.courseNameField, this.classPeriodField),
            new HorizontalLayout(this.addButton, this.resetButton)
        );

        this.binder = new Binder<>();
        this.binder.forField(this.courseNameField)
            .asRequired()
            .bind(CourseEditModel::getCourseName, CourseEditModel::setCourseName);
        this.binder.forField(this.classPeriodField)
            .asRequired()
            .withConverter(new StringToIntegerConverter("Class period is not a number"))
            .withValidator(new IntegerRangeValidator("Class period should be positive", 1, null))
            .bind(CourseEditModel::getClassPeriod, CourseEditModel::setClassPeriod);
    }

    private void addButtonOnClick(@NonNull ClickEvent<Button> event) {
        try {
            this.addButton.setEnabled(false);

            var model = new CourseEditModel();
            this.binder.writeBean(model);
            this.addEventListener.onAddEvent(model.getCourseName(), model.getClassPeriod());

            this.resetFields();
        } catch (ValidationException ignored) {
            // Error message is already shown in UI. Nothing needed here.
        } finally {
            this.addButton.setEnabled(true);
        }
    }

    private void resetButtonOnClick(@NonNull ClickEvent<Button> event) {
        this.resetFields();
    }

    private void resetFields() {
        this.courseNameField.setValue("");
        this.classPeriodField.setValue("");
    }
}