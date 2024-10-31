package io.github.oosquare.neocourse.utility.id;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class UuidIdGenerator implements IdGenerator {

    @Override
    public Id generate() {
        return Id.of(UUID.randomUUID().toString());
    }
}
