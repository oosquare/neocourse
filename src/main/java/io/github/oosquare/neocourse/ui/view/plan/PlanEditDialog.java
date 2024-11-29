package io.github.oosquare.neocourse.ui.view.plan;

import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import lombok.Data;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.command.plan.AssignRequiredClassPeriodCommand;
import io.github.oosquare.neocourse.application.command.plan.ExcludeCourseCommand;
import io.github.oosquare.neocourse.application.command.plan.IncludeCourseCommand;
import io.github.oosquare.neocourse.application.command.plan.PlanCommandService;
import io.github.oosquare.neocourse.application.query.course.CourseQueryService;
import io.github.oosquare.neocourse.application.query.course.CourseRepresentation;
import io.github.oosquare.neocourse.application.query.plan.PlanQueryService;
import io.github.oosquare.neocourse.application.query.plan.PlanRepresentation;
import io.github.oosquare.neocourse.application.security.CurrentAccountAwareSupport;
import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountKind;
import io.github.oosquare.neocourse.domain.account.model.EncodedPassword;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.ui.component.CloseCallbackDialog;
import io.github.oosquare.neocourse.utility.id.Id;

public class PlanEditDialog extends CloseCallbackDialog
    implements CurrentAccountAwareSupport {

    @Data
    private static class ClassPeriodEditModel {

        private Integer classPeriod = 0;
    }

    private final @NonNull PlanCommandService planCommandService;
    private final @NonNull PlanQueryService planQueryService;
    private final @NonNull CourseQueryService courseQueryService;
    private final @NonNull PlanRepresentation cachedPlan;

    private final @NonNull Button applyButton;
    private final @NonNull Grid<CourseRepresentation> courseGrid;
    private final @NonNull Binder<ClassPeriodEditModel> binder;

    public PlanEditDialog(
        @NonNull PlanCommandService planCommandService,
        @NonNull PlanQueryService planQueryService,
        @NonNull CourseQueryService courseQueryService,
        @NonNull String planId,
        @NonNull CloseEventListener closeEventListener
    ) {
        super(closeEventListener);
        this.planCommandService = planCommandService;
        this.planQueryService = planQueryService;
        this.courseQueryService = courseQueryService;

        var plan = this.planQueryService.getPlanById(Id.of(planId), this.getCurrentAccount());
        this.cachedPlan = plan;

        this.setHeaderTitle("Edit Plan");

        var idField = new TextField("ID");
        idField.setValue(plan.getId());
        idField.setReadOnly(true);

        var courseNameField = new TextField("Plan Name");
        courseNameField.setValue(plan.getPlanName());
        courseNameField.setReadOnly(true);

        var totalClassPeriodField = new TextField("Total Class Period");
        totalClassPeriodField.setValue(plan.getTotalClassPeriod().toString());
        totalClassPeriodField.setReadOnly(true);

        var requiredClassPeriodField = new TextField("Required Class Period");
        requiredClassPeriodField.setValue(plan.getRequiredClassPeriod().toString());

        var leftDialogLayout = createLeftDialogLayout(
            idField,
            courseNameField,
            totalClassPeriodField,
            requiredClassPeriodField
        );

        this.courseGrid = this.createCourseGrid();

        var dialogLayout = this.createDialogLayout(leftDialogLayout);
        this.add(dialogLayout);

        var cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event -> this.close());
        this.getFooter().add(cancelButton);

        var applyButton = new Button("Apply");
        applyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        applyButton.addClickListener(event -> this.applyButtonOnClickEvent());
        this.getFooter().add(applyButton);
        this.applyButton = applyButton;

        this.binder = createBinder(requiredClassPeriodField);
    }

    private HorizontalLayout createDialogLayout(VerticalLayout leftDialogLayout) {
        var layout = new HorizontalLayout(leftDialogLayout, this.courseGrid);
        layout.setPadding(false);
        layout.setSizeFull();
        return layout;
    }

    private static VerticalLayout createLeftDialogLayout(
        TextField idField,
        TextField planNameField,
        TextField totalClassPeriodField,
        TextField requiredClassPeriodField
    ) {
        var layout = new VerticalLayout(
            idField,
            planNameField,
            totalClassPeriodField,
            requiredClassPeriodField
        );
        layout.setPadding(false);
        layout.setPadding(false);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        layout.setWidth("18rem");
        layout.setMaxWidth("50%");
        return layout;
    }

    private Grid<CourseRepresentation> createCourseGrid() {
        var courseGrid = new Grid<>(CourseRepresentation.class, false);
        courseGrid.addColumn(CourseRepresentation::getCourseName)
            .setHeader("Course Name");
        courseGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        courseGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        courseGrid.setWidth("18rem");
        courseGrid.setMaxWidth("50%");
        courseGrid.setHeightFull();

        var courses = this.courseQueryService.getAllCourses(this.getCurrentAccount());
        courseGrid.setItems(courses);
        courses.stream()
            .filter(course -> this.cachedPlan.getIncludedCourseIds().contains(course.getId()))
            .forEach(courseGrid::select);

        return courseGrid;
    }

    private static Binder<ClassPeriodEditModel> createBinder(TextField requiredClassPeriodField) {
        var binder = new Binder<ClassPeriodEditModel>();
        binder.forField(requiredClassPeriodField)
            .asRequired()
            .withConverter(new StringToIntegerConverter("Class period is not a number"))
            .withValidator(new IntegerRangeValidator("Class period should be positive", 1, null))
            .bind(ClassPeriodEditModel::getClassPeriod, ClassPeriodEditModel::setClassPeriod);
        return binder;
    }

    private void applyButtonOnClickEvent() {
        try {
            this.applyButton.setEnabled(false);

            var model = new ClassPeriodEditModel();
            this.binder.writeBean(model);

            this.modifyIncludedCourses(this.courseGrid.getSelectedItems());
            this.modifyRequiredClassPeriod(model);

            this.close();
        } catch (ValidationException ignored) {
            // Error message is already shown in UI. Nothing needed here.
        } finally {
            this.applyButton.setEnabled(true);
        }
    }

    private void modifyRequiredClassPeriod(ClassPeriodEditModel model) {
        if (model.getClassPeriod().equals(this.cachedPlan.getRequiredClassPeriod())) {
            return;
        }
        var command = AssignRequiredClassPeriodCommand.builder()
            .planId(Id.of(this.cachedPlan.getId()))
            .requiredClassPeriod(ClassPeriod.of(model.getClassPeriod()))
            .build();
        var account = this.getCurrentAccount();
        this.planCommandService.assignRequiredClassPeriod(command, account);
    }

    private void modifyIncludedCourses(Set<CourseRepresentation> selectedCourses) {
        var selectedIds = selectedCourses.stream()
            .map(CourseRepresentation::getId)
            .collect(Collectors.toSet());
        var courseIdsToInclude = Sets.difference(selectedIds, this.cachedPlan.getIncludedCourseIds());
        var courseIdsToExclude = Sets.difference(this.cachedPlan.getIncludedCourseIds(), selectedIds);

        var account = this.getCurrentAccount();

        courseIdsToInclude.forEach(courseId -> {
            var command = IncludeCourseCommand.builder()
                .planId(Id.of(this.cachedPlan.getId()))
                .courseId(Id.of(courseId))
                .build();
            this.planCommandService.includeCourseToPlan(command, account);
        });

        courseIdsToExclude.forEach(courseId -> {
            var command = ExcludeCourseCommand.builder()
                .planId(Id.of(this.cachedPlan.getId()))
                .courseId(Id.of(courseId))
                .build();
            this.planCommandService.excludeCourseFromPlan(command, account);
        });
    }
}
