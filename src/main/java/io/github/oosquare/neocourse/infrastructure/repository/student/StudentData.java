package io.github.oosquare.neocourse.infrastructure.repository.student;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@NamedQuery(
    name = "StudentData.findByUsername",
    query = "SELECT s FROM StudentData s WHERE s.username = :username"
)
public class StudentData {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private String id;

    @Column(nullable = false, updatable = false, unique = true)
    private String username;

    @Column(nullable = false, updatable = false)
    private String displayedUsername;

    @Column(nullable = false, updatable = false)
    private String planId;

    @Column(nullable = false, updatable = false)
    private String transcriptId;
}
