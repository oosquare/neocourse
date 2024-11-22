package io.github.oosquare.neocourse.infrastructure.repository.schedule;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import io.github.oosquare.neocourse.domain.schedule.model.ParticipationStatus;
import io.github.oosquare.neocourse.domain.schedule.model.Registration;
import io.github.oosquare.neocourse.utility.id.Id;

@Component
@AllArgsConstructor
public class RegistrationConverter {

    public Registration convertToDomain(@NonNull RegistrationData data) {
        var participationStatus = switch (data.getParticipationStatus()) {
            case ATTENDED -> ParticipationStatus.ATTENDED;
            case ABSENT -> ParticipationStatus.ABSENT;
        };
        return Registration.ofInternally(
            Id.of(data.getId().getStudentId()),
            participationStatus
        );
    }

    public RegistrationData convertToData(@NonNull Registration entity, @NonNull Id scheduleId) {
        var participationStatusData = switch (entity.getStatus()) {
            case ATTENDED -> ParticipationStatusData.ATTENDED;
            case ABSENT -> ParticipationStatusData.ABSENT;
        };
        return RegistrationData.builder()
            .id(new RegistrationId(scheduleId.getValue(), entity.getStudent().getValue()))
            .participationStatus(participationStatusData)
            .build();
    }
}
