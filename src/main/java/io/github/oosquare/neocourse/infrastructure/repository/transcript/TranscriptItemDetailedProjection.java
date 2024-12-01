package io.github.oosquare.neocourse.infrastructure.repository.transcript;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TranscriptItemDetailedProjection {

    private String courseName;
    private Integer classPeriod;
    private Double score;
    private Boolean evaluated;
    private Boolean participationAbsent;
}
