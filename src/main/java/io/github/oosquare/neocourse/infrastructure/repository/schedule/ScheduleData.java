package io.github.oosquare.neocourse.infrastructure.repository.schedule;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
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
@NamedQuery(
    name = "ScheduleData.findByIdReturningSummaryProjection",
    query = """
        SELECT new io.github.oosquare.neocourse.infrastructure.repository.schedule.ScheduleSummaryProjection(
            s.id,
            c.name,
            t.displayedUsername,
            s.startTime,
            s.period,
            s.place,
            s.capacity
        ) FROM ScheduleData s
        JOIN CourseData c ON s.courseId = c.id
        JOIN TeacherData t ON s.teacherId = t.id
        WHERE s.id = :id
    """
)
@NamedQuery(
    name = "ScheduleData.findAllReturningSummaryProjection",
    query = """
        SELECT new io.github.oosquare.neocourse.infrastructure.repository.schedule.ScheduleSummaryProjection(
            s.id,
            c.name,
            t.displayedUsername,
            s.startTime,
            s.period,
            s.place,
            s.capacity
        ) FROM ScheduleData s
        JOIN CourseData c ON s.courseId = c.id
        JOIN TeacherData t ON s.teacherId = t.id
    """
)
@NamedQuery(
    name = "ScheduleData.findByStudentReturningSummaryProjection",
    query = """
        SELECT new io.github.oosquare.neocourse.infrastructure.repository.schedule.ScheduleSummaryProjection(
            s.id,
            c.name,
            t.displayedUsername,
            s.startTime,
            s.period,
            s.place,
            s.capacity
        ) FROM ScheduleData s
        JOIN CourseData c ON s.courseId = c.id
        JOIN TeacherData t ON s.teacherId = t.id
        JOIN RegistrationData r ON r.id.scheduleId = s.id
        WHERE r.id.studentId = :studentId
    """
)
@NamedQuery(
    name = "ScheduleData.findByIdReturningEvaluationProjection",
    query = """
        SELECT new io.github.oosquare.neocourse.infrastructure.repository.schedule.ScheduleEvaluationProjection(
            s.id,
            c.name,
            (SELECT new io.github.oosquare.neocourse.infrastructure.repository.schedule.RegistrationEvaluationProjection(
                r.id.studentId,
                st.displayedUsername,
                r.participationStatus
            ) FROM RegistrationData r
            JOIN StudentData st ON st.id = r.id.studentId
            WHERE r.id.scheduleId = s.id)
        ) FROM ScheduleData s
        JOIN CourseData c ON s.courseId = c.id
        WHERE s.id = :id
    """
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

    @OneToMany(mappedBy = "id.scheduleId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RegistrationData> registrations;
}
