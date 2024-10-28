package io.github.oosquare.neocourse.domain.student.model;

import java.util.HashMap;
import java.util.Map;

import lombok.NonNull;
import lombok.Value;
import lombok.With;

import io.github.oosquare.neocourse.utility.id.Id;

@Value
public class PlanScore {

    private final @NonNull @With Map<Id, Score> courseScores;

    public PlanScore() {
        this.courseScores = new HashMap<>();
    }

    public PlanScore(@NonNull Map<Id, Score> courseScores) {
        this.courseScores = courseScores;
    }

    public @NonNull PlanScore assignScoreForCourse(@NonNull Id course, @NonNull Score score) {
        var newCourseScores = new HashMap<>(this.courseScores);
        newCourseScores.put(course, score);
        return this.withCourseScores(newCourseScores);
    }
}
