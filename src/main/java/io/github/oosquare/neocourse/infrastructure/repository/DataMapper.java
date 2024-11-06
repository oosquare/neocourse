package io.github.oosquare.neocourse.infrastructure.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public abstract class DataMapper<T> {

    @PersistenceContext
    private final @NonNull EntityManager entityManager;
    private final @NonNull Class<T> dataClass;

    public Optional<T> find(@NonNull String id) {
        var data = this.entityManager.find(this.dataClass, id);
        return Optional.ofNullable(data);
    }

    public void save(@NonNull T data) {
        this.entityManager.merge(data);
    }

    public void remove(@NonNull T data) {
        if (!this.entityManager.contains(data)) {
            data = this.entityManager.merge(data);
        }
        this.entityManager.remove(data);
    }
}
