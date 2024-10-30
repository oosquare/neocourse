package io.github.oosquare.neocourse.domain.schedule.model;

import java.time.ZonedDateTime;
import java.util.Map;

import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.domain.schedule.exception.RegistrationException;
import io.github.oosquare.neocourse.utility.id.Id;

@Value
public class RegistrationSpecification {

    private final @NonNull Id schedule;
    private final @NonNull ZonedDateTime startTime;
    private final Capacity capacity;

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
        throw new RegistrationException(
            String.format(
                "Could not register for Schedule[id=%s] which is frozen since %s",
                this.schedule,
                this.startTime
            )
        );
    }

    private void checkRegistrationAvailable(Map<Id, Registration> registrations) {
        if (registrations.size() < this.capacity.getValue()) {
            return;
        }
        throw new RegistrationException(
            String.format(
                "Could not register for a full Schedule[id=%s, capacity=%s]",
                this.schedule,
                this.capacity
            )
        );
    }

    private void checkStudentNotRegistered(Map<Id, Registration> registrations, Id student) {
        if (!registrations.containsKey(student)) {
            return;
        }
        throw new RegistrationException(
            String.format(
                "Student[id=%s] has already registered for Schedule[id=%s]",
                student,
                this.schedule
            )
        );
    }

    private void checkStudentRegistered(Map<Id, Registration> registrations, Id student) {
        if (registrations.containsKey(student)) {
            return;
        }
        throw new RegistrationException(
            String.format(
                "Student[id=%s] hasn't registered for Schedule[id=%s] yet",
                student,
                this.schedule
            )
        );
    }
}
