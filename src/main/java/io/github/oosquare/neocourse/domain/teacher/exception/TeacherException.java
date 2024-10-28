package io.github.oosquare.neocourse.domain.teacher.exception;

public class TeacherException extends RuntimeException {

    public TeacherException() {
    }

    public TeacherException(String message) {
        super(message);
    }

    public TeacherException(String message, Throwable cause) {
        super(message, cause);
    }
}
