package io.github.oosquare.neocourse.domain.course.model;

import lombok.Value;

import static com.google.common.base.Preconditions.*;

@Value
public class ClassPeriod {

    private final int value;

    public ClassPeriod(int value) {
        checkArgument(value > 0, "Class period should not be zero or negative");
        this.value = value;
    }
}
