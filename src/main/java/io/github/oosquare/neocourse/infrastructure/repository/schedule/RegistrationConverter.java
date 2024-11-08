package io.github.oosquare.neocourse.infrastructure.repository.schedule;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import io.github.oosquare.neocourse.domain.schedule.model.ParticipationStatus;
import io.github.oosquare.neocourse.domain.schedule.model.Registration;
import io.github.oosquare.neocourse.infrastructure.repository.DataConverter;
import io.github.oosquare.neocourse.utility.id.Id;

@Component
@AllArgsConstructor
public class RegistrationConverter implements DataConverter<Registration, RegistrationData> {

    @Override
    public Registration convertToDomain(@NonNull RegistrationData data) {
        var participationStatus = switch (data.getParticipationStatus()) {
            case ATTENDED -> ParticipationStatus.ATTENDED;
            case ABSENT -> ParticipationStatus.ABSENT;
        };
        return Registration.ofInternally(
            Id.of(data.getStudentId()),
            participationStatus
        );
    }

    @Override
    public RegistrationData convertToData(@NonNull Registration entity) {
        var participationStatusData = switch (entity.getStatus()) {
            case ATTENDED -> ParticipationStatusData.ATTENDED;
            case ABSENT -> ParticipationStatusData.ABSENT;
        };
        return RegistrationData.builder()
            .studentId(entity.getStudent().getValue())
            .participationStatus(participationStatusData)
            .build();
    }
}
