package io.github.oosquare.neocourse.domain.schedule.model;

import com.google.common.base.Preconditions;
import lombok.Value;

@Value
public class Capacity {

    private final int value;

    private Capacity(int value) {
        Preconditions.checkArgument(value > 0, "Capacity should be positive");
        this.value = value;
    }

    public static Capacity of(int value) {
        return new Capacity(value);
    }

    public static Capacity ofInternally(int value) {
        return Capacity.of(value);
    }
}
