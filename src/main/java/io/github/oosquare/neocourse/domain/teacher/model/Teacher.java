package io.github.oosquare.neocourse.domain.teacher.model;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.User;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;
import io.github.oosquare.neocourse.domain.teacher.exception.TeacherException;
import io.github.oosquare.neocourse.utility.id.Id;

@Getter
@AllArgsConstructor
@Builder
public class Teacher implements User {

    private final @NonNull Id id;
    private final @NonNull Username username;
    private final @NonNull DisplayedUsername displayedUsername;
    private final @NonNull Set<Id> managedSchedules;

    public Teacher(
        @NonNull Id id,
        @NonNull Username username,
        @NonNull DisplayedUsername displayedUsername
    ) {
        this.id = id;
        this.username = username;
        this.displayedUsername = displayedUsername;
        this.managedSchedules = new HashSet<>();
    }

    public void addManagedSchedule(@NonNull Schedule schedule) {
        if (this.managedSchedules.contains(schedule.getId())) {
            throw new TeacherException(String.format(
                "Teacher[id=%s] has already been managing Schedule[id=%s]",
                this.getId(),
                schedule.getId()
            ));
        }
        if (!schedule.getTeacher().equals(this.getId())) {
            throw new TeacherException(String.format(
                "Teacher[id=%s] can't manage Schedule[id=%s] which is offered by Teacher[id=%s]",
                this.getId(),
                schedule.getId(),
                schedule.getTeacher()
            ));
        }
        this.managedSchedules.add(schedule.getId());
    }

    public void removeManagedSchedule(@NonNull Schedule schedule) {
        if (!this.managedSchedules.contains(schedule.getId())) {
            throw new TeacherException(String.format(
                "Teacher[id=%s] doesn't manage Schedule[id=%s]",
                this.getId(),
                schedule.getId()
            ));
        }
        this.managedSchedules.remove(schedule.getId());
    }

    public boolean isManagingSchedule(@NonNull Schedule schedule) {
        return this.managedSchedules.contains(schedule.getId());
    }
}
