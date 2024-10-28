package io.github.oosquare.neocourse.domain.student.model;

import java.util.HashMap;
import java.util.Map;

import lombok.NonNull;
import lombok.Value;
import lombok.With;

import io.github.oosquare.neocourse.utility.id.Id;

@Value
public class CoursePlanScore {

    private final @NonNull @With Map<Id, Score> courseScores;

    public CoursePlanScore() {
        this.courseScores = new HashMap<>();
    }

    public CoursePlanScore(@NonNull Map<Id, Score> courseScores) {
        this.courseScores = courseScores;
    }

    public @NonNull CoursePlanScore assignScoreForCourse(@NonNull Id course, @NonNull Score score) {
        var newCourseScores = new HashMap<>(this.courseScores);
        newCourseScores.put(course, score);
        return this.withCourseScores(newCourseScores);
    }
}
