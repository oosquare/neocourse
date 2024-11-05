package io.github.oosquare.neocourse.domain.course.service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.oosquare.neocourse.domain.course.exception.RemoveCourseException;
import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.course.model.CourseName;
import io.github.oosquare.neocourse.domain.plan.model.CourseSet;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;
import io.github.oosquare.neocourse.domain.plan.service.PlanRepository;
import io.github.oosquare.neocourse.domain.schedule.model.Capacity;
import io.github.oosquare.neocourse.domain.schedule.model.Place;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;
import io.github.oosquare.neocourse.domain.schedule.model.TimeRange;
import io.github.oosquare.neocourse.domain.schedule.service.ScheduleRepository;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    private @Mock PlanRepository planRepository;
    private @Mock ScheduleRepository scheduleRepository;
    private @Mock CourseFactory courseFactory;
    private @InjectMocks CourseService courseService;

    @Test
    void addCourse() {
        when(this.courseFactory.createCourse(CourseName.of("course 0"), ClassPeriod.of(1)))
            .thenReturn(createTestCourse());
        var course = this.courseService.addCourse(CourseName.of("course 0"), ClassPeriod.of(1));
        assertEquals(Id.of("course0"), course.getId());
    }

    @Test
    void prepareRemovingCourseSucceeds() {
        when(this.planRepository.findByIncludedCourse(any()))
            .thenReturn(Optional.empty());
        when(this.scheduleRepository.findByCourse(any()))
            .thenReturn(Optional.empty());

        this.courseService.prepareRemovingCourse(createTestCourse());
    }

    @Test
    void prepareRemovingCourseThrowsWhenIncludedInPlan() {
        var course = createTestCourse();
        when(this.planRepository.findByIncludedCourse(course))
            .thenReturn(Optional.of(Plan.createInternally(
                Id.of("plan0"),
                PlanName.of("plan 0"),
                CourseSet.ofInternally(Set.of(Id.of("course0")))
            )));

        assertThrows(RemoveCourseException.class, () -> {
            this.courseService.prepareRemovingCourse(course);
        });
    }

    @Test
    void prepareRemovingCourseThrowsWhenAssociatedWithSchedule() {
        var course = createTestCourse();
        when(this.scheduleRepository.findByCourse(course))
            .thenReturn(Optional.of(Schedule.createInternally(
                Id.of("schedule0"),
                Id.of("course0"),
                Id.of("teacher0"),
                TimeRange.of(ZonedDateTime.now(), Duration.ofMinutes(45)),
                Place.of("place0"),
                Capacity.of(1),
                new HashMap<>()
            )));

        assertThrows(RemoveCourseException.class, () -> {
            this.courseService.prepareRemovingCourse(course);
        });
    }

    private static Course createTestCourse() {
        return Course.createInternally(
            Id.of("course0"),
            CourseName.of("course 0"),
            ClassPeriod.of(1)
        );
    }
}