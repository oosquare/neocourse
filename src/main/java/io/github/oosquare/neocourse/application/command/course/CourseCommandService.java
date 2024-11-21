package io.github.oosquare.neocourse.application.command.course;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.domain.account.model.AccountKind;
import io.github.oosquare.neocourse.domain.common.service.UserService;
import io.github.oosquare.neocourse.domain.course.service.CourseRepository;
import io.github.oosquare.neocourse.domain.course.service.CourseService;

@Service
@AllArgsConstructor
@Slf4j
public class CourseCommandService {

    private final @NonNull UserService userService;
    private final @NonNull CourseRepository courseRepository;
    private final @NonNull CourseService courseService;

    @Transactional
    public void addCourse(@NonNull AddCourseCommand command, @NonNull Account account) {
        log.info("{} requests addCourse with {}", account.toLoggingString(), command);

        var courseName = command.getCourseName();
        var classPeriod = command.getClassPeriod();

        this.userService.checkIsUser(account, AccountKind.ADMINISTRATOR);
        var course = this.courseService.addCourse(courseName, classPeriod);
        this.courseRepository.save(course);

        log.info(
            "Added Course[id={}, name={}] by {}",
            course.getId(),
            course.getName(),
            account.toLoggingString()
        );
    }

    @Transactional
    public void removeCourse(@NonNull RemoveCourseCommand command, @NonNull Account account) {
        log.info("{} requests removeCourse with {}", account.toLoggingString(), command);

        var courseId = command.getCourseId();

        this.userService.checkIsUser(account, AccountKind.ADMINISTRATOR);
        var course = this.courseRepository.findOrThrow(courseId);
        this.courseService.prepareRemovingCourse(course);
        this.courseRepository.remove(course);

        log.info(
            "Removed Course[id={}, name={}] by {}",
            course.getId(),
            course.getName(),
            account.toLoggingString()
        );
    }
}
