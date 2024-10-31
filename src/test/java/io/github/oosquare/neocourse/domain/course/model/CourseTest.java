package io.github.oosquare.neocourse.domain.course.model;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void getActualPeriod() {
        var course = new Course(Id.of("0"), CourseName.of("test"), ClassPeriod.of(1));
        assertEquals(Duration.ofMinutes(45), course.getActualPeriod());

        var course2 = new Course(Id.of("0"), CourseName.of("test"), ClassPeriod.of(3));
        assertEquals(Duration.ofMinutes(145), course2.getActualPeriod());
    }
}