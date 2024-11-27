package io.github.oosquare.neocourse.utility.test;

import org.junit.jupiter.api.BeforeEach;

import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.course.model.CourseName;
import io.github.oosquare.neocourse.domain.course.service.CourseRepository;
import io.github.oosquare.neocourse.utility.id.Id;

public interface InitializeCourseTestSupport {

    CourseRepository getCourseRepository();

    default Course createTestCourse() {
        return new Course(Id.of("test-course"), CourseName.of("Test Course"), ClassPeriod.of(2));
    }

    @BeforeEach
    default void setUpDefaultCourse() {
        this.getCourseRepository().save(this.createTestCourse());
    }
}
