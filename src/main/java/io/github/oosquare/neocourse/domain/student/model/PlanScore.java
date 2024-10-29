package io.github.oosquare.neocourse.domain.student.model;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

import io.github.oosquare.neocourse.utility.id.Id;

@Value
@AllArgsConstructor
public class PlanScore {

    private final @NonNull @With Map<Id, Score> courseScores;

    public PlanScore() {
        this(ImmutableMap.of());
    }

    public @NonNull PlanScore assignScoreForCourse(@NonNull Id course, @NonNull Score score) {
        var newCourseScores = ImmutableMap.<Id, Score>builder()
            .putAll(this.courseScores)
            .put(course, score)
            .build();
        return this.withCourseScores(newCourseScores);
    }
}
