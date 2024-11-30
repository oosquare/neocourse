package io.github.oosquare.neocourse.ui.view.course;

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

import io.github.oosquare.neocourse.application.command.course.CourseCommandService;
import io.github.oosquare.neocourse.application.query.course.CourseQueryService;
import io.github.oosquare.neocourse.application.query.course.CourseRepresentation;
import io.github.oosquare.neocourse.application.security.CurrentAccountAwareSupport;
import io.github.oosquare.neocourse.application.security.Roles;
import io.github.oosquare.neocourse.ui.layout.MainLayout;

@Route(value = "courses", layout = MainLayout.class)
@RolesAllowed({Roles.TEACHER, Roles.ADMINISTRATOR})
public class CourseListView extends VerticalLayout
    implements CurrentAccountAwareSupport {

    private final @NonNull CourseCommandService courseCommandService;
    private final @NonNull CourseQueryService courseQueryService;

    private final @NonNull Grid<CourseRepresentation> courseGrid;

    public CourseListView(
        @NonNull CourseCommandService courseCommandService,
        @NonNull CourseQueryService courseQueryService
    ) {
        this.courseCommandService = courseCommandService;
        this.courseQueryService = courseQueryService;

        var newCourseButton = new Button("New Course");
        newCourseButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newCourseButton.addClickListener(event -> this.openCourseNewDialog());

        this.courseGrid = createCourseGrid();

        this.add(newCourseButton, this.courseGrid);
        this.setSizeFull();

        this.updateView();
    }

    private Grid<CourseRepresentation> createCourseGrid() {
        var courseGrid = new Grid<>(CourseRepresentation.class, false);
        courseGrid.addColumn(CourseRepresentation::getCourseName)
            .setHeader("Course Name");
        courseGrid.addColumn(CourseRepresentation::getClassPeriod)
            .setHeader("Class Period");
        courseGrid.addColumn(createCourseGridEditRender())
            .setHeader("Operation")
            .setAutoWidth(true)
            .setFlexGrow(0);
        courseGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        courseGrid.setSizeFull();
        return courseGrid;
    }

    private LitRenderer<CourseRepresentation> createCourseGridEditRender() {
        var button = "<vaadin-button @click=${handleClick}>Edit</vaadin-button>";
        return LitRenderer.<CourseRepresentation>of(button)
            .withFunction("handleClick", item -> this.openCourseEditDialog(item.getId()));
    }

    private void openCourseNewDialog() {
        var courseNewDialog = new CourseNewDialog(this.courseCommandService, this::updateView);
        courseNewDialog.open();
    }

    private void openCourseEditDialog(String courseId) {
        var courseEditDialog = new CourseEditDialog(
            this.courseCommandService,
            this.courseQueryService,
            courseId,
            this::updateView
        );
        courseEditDialog.open();
    }

    private void updateView() {
        Optional.ofNullable(UI.getCurrent()).ifPresent(ui -> ui.access(() -> {
            var account = this.getCurrentAccount();
            var courses = this.courseQueryService.getAllCourses(account);
            this.courseGrid.setItems(courses);
        }));
    }
}
