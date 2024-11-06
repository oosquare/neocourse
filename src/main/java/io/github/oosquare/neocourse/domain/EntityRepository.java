package io.github.oosquare.neocourse.domain;

import java.util.Optional;

import lombok.NonNull;

import io.github.oosquare.neocourse.utility.id.Id;

public interface EntityRepository<T extends Entity> {

    Optional<T> find(@NonNull Id id);

    void save(@NonNull T entity);

    void remove(@NonNull T entity);
}
