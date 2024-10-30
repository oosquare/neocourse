package io.github.oosquare.neocourse.domain.common.model;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.Value;

@Value
public class DisplayedUsername {

    private final @NonNull String displayedUsername;

    public DisplayedUsername(@NonNull String displayedUsername) {
        Preconditions.checkArgument(
            !displayedUsername.isBlank(),
            "Displayed username should not be blank"
        );
        Preconditions.checkArgument(
            displayedUsername.strip().equals(displayedUsername),
            "Displayed username should not have leading and trailing spaces"
        );
        this.displayedUsername = displayedUsername;
    }
}
