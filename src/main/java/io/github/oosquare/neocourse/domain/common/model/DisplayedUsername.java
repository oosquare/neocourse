package io.github.oosquare.neocourse.domain.common.model;

import lombok.NonNull;
import lombok.Value;

import static com.google.common.base.Preconditions.checkArgument;

@Value
public class DisplayedUsername {

    private final @NonNull String displayedUsername;

    public DisplayedUsername(@NonNull String displayedUsername) {
        checkArgument(!displayedUsername.isBlank(), "Displayed username should not be blank");
        checkArgument(
            displayedUsername.strip().equals(displayedUsername),
            "Displayed username should not have leading and trailing spaces"
        );
        this.displayedUsername = displayedUsername;
    }
}
