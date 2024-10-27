package io.github.oosquare.neocourse.utility.id;

public interface IdGenerator {

    default Id generate() {
        return Id.createWith(this);
    }

    String generateIdValue();
}
