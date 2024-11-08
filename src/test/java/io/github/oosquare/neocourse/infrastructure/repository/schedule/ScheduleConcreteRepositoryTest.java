package io.github.oosquare.neocourse.infrastructure.repository.schedule;

import jakarta.persistence.EntityManager;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.CourseName;
import io.github.oosquare.neocourse.domain.schedule.model.Capacity;
import io.github.oosquare.neocourse.domain.schedule.model.Registration;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;
import io.github.oosquare.neocourse.domain.schedule.model.Place;
import io.github.oosquare.neocourse.domain.schedule.model.TimeRange;
import io.github.oosquare.neocourse.domain.schedule.service.ScheduleRepository;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.teacher.model.Teacher;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({
    ScheduleConcreteRepository.class,
    ScheduleMapper.class,
    ScheduleConverter.class,
    RegistrationConverter.class,
})
class ScheduleConcreteRepositoryTest {

    private static final ZonedDateTime TEST_BASE_TIME = ZonedDateTime.now()
        .toLocalDate()
        .atStartOfDay(ZoneId.systemDefault());

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private EntityManager entityManager;

    private Schedule testSchedule1;
    private Schedule testSchedule2;
    private Course testCourse;
    private Teacher testTeacher;
    private Place testPlace;

    @BeforeEach
    public void setUp() {
        this.testCourse = Course.builder()
            .id(Id.of("course1"))
            .name(CourseName.of("Test Course"))
            .classPeriod(ClassPeriod.of(1))
            .build();
        this.testTeacher = Teacher.builder()
            .id(Id.of("teacher1"))
            .username(Username.of("test-teacher"))
            .displayedUsername(DisplayedUsername.of("Test Teacher"))
            .managedSchedules(new HashSet<>(Set.of(Id.of("schedule1"))))
            .build();
        this.testPlace = Place.of("Test Place");
        this.testSchedule1 = Schedule.builder()
            .id(Id.of("schedule1"))
            .course(testCourse.getId())
            .teacher(testTeacher.getId())
            .time(TimeRange.of(TEST_BASE_TIME.plusHours(8), Duration.ofMinutes(45)))
            .place(testPlace)
            .capacity(Capacity.of(30))
            .registrations(new HashMap<>())
            .build();
        this.testSchedule2 = Schedule.builder()
            .id(Id.of("schedule2"))
            .course(testCourse.getId())
            .teacher(testTeacher.getId())
            .time(TimeRange.of(TEST_BASE_TIME.plusHours(10), Duration.ofMinutes(45)))
            .place(testPlace)
            .capacity(Capacity.of(30))
            .registrations(new HashMap<>())
            .build();

        this.scheduleRepository.save(this.testSchedule1);
        this.scheduleRepository.save(this.testSchedule2);

        this.entityManager.flush();
        System.out.println("Post setUp()");
    }

    @AfterEach
    public void tearDown() {
        this.entityManager.flush();
        System.out.println("Pre tearDown()");
        this.scheduleRepository.remove(this.testSchedule1);
        this.entityManager.flush();
    }

    @Test
    public void findWhenDataExists() {
        var schedule = this.scheduleRepository.find(this.testSchedule1.getId()).orElseThrow();
        assertEquals(this.testSchedule1.getId(), schedule.getId());
        assertEquals(this.testSchedule1.getCourse(), schedule.getCourse());
        assertEquals(this.testSchedule1.getTeacher(), schedule.getTeacher());
        assertEquals(this.testSchedule1.getTime(), schedule.getTime());
        assertEquals(this.testSchedule1.getPlace(), schedule.getPlace());
        assertEquals(this.testSchedule1.getCapacity(), schedule.getCapacity());
        assertEquals(this.testSchedule1.getRegistrations(), schedule.getRegistrations());
    }

    @Test
    public void findWhenDataDoesNotExist() {
        var res = this.scheduleRepository.find(Id.of("nonexistent"));
        assertTrue(res.isEmpty());
    }

    @Test
    public void findByCourseWhenDataExists() {
        var schedule = this.scheduleRepository.findByCourse(this.testCourse).orElseThrow();
        assertEquals(this.testSchedule1.getId(), schedule.getId());
        assertEquals(this.testCourse.getId(), schedule.getCourse());
    }

    @Test
    public void findByCourseWhenDataDoesNotExist() {
        var course = Course.builder()
            .id(Id.of("nonexistent"))
            .name(CourseName.of("Nonexistent Course"))
            .classPeriod(ClassPeriod.of(1))
            .build();
        var res = this.scheduleRepository.findByCourse(course);
        assertTrue(res.isEmpty());
    }

    @Test
    public void findByDateAndPlaceWhenDataExists() {
        var date = TEST_BASE_TIME.plusHours(9);
        var res = this.scheduleRepository.findByDateAndPlace(date, this.testPlace);
        assertFalse(res.isEmpty());
        assertTrue(res.stream().anyMatch(schedule -> this.testSchedule1.getId().equals(schedule.getId())));
        assertTrue(res.stream().anyMatch(schedule -> this.testSchedule2.getId().equals(schedule.getId())));
    }

    @Test
    public void findByDateAndPlaceWhenDataDoesNotExist() {
        var date = TEST_BASE_TIME.plusDays(1);
        var res = this.scheduleRepository.findByDateAndPlace(date, this.testPlace);
        assertTrue(res.isEmpty());
    }

    @Test
    public void save() {
        var updatedSchedule = Schedule.builder()
            .id(Id.of("schedule1"))
            .course(this.testCourse.getId())
            .teacher(this.testTeacher.getId())
            .time(TimeRange.of(TEST_BASE_TIME.plusHours(8), Duration.ofMinutes(45)))
            .place(this.testPlace)
            .capacity(Capacity.of(30))
            .registrations(new HashMap<>(Map.of(
                Id.of("student1"),
                Registration.of(Id.of("student1"))
            )))
            .build();
        this.scheduleRepository.save(updatedSchedule);

        var schedule = this.scheduleRepository.find(updatedSchedule.getId()).orElseThrow();
        assertTrue(schedule.getRegistrations().containsKey(Id.of("student1")));
    }

    @Test
    public void remove() {
        this.scheduleRepository.remove(this.testSchedule1);
        var res = this.scheduleRepository.find(this.testSchedule1.getId());
        assertTrue(res.isEmpty());
    }
}
