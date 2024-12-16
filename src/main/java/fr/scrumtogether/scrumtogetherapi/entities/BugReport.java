package fr.scrumtogether.scrumtogetherapi.entities;

import fr.scrumtogether.scrumtogetherapi.entities.enums.SeverityLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "bug_report")
public class BugReport extends Task {
    @Enumerated(EnumType.STRING)
    @Column(name = "severity_level", nullable = false)
    private SeverityLevel severityLevel;

    @Column(name = "detection_date", nullable = false)
    private LocalDateTime detectionDate;

    private LocalDateTime fixDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "fixer_id")
    private User fixer;

}