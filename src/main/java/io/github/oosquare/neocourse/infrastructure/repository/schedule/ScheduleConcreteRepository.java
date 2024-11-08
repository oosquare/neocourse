package io.github.oosquare.neocourse.infrastructure.repository.schedule;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.schedule.model.Place;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;
import io.github.oosquare.neocourse.domain.schedule.service.ScheduleRepository;
import io.github.oosquare.neocourse.utility.id.Id;

@Repository
@AllArgsConstructor
public class ScheduleConcreteRepository implements ScheduleRepository {

    private final @NonNull ScheduleMapper scheduleMapper;
    private final @NonNull ScheduleConverter scheduleConverter;

    @Override
    public List<Schedule> findByDateAndPlace(@NonNull ZonedDateTime date, @NonNull Place place) {
        return this.scheduleMapper.findByDateAndPlace(date, place.getValue())
            .stream()
            .map(this.scheduleConverter::convertToDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Schedule> findByCourse(@NonNull Course course) {
        return this.scheduleMapper.findByCourse(course.getId().getValue())
            .map(this.scheduleConverter::convertToDomain);
    }

    @Override
    public Optional<Schedule> find(@NonNull Id id) {
        return this.scheduleMapper.find(id.getValue())
            .map(this.scheduleConverter::convertToDomain);
    }

    @Override
    public void save(@NonNull Schedule entity) {
        var data = this.scheduleConverter.convertToData(entity);
        this.scheduleMapper.save(data);
    }

    @Override
    public void remove(@NonNull Schedule entity) {
        var data = this.scheduleConverter.convertToData(entity);
        this.scheduleMapper.remove(data);
    }
}
