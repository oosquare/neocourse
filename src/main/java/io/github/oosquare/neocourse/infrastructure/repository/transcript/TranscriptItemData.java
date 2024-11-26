package io.github.oosquare.neocourse.infrastructure.repository.transcript;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "transcript_course_score")
public class TranscriptItemData {

    @EmbeddedId
    @Column(nullable = false, updatable = false, unique = true)
    private TranscriptItemId id;

    @Column(nullable = false)
    private Integer classPeriod;

    @Column(nullable = false)
    private Double score;

    @Column(nullable = false)
    private Boolean evaluated;

    @Column(nullable = false)
    private Boolean participationAbsent;
}
