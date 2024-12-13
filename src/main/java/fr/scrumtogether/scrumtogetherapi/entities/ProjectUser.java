package fr.scrumtogether.scrumtogetherapi.entities;

import fr.scrumtogether.scrumtogetherapi.entities.enums.ProjectRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project_user", indexes = {
        @Index(name = "idx_project_user_role", columnList = "project_role")
})
public class ProjectUser {
    @EmbeddedId
    private ProjectUserId id;

    @ManyToOne(optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @MapsId("projectId")
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_role", nullable = false)
    private ProjectRole projectRole = ProjectRole.DEFAULT;
}
