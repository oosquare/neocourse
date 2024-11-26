package io.github.oosquare.neocourse.infrastructure.repository.transcript;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "transcript")
public class TranscriptData {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private String id;

    @Column(nullable = false, updatable = false)
    private String planId;

    @OneToMany(mappedBy = "id.transcriptId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TranscriptItemData> courseScores;
}
