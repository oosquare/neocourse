package io.github.oosquare.neocourse.domain;

import java.util.Optional;

import lombok.NonNull;

import io.github.oosquare.neocourse.domain.common.exception.NoEntityException;
import io.github.oosquare.neocourse.utility.id.Id;

public interface EntityRepository<T extends Entity> {

    Optional<T> find(@NonNull Id id);

    default T findOrThrow(@NonNull Id id) {
        return this.find(id).orElseThrow(() ->
            new NoEntityException(String.format(
                "Could not find Entity[id=%s] in %s",
                id,
                this.getClass().getSimpleName()
            )));
    }

    void save(@NonNull T entity);

    void remove(@NonNull T entity);
}
