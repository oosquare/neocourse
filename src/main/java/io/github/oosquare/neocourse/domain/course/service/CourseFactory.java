package io.github.oosquare.neocourse.domain.course.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import io.github.oosquare.neocourse.domain.course.exception.CreateCourseException;
import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.course.model.CourseName;
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
        this.courseRepository.findByName(name)
            .ifPresent(course -> {
                throw new CreateCourseException(
                    String.format(
                        "Course[id=%s, name=%s] already exists",
                        course.getId(),
                        course.getName()
                    )
                );
            });
    }
}
