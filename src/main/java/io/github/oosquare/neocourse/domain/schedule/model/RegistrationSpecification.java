package io.github.oosquare.neocourse.domain.schedule.model;

import java.time.ZonedDateTime;
import java.util.Map;

import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.utility.exception.RuleViolationException;
import io.github.oosquare.neocourse.utility.id.Id;

@Value
public class RegistrationSpecification {

    private final @NonNull Id schedule;
    private final @NonNull ZonedDateTime startTime;
    private final @NonNull Capacity capacity;

    public void checkRegistrable(
        @NonNull Map<Id, Registration> registrations,
        @NonNull Id student,
        @NonNull ZonedDateTime currentTime
    ) {
        this.checkScheduleNotFrozen(currentTime);
        this.checkRegistrationAvailable(registrations);
        this.checkStudentNotRegistered(registrations, student);
    }

    public void checkCancellable(
        @NonNull Map<Id, Registration> registrations,
        @NonNull Id student,
        @NonNull ZonedDateTime currentTime
    ) {
        this.checkScheduleNotFrozen(currentTime);
        this.checkStudentRegistered(registrations, student);
    }

    private void checkScheduleNotFrozen(ZonedDateTime currentTime) {
        if (currentTime.isBefore(this.startTime)) {
            return;
        }
        throw RuleViolationException.builder()
            .message("Frozen Schedule is not registrable")
            .userMessage("Could not register for a frozen schedule which already started at %s"
                .formatted(this.startTime))
            .context("schedule.id", this.schedule)
            .context("schedule.time.start", this.startTime)
            .context("currentTime", currentTime)
            .build();
    }

    private void checkRegistrationAvailable(Map<Id, Registration> registrations) {
        if (registrations.size() < this.capacity.getValue()) {
            return;
        }
        throw RuleViolationException.builder()
            .message("Full Schedule is not registrable")
            .userMessage("Could not register for a full schedule with a capacity of %d"
                .formatted(this.capacity.getValue()))
            .context("schedule.id", this.schedule)
            .context("schedule.capacity", this.capacity)
            .build();
    }

    private void checkStudentNotRegistered(Map<Id, Registration> registrations, Id student) {
        if (!registrations.containsKey(student)) {
            return;
        }
        throw RuleViolationException.builder()
            .message("Student already registered for this Schedule")
            .userMessage("Student already registered for this Schedule")
            .context("schedule.id", this.schedule)
            .context("student.id", student)
            .build();
    }

    private void checkStudentRegistered(Map<Id, Registration> registrations, Id student) {
        if (registrations.containsKey(student)) {
            return;
        }
        throw RuleViolationException.builder()
            .message("Student hasn't registered for this Schedule")
            .userMessage("Student hasn't registered for this Schedule")
            .context("schedule.id", this.schedule)
            .context("student.id", student)
            .build();
    }
}
