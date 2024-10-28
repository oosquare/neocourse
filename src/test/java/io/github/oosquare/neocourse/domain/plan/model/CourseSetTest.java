package io.github.oosquare.neocourse.domain.plan.model;

import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.oosquare.neocourse.domain.plan.exception.CourseSetException;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;

class CourseSetTest {

    @Test
    public void addCourseSucceeds() {
        var courseSet = new CourseSet();
        var newCourseSet = courseSet.addCourse(new Id("0"));
        assertTrue(newCourseSet.getCourses().contains(new Id("0")));
    }

    @Test
    public void addCourseThrowsWhenCourseDuplicated() {
        var courseSet = new CourseSet(Set.of(new Id("0")));
        assertThrows(CourseSetException.class, () -> courseSet.addCourse(new Id("0")));
    }

    @Test
    void removeCourseSucceeds() {
        var courseSet = new CourseSet(Set.of(new Id("0")));
        var newCourseSet = courseSet.removeCourse(new Id("0"));
        assertFalse(newCourseSet.getCourses().contains(new Id("0")));
    }

    @Test
    void removeCourseThrowsWhenCourseNotExisted() {
        var courseSet = new CourseSet();
        assertThrows(CourseSetException.class, () -> courseSet.removeCourse(new Id("0")));
    }
}