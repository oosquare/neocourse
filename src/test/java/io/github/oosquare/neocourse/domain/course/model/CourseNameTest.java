package io.github.oosquare.neocourse.domain.course.model;

import org.junit.jupiter.api.Test;

import io.github.oosquare.neocourse.utility.exception.ValueValidationException;

import static org.junit.jupiter.api.Assertions.*;

class CourseNameTest {

    @Test
    public void succeedWhenCourseNameIsValid() {
        var displayedUsername = CourseName.of("Example Course 1");
        assertEquals("Example Course 1", displayedUsername.getValue());
    }

    @Test
    public void throwWhenCourseNameIsBlank() {
        assertThrows(ValueValidationException.class, () -> CourseName.of(""));
        assertThrows(ValueValidationException.class, () -> CourseName.of(" "));
    }

    @Test
    public void throwWhenCourseNameHasLeadingOrTrailingSpaces() {
        assertThrows(ValueValidationException.class, () -> CourseName.of(" leading-space"));
        assertThrows(ValueValidationException.class, () -> CourseName.of("trailing-space "));
        assertThrows(ValueValidationException.class, () -> CourseName.of(" both "));
    }
}