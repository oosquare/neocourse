package io.github.oosquare.neocourse.domain.schedule.model;

import com.google.common.base.Preconditions;
import lombok.Value;

@Value
public class Capacity {

    private final int value;

    public Capacity(int value) {
        Preconditions.checkArgument(value > 0, "Capacity should be positive");
        this.value = value;
    }
}
