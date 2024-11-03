package io.github.oosquare.neocourse.domain.transcript.model;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.utility.id.Id;

@Value
@AllArgsConstructor(staticName = "ofInternally")
public class TranscriptItem {

    private static final Score PASSING_SCORE = Score.of(60);

    private final @NonNull Id course;
    private final @NonNull ClassPeriod classPeriod;
    private final @NonNull Score score;
    private final boolean graded;

    private TranscriptItem(@NonNull Id course, @NonNull ClassPeriod classPeriod) {
        this.course = course;
        this.classPeriod = classPeriod;
        this.score = Score.of(0);
        this.graded = false;
    }

    public static TranscriptItem of(@NonNull Id course , @NonNull ClassPeriod classPeriod) {
        return new TranscriptItem(course, classPeriod);
    }

    public TranscriptItem assignScore(@NonNull Score score) {
        return new TranscriptItem(this.course, this.classPeriod, score, true);
    }
}
