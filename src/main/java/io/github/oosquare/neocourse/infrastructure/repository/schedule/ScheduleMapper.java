package io.github.oosquare.neocourse.infrastructure.repository.schedule;

import jakarta.persistence.EntityManager;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import lombok.NonNull;
import org.springframework.stereotype.Repository;

import io.github.oosquare.neocourse.infrastructure.repository.DataMapper;

@Repository
public class ScheduleMapper extends DataMapper<ScheduleData> {

    public ScheduleMapper(@NonNull EntityManager entityManager) {
        super(entityManager, ScheduleData.class);
    }

    public List<ScheduleData> findByDateAndPlace(@NonNull ZonedDateTime time, @NonNull String place) {
        var localToday = time.toLocalDate().atStartOfDay();
        var zonedToday = localToday.atZone(time.getZone());
        var offsetToday = zonedToday.withZoneSameInstant(ZoneOffset.UTC).toOffsetDateTime();

        var localTomorrow = localToday.plusDays(1).minusNanos(1);
        var zonedTomorrow = localTomorrow.atZone(time.getZone());
        var offsetTomorrow = zonedTomorrow.withZoneSameInstant(ZoneOffset.UTC).toOffsetDateTime();

        return this.getEntityManager()
            .createNamedQuery("ScheduleData.findByDateAndPlace", this.getDataClass())
            .setParameter("today", offsetToday)
            .setParameter("tomorrow", offsetTomorrow)
            .setParameter("place", place)
            .getResultList();
    }

    public Optional<ScheduleData> findByCourse(@NonNull String course) {
        var data = this.getEntityManager()
            .createNamedQuery("ScheduleData.findByCourse", this.getDataClass())
            .setParameter("course", course)
            .setMaxResults(1)
            .getResultList();
        return (data.isEmpty() ? Optional.empty() : Optional.of(data.getFirst()));
    }

    public Optional<ScheduleSummaryProjection> findByIdReturningSummaryProjection(@NonNull String id) {
        var data = this.getEntityManager()
            .createNamedQuery(
                "ScheduleData.findByIdReturningSummaryProjection",
                ScheduleSummaryProjection.class
            )
            .setParameter("id", id)
            .setMaxResults(1)
            .getResultList();
        return (data.isEmpty() ? Optional.empty() : Optional.of(data.getFirst()));
    }

    public List<ScheduleSummaryProjection> findAllReturningSummaryProjection() {
        return this.getEntityManager()
            .createNamedQuery(
                "ScheduleData.findAllReturningSummaryProjection",
                ScheduleSummaryProjection.class
            )
            .getResultList();
    }

    public List<ScheduleSummaryProjection> findByStudentReturningSummaryProjection(@NonNull String studentId) {
        return this.getEntityManager()
            .createNamedQuery(
                "ScheduleData.findByStudentReturningSummaryProjection",
                ScheduleSummaryProjection.class
            )
            .setParameter("studentId", studentId)
            .getResultList();
    }

    public Optional<ScheduleEvaluationProjection> findByIdReturningEvaluationProjection(@NonNull String id) {
        var data = this.getEntityManager()
            .createNamedQuery(
                "ScheduleData.findByIdReturningEvaluationProjection",
                ScheduleEvaluationProjection.class
            )
            .setParameter("id", id)
            .setMaxResults(1)
            .getResultList();

        if (data.isEmpty()) {
            return Optional.empty();
        }

        var res = data.getFirst();
        var registrations = this.getEntityManager()
            .createNamedQuery(
                "RegistrationData.findByScheduleAndCourseReturningEvaluationProjection",
                RegistrationEvaluationProjection.class
            )
            .setParameter("scheduleId", id)
            .setParameter("courseId", res.getCourseId())
            .getResultList();
        res.setRegistrations(registrations);
        return Optional.of(res);
    }
}
