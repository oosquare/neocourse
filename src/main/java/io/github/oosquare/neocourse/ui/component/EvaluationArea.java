package io.github.oosquare.neocourse.ui.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.validator.DoubleRangeValidator;
import lombok.Data;
import lombok.NonNull;

import io.github.oosquare.neocourse.application.query.schedule.RegistrationEvaluationRepresentation;

public class EvaluationArea extends VerticalLayout {

    @FunctionalInterface
    public interface EvaluateWhenAttendedListener {

        void evaluate(String studentId, double score);
    }

    @FunctionalInterface
    public interface EvaluateWhenAbsentListener {
        void evaluate(String studentId);
    }

    @Data
    private static class ScoreEditModel {

        private double score;
    }

    private static final String RADIO_ITEM_ATTENDED = "Attended";
    private static final String RADIO_ITEM_ABSENT = "Absent";

    private final @NonNull Button updateButton;
    private final @NonNull Binder<ScoreEditModel> scoreBinder;

    private final @NonNull EvaluateWhenAttendedListener evaluateWhenAttendedListener;
    private final @NonNull EvaluateWhenAbsentListener evaluateWhenAbsentListener;
    private final @NonNull String studentId;
    private String participationStatusSelection;

    public EvaluationArea(
        @NonNull EvaluateWhenAttendedListener evaluateWhenAttendedListener,
        @NonNull EvaluateWhenAbsentListener evaluateWhenAbsentListener,
        @NonNull RegistrationEvaluationRepresentation registration
    ) {
        this.evaluateWhenAttendedListener = evaluateWhenAttendedListener;
        this.evaluateWhenAbsentListener = evaluateWhenAbsentListener;
        this.studentId = registration.getStudentId();
        this.participationStatusSelection = registration.getParticipationStatus();

        var studentNameField = new TextField("Student");
        studentNameField.setValue(registration.getStudentName());
        studentNameField.setReadOnly(true);

        var participationStatusRadios = new RadioButtonGroup<String>("Participation Status");
        participationStatusRadios.setItems(RADIO_ITEM_ATTENDED, RADIO_ITEM_ABSENT);
        participationStatusRadios.setValue(registration.getParticipationStatus());

        var scoreField = new TextField("Score");
        scoreField.setValue(registration.getScore().toString());

        participationStatusRadios.addValueChangeListener(event -> {
            this.participationStatusSelection = event.getValue();
            scoreField.setEnabled(event.getValue().equals(RADIO_ITEM_ATTENDED));
        });

        var layout = new HorizontalLayout(studentNameField, participationStatusRadios, scoreField);
        layout.setPadding(false);

        var updateButton = new Button("Update");
        updateButton.addClickListener(event -> this.updateButtonOnClickEvent());
        this.updateButton = updateButton;

        this.add(layout, updateButton);
        this.setPadding(false);

        this.scoreBinder = createScoreBinder(scoreField);
    }

    private static Binder<ScoreEditModel> createScoreBinder(TextField scoreField) {
        var scoreBinder = new Binder<ScoreEditModel>();
        scoreBinder.forField(scoreField)
            .asRequired()
            .withConverter(new StringToDoubleConverter("Score is not a number"))
            .withValidator(new DoubleRangeValidator("Score should be between 0 and 100", 0.0, 100.0))
            .bind(ScoreEditModel::getScore, ScoreEditModel::setScore);
        return scoreBinder;
    }

    private void updateButtonOnClickEvent() {
        if (this.participationStatusSelection.equals(RADIO_ITEM_ATTENDED)) {
            try {
                this.updateButton.setEnabled(false);

                var model = new ScoreEditModel();
                this.scoreBinder.writeBean(model);

                this.evaluateWhenAttendedListener.evaluate(this.studentId, model.getScore());
            } catch (ValidationException ignored) {
                // Error message is already shown in UI. Nothing needed here.
            } finally {
                this.updateButton.setEnabled(true);
            }
        } else {
            this.evaluateWhenAbsentListener.evaluate(this.studentId);
        }
    }
}
