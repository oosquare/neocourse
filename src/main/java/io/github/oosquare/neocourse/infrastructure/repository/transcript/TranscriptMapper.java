package io.github.oosquare.neocourse.infrastructure.repository.transcript;

import jakarta.persistence.EntityManager;

import lombok.NonNull;
import org.springframework.stereotype.Repository;

import io.github.oosquare.neocourse.infrastructure.repository.DataMapper;

@Repository
public class TranscriptMapper extends DataMapper<TranscriptData> {

    public TranscriptMapper(@NonNull EntityManager entityManager) {
        super(entityManager, TranscriptData.class);
    }
}
