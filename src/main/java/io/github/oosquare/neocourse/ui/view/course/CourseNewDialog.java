package io.github.oosquare.neocourse.ui.view.course;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import lombok.Data;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.command.course.AddCourseCommand;
import io.github.oosquare.neocourse.application.command.course.CourseCommandService;
import io.github.oosquare.neocourse.application.security.CurrentAccountAwareSupport;
import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.CourseName;
import io.github.oosquare.neocourse.ui.component.CloseCallbackDialog;

public class CourseNewDialog extends CloseCallbackDialog
    implements CurrentAccountAwareSupport {

    @Data
    private static class CourseEditModel {

        private String courseName = "";
        private Integer classPeriod = 0;
    }

    private final @NonNull CourseCommandService courseCommandService;

    private final @NonNull Button addButton;
    private final @NonNull Binder<CourseEditModel> binder;

    public CourseNewDialog(
        @NonNull CourseCommandService courseCommandService,
        @NonNull CloseEventListener closeEventListener
    ) {
        super(closeEventListener);
        this.courseCommandService = courseCommandService;

        this.setHeaderTitle("New Course");

        var courseNameField = new TextField("Course Name");
        var classPeriodField = new TextField("Class Period");

        var dialogLayout = createDialogLayout(courseNameField, classPeriodField);
        this.add(dialogLayout);

        var cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event -> this.close());
        this.getFooter().add(cancelButton);

        var addButton = new Button("Add");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(event -> this.addButtonOnClickEvent());
        this.getFooter().add(addButton);
        this.addButton = addButton;

        this.binder = createBinder(courseNameField, classPeriodField);
    }

    private static VerticalLayout createDialogLayout(
        TextField courseNameField,
        TextField classPeriodField
    ) {
        var layout = new VerticalLayout(courseNameField, classPeriodField);
        layout.setPadding(false);
        layout.setPadding(false);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        layout.getStyle().set("width", "18rem").set("max-width", "100%");
        return layout;
    }

    private static Binder<CourseEditModel> createBinder(
        TextField courseNameField,
        TextField classPeriodField
    ) {
        var binder = new Binder<CourseEditModel>();
        binder.forField(courseNameField)
            .asRequired()
            .bind(CourseEditModel::getCourseName, CourseEditModel::setCourseName);
        binder.forField(classPeriodField)
            .asRequired()
            .withConverter(new StringToIntegerConverter("Class period is not a number"))
            .withValidator(new IntegerRangeValidator("Class period should be positive", 1, null))
            .bind(CourseEditModel::getClassPeriod, CourseEditModel::setClassPeriod);
        return binder;
    }

    private void addButtonOnClickEvent() {
        try {
            this.addButton.setEnabled(false);

            var model = new CourseEditModel();
            this.binder.writeBean(model);

            var command = AddCourseCommand.builder()
                .courseName(CourseName.of(model.getCourseName()))
                .classPeriod(ClassPeriod.of(model.getClassPeriod()))
                .build();
            var account = this.getCurrentAccount();
            this.courseCommandService.addCourse(command, account);

            this.close();
        } catch (ValidationException ignored) {
            // Error message is already shown in UI. Nothing needed here.
        } finally {
            this.addButton.setEnabled(true);
        }
    }
}
