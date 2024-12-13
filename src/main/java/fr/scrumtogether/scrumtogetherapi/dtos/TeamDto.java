package fr.scrumtogether.scrumtogetherapi.dtos;

import java.io.Serializable;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import fr.scrumtogether.scrumtogetherapi.entities.TeamUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO for {@link Team}
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class TeamDto extends RepresentationModel<TeamDto> implements Serializable {
    Long id;
    String name;
    String description;
    String email;
    Set<TeamUser> teamUsers;
}
