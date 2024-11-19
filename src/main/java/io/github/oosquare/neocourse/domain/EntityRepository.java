package io.github.oosquare.neocourse.domain;

import java.util.Optional;

import lombok.NonNull;

import io.github.oosquare.neocourse.utility.exception.EntityNotFoundException;
import io.github.oosquare.neocourse.utility.id.Id;

public interface EntityRepository<T extends Entity> {

    Class<T> getEntityClass();

    Optional<T> find(@NonNull Id id);

    default T findOrThrow(@NonNull Id id) {
        return this.find(id).orElseThrow(() ->
            EntityNotFoundException.builder()
                .entity(this.getEntityClass())
                .context("id", id)
                .build());
    }

    void save(@NonNull T entity);

    void remove(@NonNull T entity);
}
