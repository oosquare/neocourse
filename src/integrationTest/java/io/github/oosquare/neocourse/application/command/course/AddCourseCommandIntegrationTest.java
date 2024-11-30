package io.github.oosquare.neocourse.application.command.course;

import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.CourseName;
import io.github.oosquare.neocourse.domain.course.service.CourseRepository;
import io.github.oosquare.neocourse.domain.teacher.service.TeacherRepository;
import io.github.oosquare.neocourse.utility.test.InitializeTeacherTestSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Getter
@SpringBootTest
@Transactional
public class AddCourseCommandIntegrationTest implements InitializeTeacherTestSupport {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private AccountRepository accountRepository;
    @SpyBean
    private CourseRepository courseRepository;
    @Autowired
    private CourseCommandService courseCommandService;

    @BeforeEach
    public void resetSpy() {
        reset(this.courseRepository);
    }

    @Test
    public void addCourseSucceeds() {
        var command = AddCourseCommand.builder()
            .courseName(CourseName.of("Test Course"))
            .classPeriod(ClassPeriod.of(1))
            .build();
        var account = this.createTestTeacherAccount();

        this.courseCommandService.addCourse(command, account);

        verify(this.courseRepository).save(assertArg(course -> {
            var res = this.courseRepository.findOrThrow(course.getId());
            assertEquals(CourseName.of("Test Course"), res.getName());
            assertEquals(ClassPeriod.of(1), res.getClassPeriod());
        }));
    }
}
