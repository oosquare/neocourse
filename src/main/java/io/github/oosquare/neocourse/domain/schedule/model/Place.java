package io.github.oosquare.neocourse.domain.schedule.model;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.Value;

@Value
public class Place {

    private final @NonNull String value;

    public Place(@NonNull String value) {
        Preconditions.checkArgument(!value.isBlank(), "Place should not be blank");
        this.value = value;
    }
}
