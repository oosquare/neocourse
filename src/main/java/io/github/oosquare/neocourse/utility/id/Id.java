package io.github.oosquare.neocourse.utility.id;

import lombok.*;

@Value
public class Id {

    @NonNull
    private String value;

    public static Id createWith(IdGenerator idGenerator) {
        return new Id(idGenerator.generateIdValue());
    }
}
