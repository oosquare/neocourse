package io.github.oosquare.neocourse.domain.common;

import io.github.oosquare.neocourse.utility.id.Id;

public interface BaseRepository<T> {

    T find(Id id);

    void save(T entity);

    void delete(T entity);
}
