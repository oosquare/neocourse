package io.github.oosquare.neocourse.domain.course.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.course.model.CourseName;
import io.github.oosquare.neocourse.utility.exception.FieldDuplicationException;
import io.github.oosquare.neocourse.utility.id.IdGenerator;

@Service
@AllArgsConstructor
public class CourseFactory {

    private final @NonNull IdGenerator idGenerator;
    private final @NonNull CourseRepository courseRepository;

    public Course createCourse(@NonNull CourseName name, @NonNull ClassPeriod classPeriod) {
        this.checkCourseNotDuplicated(name);
        var id = this.idGenerator.generate();
        return new Course(id, name, classPeriod);
    }

    private void checkCourseNotDuplicated(CourseName name) {
        this.courseRepository.findByName(name).ifPresent(course -> {
            throw FieldDuplicationException.builder()
                .message("CourseName is duplicated and conflicted with another course's")
                .userMessage("Course's name is duplicated since a course with the same name already exists")
                .context("name", name)
                .context("course.id", course.getId())
                .build();
        });
    }
}
