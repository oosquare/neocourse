package io.github.oosquare.neocourse.infrastructure.repository.teacher;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
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

    @ElementCollection
    @CollectionTable(name = "teacher_managed_schedule_id", joinColumns = @JoinColumn(name = "teacher_id"))
    @Column(name = "managed_schedule_id", nullable = false)
    private Set<String> managedScheduleIds;
}
