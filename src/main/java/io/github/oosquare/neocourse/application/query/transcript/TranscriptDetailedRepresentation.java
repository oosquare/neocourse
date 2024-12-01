package io.github.oosquare.neocourse.application.query.transcript;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.domain.student.model.Student;
import io.github.oosquare.neocourse.domain.transcript.model.Transcript;
import io.github.oosquare.neocourse.infrastructure.repository.transcript.TranscriptItemDetailedProjection;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class TranscriptDetailedRepresentation {

    private final @NonNull String studentName;
    private final @NonNull Double score;
    private final @NonNull Boolean finished;
    private final @NonNull List<TranscriptItemDetailedRepresentation> items;

    public static TranscriptDetailedRepresentation fromDomainAndData(
        @NonNull Transcript transcript,
        @NonNull Student student,
        @NonNull Plan plan,
        @NonNull List<TranscriptItemDetailedProjection> itemData
    ) {
        var finalResult = transcript.calculateFinalResult(plan.getRequiredClassPeriod());
        var items = itemData.stream()
            .map(TranscriptItemDetailedRepresentation::fromData)
            .toList();
        return TranscriptDetailedRepresentation.builder()
            .studentName(student.getDisplayedUsername().getValue())
            .score(finalResult.getScore().getValue())
            .finished(finalResult.isPlanFinished())
            .items(items)
            .build();
    }
}
