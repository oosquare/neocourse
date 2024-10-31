package io.github.oosquare.neocourse.utility.id;

import lombok.*;

@Value
@AllArgsConstructor(staticName = "of")
public class Id {

    @NonNull
    private String value;
}
