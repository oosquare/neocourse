package io.github.oosquare.neocourse.domain.transcript.model;

import lombok.ToString;
import lombok.Value;

import io.github.oosquare.neocourse.utility.exception.ValueValidationException;

@Value
@ToString(includeFieldNames = false)
public class Score {

    private static final double MIN_SCORE = 0;
    private static final double MAX_SCORE = 100;

    private static final Score ZERO = new Score(0);

    private final double value;

    private Score(double value) {
        ValueValidationException.validator()
            .ensure(Score.MIN_SCORE <= value && value <= Score.MAX_SCORE)
            .message("Score should be greater than 0 and less than 100")
            .done();
        this.value = value;
    }

    public static Score of(double value) {
        return (value == 0 ? Score.ZERO : new Score(value));
    }

    public static Score ofInternally(double value) {
        return Score.of(value);
    }
}
