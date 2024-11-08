package io.github.oosquare.neocourse.infrastructure.repository.schedule;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "schedule")
@NamedQuery(
    name = "ScheduleData.findByDateAndPlace",
    query = "SELECT s FROM ScheduleData s WHERE s.startTime BETWEEN :today AND :tomorrow AND s.place = :place"
)
@NamedQuery(
    name = "ScheduleData.findByCourse",
    query = "SELECT s FROM ScheduleData s WHERE s.courseId = :course"
)
public class ScheduleData {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private String id;

    @Column(nullable = false, updatable = false)
    private String courseId;

    @Column(nullable = false, updatable = false)
    private String teacherId;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime startTime;

    @Column(nullable = false, updatable = false)
    private Duration period;

    @Column(nullable = false, updatable = false)
    private String place;

    @Column(nullable = false, updatable = false)
    private Integer capacity;

    @ElementCollection
    @CollectionTable(name = "schedule_registration", joinColumns = @JoinColumn(name = "schedule_id"))
    private Set<RegistrationData> registrations;
}
