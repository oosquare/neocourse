package io.github.oosquare.neocourse.domain.course.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseNameTest {

    @Test
    public void succeedWhenCourseNameIsValid() {
        var displayedUsername = new CourseName("Example Course 1");
        assertEquals("Example Course 1", displayedUsername.getCourseName());
    }

    @Test
    public void throwWhenCourseNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> new CourseName(""));
        assertThrows(IllegalArgumentException.class, () -> new CourseName(" "));
    }

    @Test
    public void throwWhenCourseNameHasLeadingOrTrailingSpaces() {
        assertThrows(IllegalArgumentException.class, () -> new CourseName(" leading-space"));
        assertThrows(IllegalArgumentException.class, () -> new CourseName("trailing-space "));
        assertThrows(IllegalArgumentException.class, () -> new CourseName(" both "));
    }
}