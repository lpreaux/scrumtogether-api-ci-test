package fr.scrumtogether.scrumtogetherapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

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
    private Set<TeamUserDto> teamUsers = new LinkedHashSet<>();
    private Set<ProjectDto> projects = new LinkedHashSet<>();

    /**
     * DTO for {@link fr.scrumtogether.scrumtogetherapi.entities.Project}
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectDto implements Serializable {
        private Long id;
        private String name;
    }
}
