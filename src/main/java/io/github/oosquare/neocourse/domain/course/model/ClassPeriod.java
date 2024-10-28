package io.github.oosquare.neocourse.domain.course.model;

import lombok.Value;

import static com.google.common.base.Preconditions.*;

@Value
public class ClassPeriod {

    private final double value;

    public ClassPeriod(double value) {
        checkArgument(value > 0, "Class period should not be zero or negative");
        this.value = value;
    }
}
