package io.github.oosquare.neocourse.infrastructure.repository.plan;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
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
@NamedQuery(
    name = "PlanData.findByName",
    query = "SELECT p FROM PlanData p WHERE p.name = :name"
)
@NamedQuery(
    name = "PlanData.findByIncludedCourse",
    query = "SELECT p FROM PlanData p WHERE :includedCourse MEMBER OF p.includedCourseIds"
)
public class PlanData {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private String id;

    @Column(nullable = false, updatable = false, unique = true)
    private String name;

    @ElementCollection
    @Column(nullable = false)
    private Set<String> includedCourseIds;
}
