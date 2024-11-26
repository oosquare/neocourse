package io.github.oosquare.neocourse.infrastructure.repository.transcript;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptItemId implements Serializable {

    private String transcriptId;
    private String courseId;
}
