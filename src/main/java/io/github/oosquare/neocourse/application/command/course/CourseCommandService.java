package io.github.oosquare.neocourse.application.command.course;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.model.AccountKind;
import io.github.oosquare.neocourse.domain.common.service.UserService;
import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.course.model.CourseName;
import io.github.oosquare.neocourse.domain.course.service.CourseRepository;
import io.github.oosquare.neocourse.domain.course.service.CourseService;
import io.github.oosquare.neocourse.utility.exception.EntityNotFoundException;
import io.github.oosquare.neocourse.utility.id.Id;

@Service
@AllArgsConstructor
@Slf4j
public class CourseCommandService {

    private final @NonNull UserService userService;
    private final @NonNull CourseRepository courseRepository;
    private final @NonNull CourseService courseService;

    @Transactional
    public void removeCourse(@NonNull AddCourseCommand command) {
        var courseName = CourseName.of(command.getCourseName());
        var classPeriod = ClassPeriod.of(command.getClassPeriod());
        var account = command.getAccount();

        this.userService.checkIsUser(account, AccountKind.ADMINISTRATOR);
        var course = this.courseService.addCourse(courseName, classPeriod);
        this.courseRepository.save(course);

        log.info(
            "Created Course[id={}, name={}] by Administrator[id={}, username={}]",
            course.getId(),
            course.getName(),
            account.getId(),
            account.getUsername()
        );
    }

    @Transactional
    public void removeCourse(@NonNull RemoveCourseCommand command) {
        var courseId = Id.of(command.getCourseId());
        var account = command.getAccount();

        this.userService.checkIsUser(account, AccountKind.ADMINISTRATOR);
        var course = this.courseRepository.findOrThrow(courseId);

        this.courseService.prepareRemovingCourse(course);
        this.courseRepository.remove(course);

        log.info(
            "Removed Course[id={}, name={}] by Administrator[id={}, username={}]",
            course.getId(),
            course.getName(),
            account.getId(),
            account.getUsername()
        );

    }
}
