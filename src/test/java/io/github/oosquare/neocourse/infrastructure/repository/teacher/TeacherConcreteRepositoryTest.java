package io.github.oosquare.neocourse.infrastructure.repository.teacher;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import io.github.oosquare.neocourse.domain.common.model.DisplayedUsername;
import io.github.oosquare.neocourse.domain.common.model.Username;
import io.github.oosquare.neocourse.domain.teacher.model.Teacher;
import io.github.oosquare.neocourse.domain.teacher.service.TeacherRepository;
import io.github.oosquare.neocourse.utility.annotation.InfrastructureTestTag;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({
    TeacherConcreteRepository.class,
    TeacherMapper.class,
    TeacherConverter.class,
})
@InfrastructureTestTag
class TeacherConcreteRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;
    private Teacher testTeacher;

    @BeforeEach
    public void setUp() {
        this.testTeacher = Teacher.builder()
            .id(Id.of("teacher0"))
            .username(Username.of("teacher"))
            .displayedUsername(DisplayedUsername.of("Test Teacher"))
            .build();
        this.teacherRepository.save(this.testTeacher);
    }

    @AfterEach
    public void tearDown() {
        this.teacherRepository.remove(this.testTeacher);
    }

    @Test
    public void findByUsernameWhenDataExists() {
        var res = this.teacherRepository.findByUsername(this.testTeacher.getUsername());
        assertTrue(res.isPresent());
        var teacher = res.get();
        assertEquals(this.testTeacher.getId(), teacher.getId());
        assertEquals(this.testTeacher.getUsername(), teacher.getUsername());
        assertEquals(this.testTeacher.getDisplayedUsername(), teacher.getDisplayedUsername());
    }

    @Test
    public void findByUsernameWhenDataDoesNotExist() {
        var res = this.teacherRepository.findByUsername(Username.of("nonexistent"));
        assertTrue(res.isEmpty());
    }

    @Test
    public void findWhenDataExists() {
        var res = this.teacherRepository.find(this.testTeacher.getId());
        assertTrue(res.isPresent());
        var teacher = res.get();
        assertEquals(this.testTeacher.getId(), teacher.getId());
        assertEquals(this.testTeacher.getUsername(), teacher.getUsername());
        assertEquals(this.testTeacher.getDisplayedUsername(), teacher.getDisplayedUsername());
    }

    @Test
    public void findWhenDataDoesNotExist() {
        var res = this.teacherRepository.find(Id.of("nonexistent"));
        assertTrue(res.isEmpty());
    }

    @Test
    public void save() {
        var updatedTeacher = Teacher.builder()
            .id(Id.of("teacher1"))
            .username(Username.of("teacher1"))
            .displayedUsername(DisplayedUsername.of("Test Teacher"))
            .build();
        this.teacherRepository.save(updatedTeacher);
        var teacher = this.teacherRepository.find(updatedTeacher.getId()).orElseThrow();
        assertEquals(updatedTeacher.getId(), teacher.getId());
    }

    @Test
    public void remove() {
        this.teacherRepository.remove(this.testTeacher);
        assertTrue(this.teacherRepository.find(this.testTeacher.getId()).isEmpty());
    }
}