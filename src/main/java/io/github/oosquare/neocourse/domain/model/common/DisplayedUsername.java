package io.github.oosquare.neocourse.domain.model.common;

import lombok.NonNull;
import lombok.Value;

import static com.google.common.base.Preconditions.checkArgument;

@Value
public class DisplayedUsername {

    private final @NonNull String value;

    public DisplayedUsername(@NonNull String value) {
        checkArgument(!value.isBlank(), "Displayed username should not be blank");
        checkArgument(
            value.strip().equals(value),
            "Displayed username should not have leading and trailing spaces"
        );
        this.value = value;
    }
}
