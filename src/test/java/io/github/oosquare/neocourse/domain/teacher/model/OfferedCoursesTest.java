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
        var newOfferedCourses = offeredCourses.addCourse(new Id("0"));
        assertTrue(newOfferedCourses.getOfferedCourses().contains(new Id("0")));
    }

    @Test
    public void addCourseThrowsWhenCourseDuplicated() {
        var offeredCourses = OfferedCourses.ofInternally(Set.of(new Id("0")));
        assertThrows(OfferedCourseException.class, () -> offeredCourses.addCourse(new Id("0")));
    }

    @Test
    void removeCourseSucceeds() {
        var offeredCourses = OfferedCourses.ofInternally(Set.of(new Id("0")));
        var newOfferedCourses = offeredCourses.removeCourse(new Id("0"));
        assertFalse(newOfferedCourses.getOfferedCourses().contains(new Id("0")));
    }

    @Test
    void removeCourseThrowsWhenCourseNotExisted() {
        var offeredCourses = OfferedCourses.of();
        assertThrows(OfferedCourseException.class, () -> offeredCourses.removeCourse(new Id("0")));
    }
}