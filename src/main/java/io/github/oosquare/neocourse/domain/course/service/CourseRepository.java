package io.github.oosquare.neocourse.domain.course.service;

import java.util.Optional;

import lombok.NonNull;

import io.github.oosquare.neocourse.domain.EntityRepository;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.course.model.CourseName;

public interface CourseRepository extends EntityRepository<Course> {

    Optional<Course> findByName(@NonNull CourseName name);
}
