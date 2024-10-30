package io.github.oosquare.neocourse.domain.student.model;

import com.google.common.base.Preconditions;
import lombok.Value;

@Value
public class Score {

    private static final double MIN_SCORE = 0;
    private static final double MAX_SCORE = 100;

    private static final Score ZERO = new Score(0);

    private final double score;


    public Score(double score) {
        Preconditions.checkArgument(
            Score.MIN_SCORE <= score && score <= Score.MAX_SCORE,
            "Score should be greater than 0 and less than 100"
        );
        this.score = score;
    }

    public static Score of(double value) {
        return (value == 0 ? Score.ZERO : new Score(value));
    }
}
