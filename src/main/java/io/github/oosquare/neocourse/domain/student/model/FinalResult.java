package io.github.oosquare.neocourse.domain.student.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FinalResult {

    private static final FinalResult UNFINISHED = new FinalResult(false, Score.of(0));

    private final boolean isPlanFinished;
    private final @NonNull Score score;

    public static FinalResult ofPlanUnfinished() {
        return FinalResult.UNFINISHED;
    }

    public static FinalResult ofPlanFinished(double score) {
        return new FinalResult(true, new Score(score));
    }
}
