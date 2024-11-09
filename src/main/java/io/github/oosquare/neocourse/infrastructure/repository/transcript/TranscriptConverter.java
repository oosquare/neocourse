package io.github.oosquare.neocourse.infrastructure.repository.transcript;

import java.util.HashMap;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import io.github.oosquare.neocourse.domain.transcript.model.Transcript;
import io.github.oosquare.neocourse.infrastructure.repository.DataConverter;
import io.github.oosquare.neocourse.utility.id.Id;

@Component
@AllArgsConstructor
public class TranscriptConverter implements DataConverter<Transcript, TranscriptData> {

    private final @NonNull TranscriptItemConverter transcriptItemConverter;

    @Override
    public Transcript convertToDomain(@NonNull TranscriptData data) {
        var courseScores = data.getCourseScores()
            .stream()
            .collect(Collectors.toMap(
                entry -> Id.of(entry.getCourseId()),
                this.transcriptItemConverter::convertToDomain
            ));
        return Transcript.builder()
            .id(Id.of(data.getId()))
            .plan(Id.of(data.getPlanId()))
            .courseScores(new HashMap<>(courseScores))
            .build();
    }

    @Override
    public TranscriptData convertToData(@NonNull Transcript entity) {
        var courseScores = entity.getCourseScores()
            .values()
            .stream()
            .map(this.transcriptItemConverter::convertToData)
            .collect(Collectors.toSet());
        return TranscriptData.builder()
            .id(entity.getId().getValue())
            .planId(entity.getPlan().getValue())
            .courseScores(courseScores)
            .build();
    }
}
