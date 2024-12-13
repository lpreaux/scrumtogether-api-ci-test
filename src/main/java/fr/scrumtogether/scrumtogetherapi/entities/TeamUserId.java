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
public class TeamUserId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "team_id")
    private Long teamId;
}