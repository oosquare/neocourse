package io.github.oosquare.neocourse.application.query.transcript;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.infrastructure.repository.transcript.TranscriptItemDetailedProjection;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class TranscriptItemDetailedRepresentation {

    public static final String PARTICIPATION_STATUS_ABSENT = "Absent";
    public static final String PARTICIPATION_STATUS_ATTENDED = "Attended";

    private final @NonNull String courseName;
    private final @NonNull Integer classPeriod;
    private final @NonNull Double score;
    private final @NonNull Boolean evaluated;
    private final @NonNull String participationStatus;

    public static TranscriptItemDetailedRepresentation fromData(
        @NonNull TranscriptItemDetailedProjection data
    ) {
        var participationStatus = data.getParticipationAbsent()
            ? PARTICIPATION_STATUS_ABSENT
            : PARTICIPATION_STATUS_ATTENDED;
        return TranscriptItemDetailedRepresentation.builder()
            .courseName(data.getCourseName())
            .classPeriod(data.getClassPeriod())
            .score(data.getScore())
            .evaluated(data.getEvaluated())
            .participationStatus(participationStatus)
            .build();
    }
}
