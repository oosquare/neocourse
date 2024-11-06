package io.github.oosquare.neocourse.infrastructure.repository;

import lombok.NonNull;

public interface DataConverter<E, D> {

    E convertToDomain(@NonNull D data);

    D convertToData(@NonNull E entity);
}
