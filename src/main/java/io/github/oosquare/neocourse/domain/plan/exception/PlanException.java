package io.github.oosquare.neocourse.domain.plan.exception;

public class PlanException extends RuntimeException {

    public PlanException(String message) {
        super(message);
    }

    public PlanException(String message, Throwable cause) {
        super(message, cause);
    }
}
