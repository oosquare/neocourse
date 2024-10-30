package io.github.oosquare.neocourse.domain.common.model;

import java.util.regex.Pattern;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.Value;

@Value
public class Username {

    private static Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z0-9\\-_]+");
    private String username;

    public Username(@NonNull String username) {
        Preconditions.checkArgument(!username.isBlank(), "Username should not be blank");
        Preconditions.checkArgument(
            USERNAME_PATTERN.matcher(username).matches(),
            "Username should only contains ASCII alphabets, numbers, hyphens and underscores"
        );
        this.username = username;
    }
}
