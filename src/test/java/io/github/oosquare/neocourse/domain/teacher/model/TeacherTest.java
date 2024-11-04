package io.github.oosquare.neocourse.domain.teacher.model;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.schedule.model.Capacity;
import io.github.oosquare.neocourse.domain.schedule.model.Place;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;
import io.github.oosquare.neocourse.domain.schedule.model.TimeRange;
import io.github.oosquare.neocourse.domain.teacher.exception.TeacherException;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    @Test
    void addManagedScheduleSucceeds() {
        var teacher = createTestTeacher();
        var schedule = createTestSchedule(0);
        teacher.addManagedSchedule(schedule);
        assertTrue(teacher.isManagingSchedule(schedule));
    }

    @Test
    void addManagedScheduleThrowsWhenScheduleIsAlreadyManaged() {
        var teacher = createTestTeacher();
        var schedule = createTestSchedule(0);
        teacher.addManagedSchedule(schedule);

        assertThrows(TeacherException.class, () -> {
            teacher.addManagedSchedule(schedule);
        });
    }

    @Test
    void addManagedScheduleThrowsWhenTeacherCanNotManageSchedule() {
        var teacher = createTestTeacher();
        var schedule = createTestSchedule(1);

        assertThrows(TeacherException.class, () -> {
            teacher.addManagedSchedule(schedule);
        });
    }

    @Test
    void removeManagedScheduleSucceeds() {
        var teacher = createTestTeacher();
        var schedule = createTestSchedule(0);
        teacher.addManagedSchedule(schedule);
        teacher.removeManagedSchedule(schedule);
        assertFalse(teacher.isManagingSchedule(schedule));
    }

    @Test
    void removeManagedScheduleThrowsWhenScheduleIsNotManaged() {
        var teacher = createTestTeacher();
        var schedule = createTestSchedule(0);

        assertThrows(TeacherException.class, () -> {
            teacher.removeManagedSchedule(schedule);
        });
    }

    private static Teacher createTestTeacher() {
        return new Teacher(
            Id.of("teacher0"),
            Username.of("teacher0"),
            DisplayedUsername.of("teacher 0")
        );
    }

    private static Schedule createTestSchedule(int teacherId) {
        return Schedule.createInternally(
            Id.of("schedule0"),
            Id.of("course0"),
            Id.of(String.format("teacher%d", teacherId)),
            TimeRange.of(ZonedDateTime.now(), Duration.ofSeconds(1)),
            Place.of("place0"),
            Capacity.of(1),
            new HashMap<>()
        );
    }
}