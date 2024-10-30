package io.github.oosquare.neocourse.domain.student.model;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.utility.id.Id;

@Value
@AllArgsConstructor(staticName = "ofInternally")
public class PlanScore {

    private final @NonNull @With Map<Id, Score> courseScores;

    private PlanScore() {
        this(ImmutableMap.of());
    }

    public static PlanScore of() {
        return new PlanScore();
    }

    public PlanScore assignScoreForCourse(@NonNull Id course, @NonNull Score score) {
        var newCourseScores = ImmutableMap.<Id, Score>builder()
            .putAll(this.courseScores)
            .put(course, score)
            .build();
        return this.withCourseScores(newCourseScores);
    }

    public FinalResult calculateFinalResult(
        @NonNull Map<Id, ClassPeriod> includedClassPeriods,
        @NonNull ClassPeriod requiredClassPeriod
    ) {
        int finishedClassPeriod = 0;
        double weightedScoreSum = 0;
        int weights = 0;

        for (var entry : includedClassPeriods.entrySet()) {
            Id course = entry.getKey();
            ClassPeriod weight = entry.getValue();
            if (this.courseScores.containsKey(entry.getKey())) {
                finishedClassPeriod += weight.getValue();
                Score score = this.courseScores.getOrDefault(course, Score.of(0));
                weightedScoreSum += weight.getValue() * score.getValue();
                weights += weight.getValue();
            }
        }

        if (finishedClassPeriod < requiredClassPeriod.getValue()) {
            return FinalResult.ofPlanUnfinished();
        } else {
            double averageScore = weightedScoreSum / weights;
            return FinalResult.ofPlanFinished(averageScore);
        }
    }
}
