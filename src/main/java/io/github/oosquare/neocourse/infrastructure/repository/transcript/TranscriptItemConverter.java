package io.github.oosquare.neocourse.infrastructure.repository.transcript;

import lombok.NonNull;
import org.springframework.stereotype.Component;

import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.transcript.model.Score;
import io.github.oosquare.neocourse.domain.transcript.model.TranscriptItem;
import io.github.oosquare.neocourse.infrastructure.repository.DataConverter;
import io.github.oosquare.neocourse.utility.id.Id;

@Component
public class TranscriptItemConverter implements DataConverter<TranscriptItem, TranscriptItemData> {

    @Override
    public TranscriptItem convertToDomain(@NonNull TranscriptItemData data) {
        return TranscriptItem.ofInternally(
            Id.of(data.getCourseId()),
            ClassPeriod.of(data.getClassPeriod()),
            Score.of(data.getScore()),
            data.getEvaluated(),
            data.getParticipationAbsent()
        );
    }

    @Override
    public TranscriptItemData convertToData(@NonNull TranscriptItem entity) {
        return TranscriptItemData.builder()
            .courseId(entity.getCourse().getValue())
            .classPeriod(entity.getClassPeriod().getValue())
            .score(entity.getScore().map(Score::getValue).orElse(0.0))
            .evaluated(entity.isEvaluated())
            .participationAbsent(entity.isParticipationAbsent())
            .build();
    }
}
