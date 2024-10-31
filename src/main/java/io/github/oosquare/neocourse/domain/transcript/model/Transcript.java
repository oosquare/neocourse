package io.github.oosquare.neocourse.domain.transcript.model;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import io.github.oosquare.neocourse.domain.Entity;
import io.github.oosquare.neocourse.domain.course.model.ClassPeriod;
import io.github.oosquare.neocourse.domain.plan.model.Plan;
import io.github.oosquare.neocourse.utility.id.Id;

@Getter
@AllArgsConstructor(staticName = "createInternally")
public class Transcript implements Entity {

    private final @NonNull Id id;
    private final @NonNull Id plan;
    private final @NonNull Map<Id, Score> courseScores;

    public Transcript(@NonNull Id id, @NonNull Plan plan) {
        this(id, plan.getId(), new HashMap<>());
    }

    public void assignScoreForCourse(@NonNull Id course, @NonNull Score score) {
        this.courseScores.put(course, score);
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
