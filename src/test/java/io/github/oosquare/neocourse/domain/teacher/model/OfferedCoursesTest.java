package io.github.oosquare.neocourse.domain.teacher.model;

import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.oosquare.neocourse.domain.teacher.exception.OfferedCourseException;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;

class OfferedCoursesTest {

    @Test
    public void addCourseSucceeds() {
        var offeredCourses = OfferedCourses.of();
        var newOfferedCourses = offeredCourses.addCourse(Id.of("0"));
        assertTrue(newOfferedCourses.getOfferedCourses().contains(Id.of("0")));
    }

    @Test
    public void addCourseThrowsWhenCourseDuplicated() {
        var offeredCourses = OfferedCourses.ofInternally(Set.of(Id.of("0")));
        assertThrows(OfferedCourseException.class, () -> offeredCourses.addCourse(Id.of("0")));
    }

    @Test
    void removeCourseSucceeds() {
        var offeredCourses = OfferedCourses.ofInternally(Set.of(Id.of("0")));
        var newOfferedCourses = offeredCourses.removeCourse(Id.of("0"));
        assertFalse(newOfferedCourses.getOfferedCourses().contains(Id.of("0")));
    }

    @Test
    void removeCourseThrowsWhenCourseNotExisted() {
        var offeredCourses = OfferedCourses.of();
        assertThrows(OfferedCourseException.class, () -> offeredCourses.removeCourse(Id.of("0")));
    }
}