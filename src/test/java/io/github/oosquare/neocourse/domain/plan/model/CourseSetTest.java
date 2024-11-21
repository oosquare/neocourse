package io.github.oosquare.neocourse.domain.plan.model;

import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;

class CourseSetTest {

    @Test
    public void addCourseSucceeds() {
        var courseSet = CourseSet.of();
        var newCourseSet = courseSet.addCourse(Id.of("0"));
        assertTrue(newCourseSet.getCourses().contains(Id.of("0")));
    }

    @Test
    void removeCourseSucceeds() {
        var courseSet = CourseSet.ofInternally(Set.of(Id.of("0")));
        var newCourseSet = courseSet.removeCourse(Id.of("0"));
        assertFalse(newCourseSet.getCourses().contains(Id.of("0")));
    }
}