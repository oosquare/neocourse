package io.github.oosquare.neocourse.infrastructure.repository.transcript;

import jakarta.persistence.EntityManager;

import java.util.List;

import lombok.NonNull;
import org.springframework.stereotype.Repository;

import io.github.oosquare.neocourse.infrastructure.repository.DataMapper;

@Repository
public class TranscriptMapper extends DataMapper<TranscriptData> {

    public TranscriptMapper(@NonNull EntityManager entityManager) {
        super(entityManager, TranscriptData.class);
    }

    public List<TranscriptItemDetailedProjection> findAllByTranscriptReturningDetailedProjection(
        @NonNull String transcriptId
    ) {
        return this.getEntityManager()
            .createNamedQuery(
                "findAllByTranscriptReturningDetailedProjection",
                TranscriptItemDetailedProjection.class
            )
            .setParameter("transcriptId", transcriptId)
            .getResultList();
    }
}
