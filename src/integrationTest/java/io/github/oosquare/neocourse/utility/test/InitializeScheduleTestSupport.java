package io.github.oosquare.neocourse.utility.test;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;

import io.github.oosquare.neocourse.domain.schedule.model.Capacity;
import io.github.oosquare.neocourse.domain.schedule.model.Place;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;
import io.github.oosquare.neocourse.domain.schedule.service.ScheduleRepository;
import io.github.oosquare.neocourse.utility.id.Id;

public interface InitializeScheduleTestSupport
    extends
        InitializeCourseTestSupport,
        InitializeTeacherTestSupport {

    ZonedDateTime TEST_START_TIME = ZonedDateTime.now().plusDays(1);

    ScheduleRepository getScheduleRepository();

    default Schedule createTestSchedule() {
        return new Schedule(
            Id.of("test-schedule"),
            this.createTestCourse(),
            this.createTestTeacher(),
            TEST_START_TIME,
            Place.of("Test Place"),
            Capacity.of(5)
        );
    }

    @BeforeEach
    default void setUpDefaultSchedule() {
        this.getScheduleRepository().save(this.createTestSchedule());
    }
}
