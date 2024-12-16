package fr.scrumtogether.scrumtogetherapi.dtos;

import fr.scrumtogether.scrumtogetherapi.entities.TeamUserId;
import fr.scrumtogether.scrumtogetherapi.entities.enums.TeamRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link fr.scrumtogether.scrumtogetherapi.entities.TeamUser}
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamUserDto implements Serializable {
    private TeamUserId id;
    private String userUsername;
    @Builder.Default
    private TeamRole teamRole = TeamRole.MEMBER;
}