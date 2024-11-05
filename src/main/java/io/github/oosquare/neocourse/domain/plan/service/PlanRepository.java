package io.github.oosquare.neocourse.domain.plan.service;

import java.util.Optional;

import lombok.NonNull;

import io.github.oosquare.neocourse.domain.EntityRepository;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;

public interface PlanRepository extends EntityRepository<Plan> {

    Optional<Plan> findByName(@NonNull PlanName name);

    Optional<Plan> findByIncludedCourse(@NonNull Course includedCourse);
}
