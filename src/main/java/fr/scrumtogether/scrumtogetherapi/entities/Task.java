package fr.scrumtogether.scrumtogetherapi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
@Entity
@Table(name = "task")
@Inheritance(strategy = InheritanceType.JOINED)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "estimation")
    private Integer estimation;

    @Column(name = "duration")
    private Duration duration;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_story_id", nullable = false)
    private UserStory userStory;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private TaskStatus status;

}