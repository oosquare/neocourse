package io.github.oosquare.neocourse.utility.id;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
@ToString(includeFieldNames = false)
public class Id {

    @NonNull
    private String value;
}
