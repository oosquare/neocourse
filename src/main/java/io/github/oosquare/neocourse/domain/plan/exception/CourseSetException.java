package io.github.oosquare.neocourse.domain.plan.exception;

public class CourseSetException extends RuntimeException{

    public CourseSetException() {
    }

    public CourseSetException(String message) {
        super(message);
    }

    public CourseSetException(String message, Throwable cause) {
        super(message, cause);
    }
}
