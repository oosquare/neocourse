package io.github.oosquare.neocourse.infrastructure.repository.transcript;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import io.github.oosquare.neocourse.domain.transcript.model.Transcript;
import io.github.oosquare.neocourse.domain.transcript.service.TranscriptRepository;
import io.github.oosquare.neocourse.utility.id.Id;

@Repository
@AllArgsConstructor
public class TranscriptConcreteRepository implements TranscriptRepository {

    private final @NonNull TranscriptMapper transcriptMapper;
    private final @NonNull TranscriptConverter transcriptConverter;

    @Override
    public Optional<Transcript> find(@NonNull Id id) {
        return this.transcriptMapper.find(id.getValue())
            .map(this.transcriptConverter::convertToDomain);
    }

    @Override
    public void save(@NonNull Transcript entity) {
        var data = this.transcriptConverter.convertToData(entity);
        this.transcriptMapper.save(data);
    }

    @Override
    public void remove(@NonNull Transcript entity) {
        var data = this.transcriptConverter.convertToData(entity);
        this.transcriptMapper.remove(data);
    }
}
