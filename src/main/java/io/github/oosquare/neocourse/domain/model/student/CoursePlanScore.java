package io.github.oosquare.neocourse.domain.model.student;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

import io.github.oosquare.neocourse.utility.id.Id;

@Value
public class CoursePlanScore {

    private final @NonNull @With(AccessLevel.PRIVATE) Map<Id, Score> courseScores;

    public CoursePlanScore() {
        this.courseScores = new HashMap<>();
    }

    public @NonNull CoursePlanScore assignScoreForCourse(@NonNull Id course, @NonNull Score score) {
        var newCourseScores = new HashMap<>(this.courseScores);
        newCourseScores.put(course, score);
        return this.withCourseScores(newCourseScores);
    }
}
