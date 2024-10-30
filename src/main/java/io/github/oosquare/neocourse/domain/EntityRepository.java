package io.github.oosquare.neocourse.domain;

import java.util.Optional;

import io.github.oosquare.neocourse.utility.id.Id;

public interface EntityRepository<T extends Entity> {

    Optional<T> find(Id id);

    void save(T entity);

    void delete(T entity);
}
