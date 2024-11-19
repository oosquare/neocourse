package io.github.oosquare.neocourse.domain.teacher.service;

import java.util.Optional;

import lombok.NonNull;

import io.github.oosquare.neocourse.domain.EntityRepository;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.teacher.model.Teacher;

public interface TeacherRepository extends EntityRepository<Teacher> {

    Optional<Teacher> findByUsername(@NonNull Username username);

    @Override
    default Class<Teacher> getEntityClass() {
        return Teacher.class;
    }
}
