package io.github.oosquare.neocourse.application.query.plan;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.oosquare.neocourse.infrastructure.repository.plan.PlanMapper;

@Service
@AllArgsConstructor
@Slf4j
public class PlanQueryService {

    private final @NonNull PlanMapper planMapper;

    @Transactional
    public List<PlanRepresentation> getAllPlans() {
        log.info("Requests getAllPlans");

        return this.planMapper.findAll()
            .stream()
            .map(PlanRepresentation::fromData)
            .toList();
    }
}
