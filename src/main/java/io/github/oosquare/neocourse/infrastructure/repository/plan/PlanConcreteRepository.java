package io.github.oosquare.neocourse.infrastructure.repository.plan;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.plan.model.PlanName;
import io.github.oosquare.neocourse.domain.plan.service.PlanRepository;
import io.github.oosquare.neocourse.utility.id.Id;

@Repository
@AllArgsConstructor
public class PlanConcreteRepository implements PlanRepository {

    private final @NonNull PlanMapper planMapper;
    private final @NonNull PlanConverter planConverter;

    @Override
    public Optional<Plan> findByName(@NonNull PlanName name) {
        return this.planMapper.findByName(name.getValue())
            .map(this.planConverter::convertToDomain);
    }

    @Override
    public Optional<Plan> findByIncludedCourse(@NonNull Course includedCourse) {
        return this.planMapper.findByIncludedCourse(includedCourse.getId().getValue())
            .map(this.planConverter::convertToDomain);
    }

    @Override
    public Optional<Plan> find(@NonNull Id id) {
        return this.planMapper.find(id.getValue())
            .map(this.planConverter::convertToDomain);
    }

    @Override
    public void save(@NonNull Plan entity) {
        var data = this.planConverter.convertToData(entity);
        this.planMapper.save(data);
    }

    @Override
    public void remove(@NonNull Plan entity) {
        var data = this.planConverter.convertToData(entity);
        this.planMapper.remove(data);
    }
}
