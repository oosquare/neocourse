package io.github.oosquare.neocourse.infrastructure.repository.teacher;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@NamedQuery(
    name = "TeacherData.findByUsername",
    query = "SELECT t FROM TeacherData t WHERE t.username = :username"
)
public class TeacherData {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private String id;

    @Column(nullable = false, updatable = false, unique = true)
    private String username;

    @Column(nullable = false, updatable = false)
    private String displayedUsername;

    @ElementCollection
    @Column(nullable = false)
    private Set<String> managedScheduleIds;
}
