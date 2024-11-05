package io.github.oosquare.neocourse.domain.schedule.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import lombok.NonNull;

import io.github.oosquare.neocourse.domain.EntityRepository;
import io.github.oosquare.neocourse.domain.course.model.Course;
import io.github.oosquare.neocourse.domain.schedule.model.Place;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;

public interface ScheduleRepository extends EntityRepository<Schedule> {

    List<Schedule> findByDateAndPlace(@NonNull ZonedDateTime date, @NonNull Place place);

    Optional<Schedule> findByCourse(@NonNull Course course);
}
