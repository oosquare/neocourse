package io.github.oosquare.neocourse.infrastructure.repository.transcript;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Embeddable
public class TranscriptItemData {

    @Column(nullable = false)
    private String courseId;

    @Column(nullable = false)
    private Integer classPeriod;

    @Column(nullable = false)
    private Double score;

    @Column(nullable = false)
    private Boolean evaluated;

    @Column(nullable = false)
    private Boolean participationAbsent;
}
