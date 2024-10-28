package io.github.oosquare.neocourse.domain.teacher.exception;

public class OfferedCourseException extends RuntimeException {

    public OfferedCourseException() {}

    public OfferedCourseException(String message) {
        super(message);
    }

    public OfferedCourseException(String message, Throwable cause) {
        super(message, cause);
    }
}
