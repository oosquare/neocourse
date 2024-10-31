package io.github.oosquare.neocourse.domain.course.service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.oosquare.neocourse.domain.course.exception.CreateCourseException;
import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.course.model.CourseName;
import io.github.oosquare.neocourse.utility.id.Id;
import io.github.oosquare.neocourse.utility.id.IdGenerator;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CourseFactoryTest {

    private @Mock IdGenerator idGenerator;
    private @Mock CourseRepository courseRepository;
    private @InjectMocks CourseFactory courseFactory;

    @Test
    void createCourseSucceeds() {
        when(this.idGenerator.generate())
            .thenReturn(new Id("0"));
        when(this.courseRepository.findByName(any()))
            .thenReturn(Optional.empty());

        var course = this.courseFactory.createCourse(CourseName.of("test course"), ClassPeriod.of(1));
        assertEquals(new Id("0"), course.getId());
    }

    @Test
    void createCourseThrowsWhenCourseNameIsDuplicated() {
        when(this.courseRepository.findByName(CourseName.of("test course")))
            .thenReturn(Optional.of(new Course(
                new Id("0"),
                CourseName.of("test course"),
                ClassPeriod.of(1)
            )));

        assertThrows(CreateCourseException.class, () -> {
            this.courseFactory.createCourse(CourseName.of("test course"), ClassPeriod.of(2));
        });
    }
}