package io.github.oosquare.neocourse.infrastructure.repository.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import io.github.oosquare.neocourse.domain.admin.model.Administrator;
import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.student.model.Student;
import io.github.oosquare.neocourse.domain.student.service.StudentRepository;
import io.github.oosquare.neocourse.utility.annotation.InfrastructureTestTag;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({
    StudentConcreteRepository.class,
    StudentMapper.class,
    StudentConverter.class,
})
@InfrastructureTestTag
class StudentConcreteRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;
    private Student testStudent;

    @BeforeEach
    public void setUp() {
        this.testStudent = Student.builder()
            .id(Id.of("student0"))
            .username(Username.of("student"))
            .displayedUsername(DisplayedUsername.of("Test Student"))
            .plan(Id.of("plan0"))
            .transcript(Id.of("transcript0"))
            .build();
        this.studentRepository.save(this.testStudent);
    }

    @AfterEach
    public void tearDown() {
        this.studentRepository.remove(this.testStudent);
    }

    @Test
    public void findByUsernameWhenDataExists() {
        var res = this.studentRepository.findByUsername(this.testStudent.getUsername());
        assertTrue(res.isPresent());
        var student = res.get();
        assertEquals(this.testStudent.getId(), student.getId());
        assertEquals(this.testStudent.getUsername(), student.getUsername());
        assertEquals(this.testStudent.getDisplayedUsername(), student.getDisplayedUsername());
        assertEquals(this.testStudent.getPlan(), student.getPlan());
        assertEquals(this.testStudent.getTranscript(), student.getTranscript());
    }

    @Test
    public void findByUsernameWhenDataDoesNotExist() {
        var res = this.studentRepository.findByUsername(Username.of("nonexistent"));
        assertTrue(res.isEmpty());
    }

    @Test
    public void findWhenDataExists() {
        var res = this.studentRepository.find(this.testStudent.getId());
        assertTrue(res.isPresent());
        var student = res.get();
        assertEquals(this.testStudent.getId(), student.getId());
        assertEquals(this.testStudent.getUsername(), student.getUsername());
        assertEquals(this.testStudent.getDisplayedUsername(), student.getDisplayedUsername());
        assertEquals(this.testStudent.getPlan(), student.getPlan());
        assertEquals(this.testStudent.getTranscript(), student.getTranscript());
    }

    @Test
    public void findWhenDataDoesNotExist() {
        var res = this.studentRepository.find(Id.of("nonexistent"));
        assertTrue(res.isEmpty());
    }

    @Test
    public void save() {
        var anotherStudent = Student.builder()
            .id(Id.of("student1"))
            .username(Username.of("another-student"))
            .displayedUsername(DisplayedUsername.of("Another Student"))
            .plan(Id.of("plan0"))
            .transcript(Id.of("transcript1"))
            .build();
        this.studentRepository.save(anotherStudent);
        var res = this.studentRepository.find(anotherStudent.getId());
        assertTrue(res.isPresent());
        var student = res.get();
        assertEquals(anotherStudent.getId(), student.getId());
        assertEquals(anotherStudent.getUsername(), student.getUsername());
        assertEquals(anotherStudent.getDisplayedUsername(), student.getDisplayedUsername());
        assertEquals(anotherStudent.getPlan(), student.getPlan());
        assertEquals(anotherStudent.getTranscript(), student.getTranscript());
    }

    @Test
    public void remove() {
        this.studentRepository.remove(this.testStudent);
        var res = this.studentRepository.find(this.testStudent.getId());
        assertTrue(res.isEmpty());
    }
}