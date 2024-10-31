package io.github.oosquare.neocourse.domain.schedule.model;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@ToString(includeFieldNames = false)
public class Place {

    private final @NonNull String value;

    private Place(@NonNull String value) {
        Preconditions.checkArgument(!value.isBlank(), "Place should not be blank");
        this.value = value;
    }

    public static Place of(@NonNull String value) {
        return new Place(value);
    }

    public static Place ofInternally(@NonNull String value) {
        return Place.of(value);
    }
}
