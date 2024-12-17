package fr.scrumtogether.scrumtogetherapi.entities;

import fr.scrumtogether.scrumtogetherapi.entities.enums.TeamRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "team_user", indexes = {
        @Index(name = "idx_team_user_role", columnList = "team_role")
})
public class TeamUser {
  @EmbeddedId
  private TeamUserId id;

  @ManyToOne(optional = false)
  @MapsId("userId")
  private User user;

  @ManyToOne(optional = false)
  @MapsId("teamId")
  private Team team;

  @Enumerated(EnumType.STRING)
  @Column(name = "team_role")
  private TeamRole teamRole = TeamRole.MEMBER;
}