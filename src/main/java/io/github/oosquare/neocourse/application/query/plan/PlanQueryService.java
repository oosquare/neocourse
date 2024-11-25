package io.github.oosquare.neocourse.application.query.plan;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.domain.account.model.Account;
import io.github.oosquare.neocourse.infrastructure.repository.plan.PlanMapper;
import io.github.oosquare.neocourse.utility.exception.EntityNotFoundException;
import io.github.oosquare.neocourse.utility.id.Id;

@Service
@AllArgsConstructor
@Slf4j
public class PlanQueryService {

    private final @NonNull PlanMapper planMapper;

    @Transactional
    public PlanRepresentation getPlanById(@NonNull Id planId, @NonNull Account account) {
        log.info("{} requests getPlanById with {}", account.toLoggingString(), planId);

        return this.planMapper.find(planId.getValue())
            .map(PlanRepresentation::fromData)
            .orElseThrow(() -> EntityNotFoundException.builder()
                .entity(PlanRepresentation.class)
                .context("planId", planId)
                .build());
    }

    @Transactional
    public List<PlanSummaryRepresentation> getAllPlansInSummaryRepresentation(@NonNull Account account) {
        log.info("{} requests getAllPlansInSummaryRepresentation", account.toLoggingString());

        return this.planMapper.findAllReturningSummaryProjection()
            .stream()
            .map(PlanSummaryRepresentation::fromData)
            .toList();
    }
}
