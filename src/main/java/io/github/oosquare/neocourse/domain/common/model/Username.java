package io.github.oosquare.neocourse.domain.common.model;

import java.util.regex.Pattern;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@ToString(includeFieldNames = false)
public class Username {

    private static Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z0-9\\-_]+");
    private String value;

    private Username(String value) {
        Preconditions.checkArgument(!value.isBlank(), "Username should not be blank");
        Preconditions.checkArgument(
            USERNAME_PATTERN.matcher(value).matches(),
            "Username should only contains ASCII alphabets, numbers, hyphens and underscores"
        );
        this.value = value;
    }

    public static Username of(@NonNull String value) {
        return new Username(value);
    }

    public static Username ofInternally(@NonNull String value) {
        return Username.of(value);
    }
}
