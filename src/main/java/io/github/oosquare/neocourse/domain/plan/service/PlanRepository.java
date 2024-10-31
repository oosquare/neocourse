package io.github.oosquare.neocourse.domain.plan.service;

import java.util.Optional;

import lombok.NonNull;

import io.github.oosquare.neocourse.domain.EntityRepository;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;

public interface PlanRepository extends EntityRepository<Plan> {

    Optional<PlanSummaryRepresentation> findByNameReturningSummary(@NonNull PlanName name);
}
