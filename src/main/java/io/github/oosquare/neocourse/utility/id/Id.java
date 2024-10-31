package io.github.oosquare.neocourse.utility.id;

import lombok.*;

@Value
@AllArgsConstructor(staticName = "of")
@ToString(includeFieldNames = false)
public class Id {

    @NonNull
    private String value;
}
