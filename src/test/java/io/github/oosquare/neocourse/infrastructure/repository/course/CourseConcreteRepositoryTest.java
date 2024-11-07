package io.github.oosquare.neocourse.infrastructure.repository.course;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.course.model.CourseName;
import io.github.oosquare.neocourse.domain.course.service.CourseRepository;
import io.github.oosquare.neocourse.utility.annotation.InfrastructureTestTag;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({
    CourseConcreteRepository.class,
    CourseMapper.class,
    CourseConverter.class,
})
@InfrastructureTestTag
class CourseConcreteRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;
    private Course testCourse;

    @BeforeEach
    public void setUp() {
        this.testCourse = Course.builder()
            .id(Id.of("course0"))
            .name(CourseName.of("Test Course"))
            .classPeriod(ClassPeriod.of(3))
            .build();
        this.courseRepository.save(this.testCourse);
    }

    @AfterEach
    public void tearDown() {
        this.courseRepository.remove(this.testCourse);
    }

    @Test
    public void findByNameWhenDataExists() {
        var res = this.courseRepository.findByName(this.testCourse.getName());
        assertTrue(res.isPresent());
        var course = res.get();
        assertEquals(this.testCourse.getId(), course.getId());
        assertEquals(this.testCourse.getName(), course.getName());
        assertEquals(this.testCourse.getClassPeriod(), course.getClassPeriod());
    }

    @Test
    public void findByNameWhenDataDoesNotExist() {
        assertTrue(this.courseRepository.findByName(CourseName.of("Nonexistent Course")).isEmpty());
    }

    @Test
    public void findWhenDataExists() {
        var res = this.courseRepository.find(this.testCourse.getId());
        assertTrue(res.isPresent());
        var course = res.get();
        assertEquals(this.testCourse.getId(), course.getId());
        assertEquals(this.testCourse.getName(), course.getName());
        assertEquals(this.testCourse.getClassPeriod(), course.getClassPeriod());
    }

    @Test
    public void findWhenDataDoesNotExist() {
        assertTrue(this.courseRepository.find(Id.of("nonexistent")).isEmpty());
    }

    @Test
    public void save() {
        var updatedCourse = Course.builder()
            .id(Id.of("course0"))
            .name(CourseName.of("Another Course"))
            .classPeriod(ClassPeriod.of(5))
            .build();
        this.courseRepository.save(updatedCourse);

        var course = this.courseRepository.find(updatedCourse.getId()).orElseThrow();
        assertEquals(updatedCourse.getName(), course.getName());
        assertEquals(updatedCourse.getClassPeriod(), course.getClassPeriod());
    }

    @Test
    public void remove() {
        this.courseRepository.remove(this.testCourse);
        assertTrue(this.courseRepository.find(this.testCourse.getId()).isEmpty());
    }
}