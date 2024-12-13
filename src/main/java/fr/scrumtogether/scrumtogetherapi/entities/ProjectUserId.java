package fr.scrumtogether.scrumtogetherapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUserId implements Serializable {
    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "user_id")
    private Long userId;
}
