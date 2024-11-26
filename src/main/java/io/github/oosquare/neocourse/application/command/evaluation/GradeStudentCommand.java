package io.github.oosquare.neocourse.application.command.evaluation;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import io.github.oosquare.neocourse.domain.transcript.model.Score;
import io.github.oosquare.neocourse.utility.id.Id;

@Value
@Builder
public class GradeStudentCommand {

    private final @NonNull Id scheduleId;
    private final @NonNull Id studentId;
    private final @NonNull Score score;
}
