package io.github.oosquare.neocourse.infrastructure.repository.schedule;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "schedule_registration")
public class RegistrationData {

    @EmbeddedId
    @Column(nullable = false, updatable = false, unique = true)
    private RegistrationId id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipationStatusData participationStatus;
}
