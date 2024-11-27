package io.github.oosquare.neocourse.utility.test;

import org.junit.jupiter.api.BeforeEach;

import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.plan.model.CourseSet;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;
import io.github.oosquare.neocourse.domain.plan.service.PlanRepository;
import io.github.oosquare.neocourse.utility.id.Id;

public interface InitializePlanTestSupport {

    PlanRepository getPlanRepository();

    default Plan createTestPlan() {
        return new Plan(Id.of("test-plan"), PlanName.of("Test Plan"));
    }

    @BeforeEach
    default void setUpDefaultPlan() {
        this.getPlanRepository().save(this.createTestPlan());
    }
}
