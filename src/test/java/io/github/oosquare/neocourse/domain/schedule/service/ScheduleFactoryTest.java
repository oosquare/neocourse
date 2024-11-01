package io.github.oosquare.neocourse.domain.schedule.service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.course.model.CourseName;
import io.github.oosquare.neocourse.domain.schedule.exception.CreateScheduleException;
import io.github.oosquare.neocourse.domain.schedule.model.Capacity;
import io.github.oosquare.neocourse.domain.schedule.model.Place;
import io.github.oosquare.neocourse.domain.schedule.model.Registration;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;
import io.github.oosquare.neocourse.domain.schedule.model.TimeRange;
import io.github.oosquare.neocourse.domain.teacher.model.Teacher;
import io.github.oosquare.neocourse.utility.id.Id;
import io.github.oosquare.neocourse.utility.id.IdGenerator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleFactoryTest {

    private static final ZonedDateTime TEST_BASE_TIME = ZonedDateTime.ofInstant(
        Instant.ofEpochSecond(0),
        ZoneId.of("Z")
    );
    private static final Duration TEST_PERIOD = Duration.ofMinutes(45);

    private @Mock IdGenerator idGenerator;
    private @Mock ScheduleRepository scheduleRepository;
    private @InjectMocks ScheduleFactory scheduleFactory;

    @ParameterizedTest
    @ValueSource(ints = {0, 90, 181, 280})
    void createScheduleSucceeds(int startMinutes) {
        var course = new Course(Id.of("course0"), CourseName.of("test course"), ClassPeriod.of(1));
        var teacher = new Teacher(
            Id.of("teacher0"),
            Username.of("test-teacher"),
            DisplayedUsername.of("test teacher")
        );
        var startTime = TEST_BASE_TIME.plusMinutes(startMinutes);
        var place = Place.of("test place");
        var capacity = Capacity.of(2);

        when(this.idGenerator.generate())
            .thenReturn(Id.of("0"));
        when(this.scheduleRepository.findByDateAndPlace(startTime, place))
            .thenReturn(List.of(
                createTestSchedule(0, 45),
                createTestSchedule(1, 135),
                createTestSchedule(2, 230)
            ));

        var schedule = this.scheduleFactory.createSchedule(course, teacher, startTime, place, capacity);
        assertEquals(Id.of("0"), schedule.getId());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 44})
    void createScheduleThrowsWhenTimeRangeConflicts(int startMinutes) {
        var course = new Course(Id.of("course0"), CourseName.of("test course"), ClassPeriod.of(1));
        var teacher = new Teacher(
            Id.of("teacher0"),
            Username.of("test-teacher"),
            DisplayedUsername.of("test teacher")
        );
        var startTime = TEST_BASE_TIME.plusMinutes(startMinutes);
        var place = Place.of("test place");
        var capacity = Capacity.of(2);

        when(this.scheduleRepository.findByDateAndPlace(startTime, place))
            .thenReturn(List.of(createTestSchedule(0, 45)));

        assertThrows(CreateScheduleException.class, () -> {
            this.scheduleFactory.createSchedule(course, teacher, startTime, place, capacity);
        });
    }

    private static Schedule createTestSchedule(int scheduleId, int startMinutes) {
        var registrations = new HashMap<Id, Registration>();

        return Schedule.createInternally(
            Id.of(String.format("schedule%s", scheduleId)),
            Id.of("course0"),
            Id.of("teacher0"),
            TimeRange.of(TEST_BASE_TIME.plusMinutes(startMinutes), TEST_PERIOD),
            Place.of("test place"),
            Capacity.of(2),
            registrations
        );
    }
}