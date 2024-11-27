package io.github.oosquare.neocourse.application.command.course;

import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.service.AccountRepository;
import io.github.oosquare.neocourse.domain.admin.service.AdministratorRepository;
import io.github.oosquare.neocourse.domain.course.service.CourseRepository;
import io.github.oosquare.neocourse.utility.test.InitializeAdministratorTestSupport;
import io.github.oosquare.neocourse.utility.test.InitializeCourseTestSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Getter
@SpringBootTest
@Transactional
public class RemoveCourseCommandIntegrationTest
    implements InitializeAdministratorTestSupport, InitializeCourseTestSupport {

    @Autowired
    private AdministratorRepository administratorRepository;
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
    public void removeCourseSucceeds() {
        var course = this.createTestCourse();

        var command = RemoveCourseCommand.builder()
            .courseId(course.getId())
            .build();
        var account = this.createTestAdministratorAccount();

        this.courseCommandService.removeCourse(command, account);

        assertTrue(this.courseRepository.find(course.getId()).isEmpty());
    }
}
