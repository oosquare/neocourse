package io.github.oosquare.neocourse.domain.schedule.model;

import java.util.Map;

import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.domain.schedule.exception.EvaluationException;
import io.github.oosquare.neocourse.utility.id.Id;

@Value
public class EvaluationSpecification {

    private final @NonNull Id schedule;
    private final @NonNull Map<Id, Registration> registrations;

    public void checkStudentEvaluable(@NonNull Id student) {
        this.checkStudentExistent(student);
    }

    private void checkStudentExistent(Id student) {
        if (this.registrations.containsKey(student)) {
            return;
        }
        throw new EvaluationException(
            String.format(
                "Student[id=%s] hasn't registered for Schedule[id=%s] yet",
                student,
                this.schedule
            )
        );
    }

}
