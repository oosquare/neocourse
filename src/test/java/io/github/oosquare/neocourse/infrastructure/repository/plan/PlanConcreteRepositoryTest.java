package io.github.oosquare.neocourse.infrastructure.repository.plan;

import jakarta.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.course.model.CourseName;
import io.github.oosquare.neocourse.domain.plan.model.CourseSet;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;
import io.github.oosquare.neocourse.domain.plan.service.PlanRepository;
import io.github.oosquare.neocourse.utility.annotation.InfrastructureTestTag;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({
    PlanConcreteRepository.class,
    PlanMapper.class,
    PlanConverter.class,
})
@InfrastructureTestTag
class PlanConcreteRepositoryTest {

    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private EntityManager entityManager;

    private Plan testPlan;
    private Course testCourse;

    @BeforeEach
    public void setUp() {
        this.testCourse = Course.builder()
            .id(Id.of("course0"))
            .name(CourseName.of("Test Course"))
            .classPeriod(ClassPeriod.of(1))
            .build();

        this.testPlan = Plan.builder()
            .id(Id.of("plan0"))
            .name(PlanName.of("Test Plan"))
            .requiredClassPeriod(ClassPeriod.of(2))
            .includedCourses(CourseSet.ofInternally(new HashSet<>(Set.of(
                Id.of("course0"),
                Id.of("course1")
            ))))
            .build();

        this.planRepository.save(this.testPlan);

        this.entityManager.flush();
        System.out.println("Post setUp()");
    }

    @AfterEach
    public void tearDown() {
        this.entityManager.flush();
        System.out.println("Pre tearDown()");
        this.planRepository.remove(this.testPlan);
        this.entityManager.flush();

    }

    @Test
    public void findByNameWhenDataExists() {
        var plan = this.planRepository.findByName(PlanName.of("Test Plan")).orElseThrow();
        assertEquals(this.testPlan.getId(), plan.getId());
        assertEquals(this.testPlan.getName(), plan.getName());
        assertEquals(this.testPlan.getIncludedCourses(), plan.getIncludedCourses());
    }

    @Test
    public void findByNameWhenDataDoesNotExist() {
        var res = this.planRepository.findByName(PlanName.of("Nonexistent Plan"));
        assertTrue(res.isEmpty());
    }

    @Test
    public void findByIncludedCourseWhenDataExists() {
        var plan = this.planRepository.findByIncludedCourse(this.testCourse).orElseThrow();
        assertEquals(this.testPlan.getId(), plan.getId());
    }

    @Test
    public void findByIncludedCourseWhenDataDoesNotExist() {
        var course = Course.builder()
            .id(Id.of("nonexistent"))
            .name(CourseName.of("Nonexistent Course"))
            .classPeriod(ClassPeriod.of(1))
            .build();
        var res = this.planRepository.findByIncludedCourse(course);
        assertTrue(res.isEmpty());
    }

    @Test
    public void findWhenDataExists() {
        var plan = this.planRepository.find(this.testPlan.getId()).orElseThrow();
        assertEquals(this.testPlan.getId(), plan.getId());
        assertEquals(this.testPlan.getName(), plan.getName());
        assertEquals(this.testPlan.getIncludedCourses(), plan.getIncludedCourses());
    }

    @Test
    public void findWhenDataDoesNotExist() {
        var res = this.planRepository.find(Id.of("nonexistent"));
        assertTrue(res.isEmpty());
    }

    @Test
    public void save() {
        var updatedPlan = Plan.builder()
            .id(this.testPlan.getId())
            .name(this.testPlan.getName())
            .requiredClassPeriod(ClassPeriod.of(2))
            .includedCourses(CourseSet.ofInternally(new HashSet<>(Set.of(
                Id.of("course1"),
                Id.of("new-course")
            ))))
            .build();
        this.planRepository.save(updatedPlan);

        var plan = this.planRepository.find(updatedPlan.getId()).orElseThrow();
        assertFalse(plan.getIncludedCourses().getCourses().contains(Id.of("course0")));
        assertTrue(plan.getIncludedCourses().getCourses().contains(Id.of("course1")));
        assertTrue(plan.getIncludedCourses().getCourses().contains(Id.of("new-course")));
    }

    @Test
    public void remove() {
        this.planRepository.remove(this.testPlan);
        assertTrue(this.planRepository.find(this.testPlan.getId()).isEmpty());
    }
}
