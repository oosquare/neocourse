package io.github.oosquare.neocourse.ui.view.evaluation;

import jakarta.annotation.security.RolesAllowed;
import java.util.Optional;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.command.evaluation.EvaluationCommandService;
import io.github.oosquare.neocourse.application.command.evaluation.GradeStudentCommand;
import io.github.oosquare.neocourse.application.command.evaluation.MarkStudentAbsentCommand;
import io.github.oosquare.neocourse.application.query.schedule.RegistrationEvaluationRepresentation;
import io.github.oosquare.neocourse.application.query.schedule.ScheduleEvaluationRepresentation;
import io.github.oosquare.neocourse.application.query.schedule.ScheduleQueryService;
import io.github.oosquare.neocourse.application.security.CurrentAccountAwareSupport;
import io.github.oosquare.neocourse.application.security.Roles;
import io.github.oosquare.neocourse.domain.transcript.model.Score;
import io.github.oosquare.neocourse.ui.component.EvaluationArea;
import io.github.oosquare.neocourse.ui.layout.MainLayout;
import io.github.oosquare.neocourse.ui.view.schedule.ScheduleListView;
import io.github.oosquare.neocourse.utility.id.Id;

@Route(value = "evaluation", layout = MainLayout.class)
@PageTitle("Evaluation | NeoCourse")
@RolesAllowed({Roles.TEACHER, Roles.ADMINISTRATOR})
public class EvaluationView extends VerticalLayout
    implements HasUrlParameter<String>, CurrentAccountAwareSupport {

    private final @NonNull EvaluationCommandService evaluationCommandService;
    private final @NonNull ScheduleQueryService scheduleQueryService;
    private String scheduleId;

    public EvaluationView(
        @NonNull EvaluationCommandService evaluationCommandService,
        @NonNull ScheduleQueryService scheduleQueryService
    ) {
        this.evaluationCommandService = evaluationCommandService;
        this.scheduleQueryService = scheduleQueryService;
    }

    @Override
    public void setParameter(BeforeEvent event, String scheduleId) {
        this.scheduleId = scheduleId;
        var schedule = this.scheduleQueryService.getScheduleByIdInEvaluationRepresentation(
            Id.of(scheduleId),
            this.getCurrentAccount()
        );

        var title = new H3("Evaluation");

        var idField = new TextField("ID");
        idField.setValue(schedule.getId());
        idField.setReadOnly(true);

        var courseNameField = new TextField("Course Name");
        courseNameField.setValue(schedule.getCourseName());
        courseNameField.setReadOnly(true);

        var informationLayout = new HorizontalLayout(idField, courseNameField);
        informationLayout.setPadding(false);

        var area = this.createEvaluationArea(schedule);

        var returnButton = new Button("Return");
        returnButton.addClickListener(event1 -> this.navigateToScheduleList());

        this.add(title, informationLayout, area, returnButton);
    }

    private VerticalLayout createEvaluationArea(ScheduleEvaluationRepresentation schedule) {
        var layout = new VerticalLayout();
        schedule.getRegistrations().forEach(registration -> {
            layout.add(this.createSingleEvaluationArea(registration));
        });
        layout.setSpacing(false);
        layout.setPadding(false);
        return layout;
    }

    private EvaluationArea createSingleEvaluationArea(RegistrationEvaluationRepresentation registration) {
        return new EvaluationArea(this::gradeStudent, this::markStudentAbsent, registration);
    }

    private void gradeStudent(String studentId, double score) {
        var command = GradeStudentCommand.builder()
            .studentId(Id.of(studentId))
            .scheduleId(Id.of(this.scheduleId))
            .score(Score.of(score))
            .build();
        var account = this.getCurrentAccount();
        this.evaluationCommandService.gradeStudent(command, account);
    }

    private void markStudentAbsent(String studentId) {
        var command = MarkStudentAbsentCommand.builder()
            .studentId(Id.of(studentId))
            .scheduleId(Id.of(this.scheduleId))
            .build();
        var account = this.getCurrentAccount();
        this.evaluationCommandService.markStudentAbsent(command, account);
    }

    private void navigateToScheduleList() {
        Optional.ofNullable(UI.getCurrent()).ifPresent(ui -> ui.navigate(ScheduleListView.class));
    }
}
