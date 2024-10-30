package io.github.oosquare.neocourse.domain.course.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseNameTest {

    @Test
    public void succeedWhenCourseNameIsValid() {
        var displayedUsername = CourseName.of("Example Course 1");
        assertEquals("Example Course 1", displayedUsername.getValue());
    }

    @Test
    public void throwWhenCourseNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> CourseName.of(""));
        assertThrows(IllegalArgumentException.class, () -> CourseName.of(" "));
    }

    @Test
    public void throwWhenCourseNameHasLeadingOrTrailingSpaces() {
        assertThrows(IllegalArgumentException.class, () -> CourseName.of(" leading-space"));
        assertThrows(IllegalArgumentException.class, () -> CourseName.of("trailing-space "));
        assertThrows(IllegalArgumentException.class, () -> CourseName.of(" both "));
    }
}