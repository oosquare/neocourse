package io.github.oosquare.neocourse.domain.student.model;

import com.google.common.base.Preconditions;
import lombok.ToString;
import lombok.Value;

@Value
@ToString(includeFieldNames = false)
public class Score {

    private static final double MIN_SCORE = 0;
    private static final double MAX_SCORE = 100;

    private static final Score ZERO = new Score(0);

    private final double value;

    private Score(double value) {
        Preconditions.checkArgument(
            Score.MIN_SCORE <= value && value <= Score.MAX_SCORE,
            "Score should be greater than 0 and less than 100"
        );
        this.value = value;
    }

    public static Score of(double value) {
        return (value == 0 ? Score.ZERO : new Score(value));
    }

    public static Score ofInternally(double value) {
        return Score.of(value);
    }
}
