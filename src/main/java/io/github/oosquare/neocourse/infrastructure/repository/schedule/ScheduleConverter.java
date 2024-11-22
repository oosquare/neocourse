package io.github.oosquare.neocourse.infrastructure.repository.schedule;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import io.github.oosquare.neocourse.domain.schedule.model.Capacity;
import io.github.oosquare.neocourse.domain.schedule.model.Place;
import io.github.oosquare.neocourse.domain.schedule.model.Schedule;
import io.github.oosquare.neocourse.domain.schedule.model.TimeRange;
import io.github.oosquare.neocourse.infrastructure.repository.DataConverter;
import io.github.oosquare.neocourse.utility.id.Id;

@Component
@AllArgsConstructor
public class ScheduleConverter implements DataConverter<Schedule, ScheduleData> {

    private final @NonNull RegistrationConverter registrationConverter;

    @Override
    public Schedule convertToDomain(@NonNull ScheduleData data) {
        var time = TimeRange.ofInternally(
            data.getStartTime().atZoneSameInstant(ZoneId.systemDefault()),
            data.getPeriod()
        );
        var registrations = data.getRegistrations()
            .stream()
            .collect(Collectors.toMap(
                entry -> Id.of(entry.getId().getStudentId()),
                this.registrationConverter::convertToDomain
            ));
        return Schedule.builder()
            .id(Id.of(data.getId()))
            .course(Id.of(data.getCourseId()))
            .teacher(Id.of(data.getTeacherId()))
            .time(time)
            .place(Place.of(data.getPlace()))
            .capacity(Capacity.of(data.getCapacity()))
            .registrations(new HashMap<>(registrations))
            .build();
    }

    @Override
    public ScheduleData convertToData(@NonNull Schedule entity) {
        var registrations = entity.getRegistrations()
            .values()
            .stream()
            .map(data -> this.registrationConverter.convertToData(data, entity.getId()))
            .collect(Collectors.toSet());
        var startTime = entity.getTime()
            .getStart()
            .withZoneSameInstant(ZoneOffset.UTC)
            .toOffsetDateTime();
        return ScheduleData.builder()
            .id(entity.getId().getValue())
            .courseId(entity.getCourse().getValue())
            .teacherId(entity.getTeacher().getValue())
            .startTime(startTime)
            .period(entity.getTime().getPeriod())
            .place(entity.getPlace().getValue())
            .capacity(entity.getCapacity().getValue())
            .registrations(registrations)
            .build();
    }
}
