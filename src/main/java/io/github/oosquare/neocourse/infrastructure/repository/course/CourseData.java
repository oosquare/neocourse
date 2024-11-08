package io.github.oosquare.neocourse.infrastructure.repository.course;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;

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
    name = "CourseData.findByName",
    query = "SELECT c FROM CourseData c WHERE c.name = :name"
)
public class CourseData {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private String id;

    @Column(nullable = false, updatable = false, unique = true)
    private String name;

    @Column(nullable = false, updatable = false)
    private Integer classPeriod;
}
