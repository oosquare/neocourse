package io.github.oosquare.neocourse.domain.schedule.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

import io.github.oosquare.neocourse.utility.id.Id;

@Value
@AllArgsConstructor(staticName = "ofInternally")
public class Registration {

    private final @NonNull Id student;
    private final @NonNull @With(AccessLevel.PRIVATE) ParticipationStatus status;

    private Registration(Id student) {
        this(student, ParticipationStatus.ATTENDED);
    }

    public static Registration of(@NonNull Id student) {
        return new Registration(student);
    }

    public Registration markAttended() {
        return this.withStatus(ParticipationStatus.ATTENDED);
    }

    public Registration markAbsent() {
        return this.withStatus(ParticipationStatus.ABSENT);
    }
}
