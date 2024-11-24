package io.github.oosquare.neocourse.ui.view.course;

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
import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountKind;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.ui.layout.MainLayout;
import io.github.oosquare.neocourse.utility.id.Id;

@Route(value = "courses", layout = MainLayout.class)
public class CourseListView extends VerticalLayout {

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
        this.getUI().ifPresent(ui -> ui.access(() -> {
            var account = this.getCurrentAccount();
            var courses = this.courseQueryService.getAllCourses(account);
            this.courseGrid.setItems(courses);
        }));
    }

    private Account getCurrentAccount() {
        return CURRENT_ACCOUNT;
    }
}
