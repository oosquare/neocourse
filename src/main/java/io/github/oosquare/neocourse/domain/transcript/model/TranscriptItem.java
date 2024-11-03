package io.github.oosquare.neocourse.domain.transcript.model;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.utility.id.Id;

@Value
@AllArgsConstructor(staticName = "ofInternally")
public class TranscriptItem {

    private final @NonNull Id course;
    private final @NonNull ClassPeriod classPeriod;
    private final @NonNull Score score;
    private final boolean evaluated;
    private final boolean participationAbsent;

    private TranscriptItem(@NonNull Id course, @NonNull ClassPeriod classPeriod) {
        this.course = course;
        this.classPeriod = classPeriod;
        this.score = Score.of(0);
        this.evaluated = false;
        this.participationAbsent = false;
    }

    public static TranscriptItem of(@NonNull Id course, @NonNull ClassPeriod classPeriod) {
        return new TranscriptItem(course, classPeriod);
    }

    public TranscriptItem assignScore(@NonNull Score score) {
        return new TranscriptItem(this.course, this.classPeriod, score, true, false);
    }

    public TranscriptItem markAbsent() {
        return new TranscriptItem(this.course, this.classPeriod, Score.of(0), true, true);
    }

    public boolean accountsForEstimatedClassPeriod() {
        return !this.isParticipationAbsent();
    }

    public Optional<Score> getScore() {
        return (this.isEvaluated() ? Optional.of(this.score) : Optional.empty());
    }
}
