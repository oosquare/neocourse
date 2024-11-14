package io.github.oosquare.neocourse.domain.account.model;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.Value;

@Value
public class EncodedPassword {

    private final @NonNull String value;

    private EncodedPassword(@NonNull String value) {
        Preconditions.checkArgument(!value.isBlank(), "Encoded password should not be blank");
        this.value = value;
    }

    public static EncodedPassword of(@NonNull String value) {
        return new EncodedPassword(value);
    }

    public static EncodedPassword ofInternally(@NonNull String value) {
        return EncodedPassword.of(value);
    }
}
