package io.github.oosquare.neocourse.utility.id;

import java.util.UUID;

public class UuidIdGenerator implements IdGenerator {

    @Override
    public String generateIdValue() {
        return UUID.randomUUID().toString();
    }
}
