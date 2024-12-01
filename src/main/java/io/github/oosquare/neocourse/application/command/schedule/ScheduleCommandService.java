package io.github.oosquare.neocourse.application.command.schedule;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.service.AccountService;
import io.github.oosquare.neocourse.domain.course.service.CourseRepository;
import io.github.oosquare.neocourse.domain.schedule.service.ScheduleRepository;
import io.github.oosquare.neocourse.domain.schedule.service.ScheduleService;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduleCommandService {

    private final @NonNull AccountService accountService;
    private final @NonNull ScheduleService scheduleService;

    private final @NonNull ScheduleRepository scheduleRepository;
    private final @NonNull CourseRepository courseRepository;

    @Transactional
    public void addSchedule(@NonNull AddScheduleCommand command, @NonNull Account account) {
        log.info("{} requests addSchedule with {}", account.toLoggingString(), command);

        var courseId = command.getCourseId();
        var startTime = command.getStartTime();
        var place = command.getPlace();
        var capacity = command.getCapacity();

        var teacher = this.accountService.getTeacherUser(account);
        var course = this.courseRepository.findOrThrow(courseId);
        var schedule = this.scheduleService.addNewSchedule(course, teacher, startTime, place, capacity);
        this.scheduleRepository.save(schedule);

        log.info(
            "Added Schedule[id={}, course={}, teacher={}] by {}",
            schedule.getId(),
            schedule.getCourse(),
            schedule.getTeacher(),
            account.toLoggingString()
        );
    }

    @Transactional
    public void removeSchedule(@NonNull RemoveScheduleCommand command, @NonNull Account account) {
        log.info("{} requests removeSchedule with {}", account.toLoggingString(), command);

        var scheduleId = command.getScheduleId();

        var teacher = this.accountService.getTeacherUser(account);
        var schedule = this.scheduleRepository.findOrThrow(scheduleId);
        this.scheduleService.prepareRemovingSchedule(schedule, teacher);
        this.scheduleRepository.remove(schedule);

        log.info(
            "Removed Schedule[id={}, teacher={}] by {}",
            schedule.getId(),
            schedule.getTeacher(),
            account.toLoggingString()
        );
    }
}
