package io.github.oosquare.neocourse.domain.student.service;

import java.util.Optional;

import lombok.NonNull;

import io.github.oosquare.neocourse.domain.EntityRepository;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.student.model.Student;

public interface StudentRepository extends EntityRepository<Student> {

    Optional<Student> findByUsername(@NonNull Username username);
}
