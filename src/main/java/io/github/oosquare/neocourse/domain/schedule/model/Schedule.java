package io.github.oosquare.neocourse.domain.schedule.model;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import io.github.oosquare.neocourse.domain.Entity;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.student.model.Student;
import io.github.oosquare.neocourse.domain.teacher.model.Teacher;
import io.github.oosquare.neocourse.utility.id.Id;

@Getter
@AllArgsConstructor(staticName = "createInternally")
public class Schedule implements Entity {

    private final @NonNull Id id;
    private final @NonNull Id course;
    private final @NonNull Id teacher;
    private final @NonNull TimeRange time;
    private final @NonNull Place place;
    private final @NonNull Capacity capacity;
    private final @NonNull Map<Id, Registration> registrations;

    public Schedule(
        @NonNull Id id,
        @NonNull Course course,
        @NonNull Teacher teacher,
        @NonNull ZonedDateTime startTime,
        @NonNull Place place,
        @NonNull Capacity capacity
    ) {
        this.id = id;
        this.course = course.getId();
        this.teacher = teacher.getId();
        this.time = TimeRange.of(startTime, course.getActualPeriod());
        this.place = place;
        this.capacity = capacity;
        this.registrations = new HashMap<>();
    }

    public void requestRegistration(@NonNull Student student, @NonNull ZonedDateTime currentTime) {
        new RegistrationSpecification(
            this.getId(),
            this.getTime().getStart(),
            this.getCapacity()
        ).checkRegistrable(this.getRegistrations(), student.getId(), currentTime);
        var registration = Registration.of(student.getId());
        this.registrations.put(student.getId(), registration);
    }

    public void cancelRegistration(@NonNull Student student, @NonNull ZonedDateTime currentTime) {
        new RegistrationSpecification(
            this.getId(),
            this.getTime().getStart(),
            this.getCapacity()
        ).checkCancellable(this.getRegistrations(), student.getId(), currentTime);
        this.registrations.remove(student.getId());
    }

    public boolean isStudentRegistered(@NonNull Student student) {
        return this.registrations.containsKey(student.getId());
    }

    public void markStudentAttended(@NonNull Student student) {
        new EvaluationSpecification(
            this.getId(),
            this.getRegistrations()
        ).checkStudentEvaluable(student.getId());
        this.registrations.computeIfPresent(
            student.getId(),
            (id, registration) -> registration.markAttended()
        );
    }

    public void markStudentAbsent(@NonNull Student student) {
        new EvaluationSpecification(
            this.getId(),
            this.getRegistrations()
        ).checkStudentEvaluable(student.getId());
        this.registrations.computeIfPresent(
            student.getId(),
            (id, registration) -> registration.markAbsent()
        );
    }

    public Optional<ParticipationStatus> getStudentParticipationStatus(@NonNull Student student) {
        return Optional.ofNullable(this.registrations.get(student.getId()))
            .map(Registration::getStatus);
    }
}
