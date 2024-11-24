package io.github.oosquare.neocourse.ui.view.course;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.command.course.AddCourseCommand;
import io.github.oosquare.neocourse.application.command.course.CourseCommandService;
import io.github.oosquare.neocourse.application.query.course.CourseQueryService;
import io.github.oosquare.neocourse.application.query.course.CourseRepresentation;
import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountKind;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.CourseName;
import io.github.oosquare.neocourse.ui.layout.MainLayout;
import io.github.oosquare.neocourse.utility.id.Id;

@Route(value = "courses", layout = MainLayout.class)
public class CourseView extends VerticalLayout {

    private static final @NonNull Account CURRENT_ACCOUNT = Account.builder()
        .id(Id.of("account0"))
        .kind(AccountKind.ADMINISTRATOR)
        .username(Username.of("test-account"))
        .displayedUsername(DisplayedUsername.of("Test Account"))
        .encodedPassword(EncodedPassword.of("password"))
        .user(Id.of("test-user"))
        .build();

    private final @NonNull CourseCommandService courseCommandService;
    private final @NonNull CourseQueryService courseQueryService;

    private final @NonNull Grid<CourseRepresentation> courseGrid;
    private final @NonNull CourseEditComponent courseEditComponent;

    public CourseView(
        @NonNull CourseCommandService courseCommandService,
        @NonNull CourseQueryService courseQueryService
    ) {
        this.courseCommandService = courseCommandService;
        this.courseQueryService = courseQueryService;

        this.courseGrid = new Grid<>(CourseRepresentation.class, false);
        this.courseGrid.addColumn(CourseRepresentation::getId).setHeader("ID");
        this.courseGrid.addColumn(CourseRepresentation::getName).setHeader("Course Name");
        this.courseGrid.addColumn(CourseRepresentation::getClassPeriod).setHeader("Class Period");
        this.courseGrid.setItems(this.courseQueryService.getAllCourses(CURRENT_ACCOUNT));
        this.courseGrid.setSizeFull();

        this.courseEditComponent = new CourseEditComponent(this::addCourse);
        this.courseEditComponent.setWidthFull();

        this.add(this.courseGrid, this.courseEditComponent);
        this.setSizeFull();
    }

    private void addCourse(@NonNull String courseName, int classPeriod) {
        var command = AddCourseCommand.builder()
            .courseName(CourseName.of(courseName))
            .classPeriod(ClassPeriod.of(classPeriod))
            .build();
        this.courseCommandService.addCourse(command, CURRENT_ACCOUNT);
        this.updateUi();
    }

    private void updateUi() {
        getUI().ifPresent(ui -> ui.access(() -> {
            var courses = this.courseQueryService.getAllCourses(CURRENT_ACCOUNT);
            this.courseGrid.setItems(courses);
        }));
    }
}
