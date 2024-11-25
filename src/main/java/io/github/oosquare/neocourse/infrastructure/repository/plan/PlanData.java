package io.github.oosquare.neocourse.infrastructure.repository.plan;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "plan")
@NamedQuery(
    name = "PlanData.findByName",
    query = "SELECT p FROM PlanData p WHERE p.name = :name"
)
@NamedQuery(
    name = "PlanData.findByIncludedCourse",
    query = "SELECT p FROM PlanData p WHERE :includedCourse MEMBER OF p.includedCourseIds"
)
@NamedQuery(
    name = "PlanData.findAllReturningSummaryProjection",
    query = """
        SELECT new io.github.oosquare.neocourse.infrastructure.repository.plan.PlanSummaryProjection(
            p.id,
            p.name,
            p.totalClassPeriod,
            p.requiredClassPeriod
        ) FROM PlanData p
    """
)
public class PlanData {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private String id;

    @Column(nullable = false, updatable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer totalClassPeriod;

    @Column(nullable = false)
    private Integer requiredClassPeriod;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "plan_included_course_id", joinColumns = @JoinColumn(name = "plan_id"))
    @Column(name = "included_course_id", nullable = false)
    private Set<String> includedCourseIds;
}
