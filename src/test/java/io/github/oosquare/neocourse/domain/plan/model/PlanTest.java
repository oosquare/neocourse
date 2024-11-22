package io.github.oosquare.neocourse.domain.plan.model;

import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.utility.exception.RuleViolationException;
import io.github.oosquare.neocourse.utility.id.Id;

import static org.junit.jupiter.api.Assertions.*;

public class PlanTest {

    @Test
    public void assignRequiredClassPeriodSucceeds() {
        var plan = Plan.builder()
            .id(Id.of("plan0"))
            .name(PlanName.of("Test Plan"))
            .includedCourses(CourseSet.ofInternally(Set.of(Id.of("course0"))))
            .totalClassPeriod(ClassPeriod.of(1))
            .requiredClassPeriod(ClassPeriod.of(0))
            .build();
        plan.assignRequiredClassPeriod(ClassPeriod.of(1));
        assertEquals(ClassPeriod.of(1), plan.getRequiredClassPeriod());
    }

    @Test
    public void assignRequiredClassPeriodThrowsWhenExceedTotalClassPeriod() {
        var plan = Plan.builder()
            .id(Id.of("plan0"))
            .name(PlanName.of("Test Plan"))
            .includedCourses(CourseSet.of())
            .totalClassPeriod(ClassPeriod.of(0))
            .requiredClassPeriod(ClassPeriod.of(0))
            .build();
        assertThrows(RuleViolationException.class, () -> {
            plan.assignRequiredClassPeriod(ClassPeriod.of(1));
        });
    }
}