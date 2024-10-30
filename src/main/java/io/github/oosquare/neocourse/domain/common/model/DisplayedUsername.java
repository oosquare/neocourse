package io.github.oosquare.neocourse.domain.common.model;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.Value;

@Value
public class DisplayedUsername {

    private final @NonNull String value;

    private DisplayedUsername(String value) {
        Preconditions.checkArgument(
            !value.isBlank(),
            "Displayed username should not be blank"
        );
        Preconditions.checkArgument(
            value.strip().equals(value),
            "Displayed username should not have leading and trailing spaces"
        );
        this.value = value;
    }

    public static DisplayedUsername of(@NonNull String value) {
        return new DisplayedUsername(value);
    }

    public static DisplayedUsername ofInternally(@NonNull String value) {
        return DisplayedUsername.of(value);
    }
}
