package io.github.oosquare.neocourse.infrastructure.repository.teacher;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "teacher")
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
}
