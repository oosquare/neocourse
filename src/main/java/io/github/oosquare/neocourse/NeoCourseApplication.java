package io.github.oosquare.neocourse;

import java.time.Clock;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.admin.model.Administrator;
import io.github.oosquare.neocourse.domain.admin.service.AdministratorRepository;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.student.model.Student;
import io.github.oosquare.neocourse.domain.student.service.StudentRepository;
import io.github.oosquare.neocourse.domain.teacher.model.Teacher;
import io.github.oosquare.neocourse.domain.teacher.service.TeacherRepository;
import io.github.oosquare.neocourse.utility.id.Id;

@SpringBootApplication
@Push
public class NeoCourseApplication
    implements AppShellConfigurator, CommandLineRunner {

    private AdministratorRepository administratorRepository;
    private TeacherRepository teacherRepository;
    private StudentRepository studentRepository;

    @Autowired(required = false)
    public void setAdministratorRepository(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    @Autowired(required = false)
    public void setTeacherRepository(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Autowired(required = false)
    public void setStudentRepository(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (administratorRepository != null) {
            administratorRepository.save(Administrator.builder()
                .id(Id.of("test-user"))
                .username(Username.of("test-user"))
                .displayedUsername(DisplayedUsername.of("Test Administrator"))
                .build());
        }

        if (teacherRepository != null) {
            teacherRepository.save(Teacher.builder()
                .id(Id.of("test-teacher"))
                .username(Username.of("test-teacher"))
                .displayedUsername(DisplayedUsername.of("Test Teacher"))
                .build());
        }

        if (studentRepository != null) {
            studentRepository.save(Student.builder()
                .id(Id.of("test-student"))
                .username(Username.of("test-student"))
                .displayedUsername(DisplayedUsername.of("Test Student"))
                .plan(Id.of("plan"))
                .transcript(Id.of("transcript"))
                .build());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(NeoCourseApplication.class, args);
    }
}
