package io.github.oosquare.neocourse.domain.course.model;

import com.google.common.base.Preconditions;
import lombok.Value;

@Value
public class ClassPeriod {

    private final int value;

    public ClassPeriod(int value) {
        Preconditions.checkArgument(value > 0, "Class period should not be zero or negative");
        this.value = value;
    }
}
