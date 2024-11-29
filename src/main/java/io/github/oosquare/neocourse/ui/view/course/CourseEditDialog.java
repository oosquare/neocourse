package io.github.oosquare.neocourse.ui.view.course;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.command.course.CourseCommandService;
import io.github.oosquare.neocourse.application.command.course.RemoveCourseCommand;
import io.github.oosquare.neocourse.application.query.course.CourseQueryService;
import io.github.oosquare.neocourse.application.security.CurrentAccountAwareSupport;
import io.github.oosquare.neocourse.ui.component.CloseCallbackDialog;
import io.github.oosquare.neocourse.utility.id.Id;

public class CourseEditDialog extends CloseCallbackDialog
    implements CurrentAccountAwareSupport {

    private final @NonNull CourseCommandService courseCommandService;
    private final @NonNull CourseQueryService courseQueryService;
    private final @NonNull String courseId;

    private final @NonNull Button removeButton;

    public CourseEditDialog(
        @NonNull CourseCommandService courseCommandService,
        @NonNull CourseQueryService courseQueryService,
        @NonNull String courseId,
        @NonNull CloseEventListener closeEventListener
    ) {
        super(closeEventListener);
        this.courseCommandService = courseCommandService;
        this.courseQueryService = courseQueryService;
        this.courseId = courseId;

        var course = this.courseQueryService.getCourseById(Id.of(courseId), this.getCurrentAccount());

        this.setHeaderTitle("Edit Course");

        var idField = new TextField("ID");
        idField.setValue(course.getId());
        idField.setReadOnly(true);

        var courseNameField = new TextField("Course Name");
        courseNameField.setValue(course.getCourseName());
        courseNameField.setReadOnly(true);

        var classPeriodField = new TextField("Class Period");
        classPeriodField.setValue(course.getClassPeriod().toString());
        classPeriodField.setReadOnly(true);

        var dialogLayout = createDialogLayout(idField, courseNameField, classPeriodField);
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

    private static VerticalLayout createDialogLayout(
        TextField idField,
        TextField courseNameField,
        TextField classPeriodField
    ) {
        var layout = new VerticalLayout(idField, courseNameField, classPeriodField);
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        layout.getStyle().set("width", "18rem").set("max-width", "100%");
        return layout;
    }

    private void removeButtonOnClickEvent() {
        try {
            this.removeButton.setEnabled(false);

            var command = RemoveCourseCommand.builder()
                .courseId(Id.of(this.courseId))
                .build();
            var account = this.getCurrentAccount();
            this.courseCommandService.removeCourse(command, account);

            this.close();
        } finally {
            this.removeButton.setEnabled(true);
        }
    }
}
