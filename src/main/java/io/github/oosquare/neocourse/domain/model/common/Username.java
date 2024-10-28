package io.github.oosquare.neocourse.domain.model.common;

import java.util.regex.Pattern;

import lombok.NonNull;
import lombok.Value;

import static com.google.common.base.Preconditions.checkArgument;

@Value
public class Username {

    private static Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z0-9\\-_]+");
    private String value;

    public Username(@NonNull String value) {
        checkArgument(!value.isBlank(), "Username should not be blank");
        checkArgument(
            USERNAME_PATTERN.matcher(value).matches(),
            "Username should only contains ASCII alphabets, numbers, hyphens and underscores"
        );
        this.value = value;
    }
}
