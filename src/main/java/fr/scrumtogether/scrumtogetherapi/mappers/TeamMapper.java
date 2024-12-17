package fr.scrumtogether.scrumtogetherapi.mappers;

import fr.scrumtogether.scrumtogetherapi.controllers.TeamController;
import fr.scrumtogether.scrumtogetherapi.dtos.TeamDto;
import fr.scrumtogether.scrumtogetherapi.dtos.TeamUserDto;
import fr.scrumtogether.scrumtogetherapi.entities.Team;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@Component
public class TeamMapper extends RepresentationModelAssemblerSupport<Team, TeamDto> {

    public TeamMapper() {
        super(TeamController.class, TeamDto.class);
    }

    public Team toEntity(TeamDto teamDto) {
        log.debug("Mapping registration request for user: {}", teamDto.getName());

        return Team.builder()
                .name(teamDto.getName().trim())
                .email(teamDto.getEmail().trim().toLowerCase())
                .description(teamDto.getDescription().trim())
                .build();
    }

    public void updateEntity(Team team, TeamDto teamDto) {
        log.debug("Updating team: {}", team.getName());

        team.setName(teamDto.getName());
        team.setDescription(teamDto.getDescription());
        team.setEmail(teamDto.getEmail());
    }

    @Override
    @NonNull
    public TeamDto toModel(@NonNull Team entity) {
        log.debug("Mapping team to team DTO: {}", entity.getName());

        TeamDto teamDto = instantiateModel(entity);
        teamDto.setId(entity.getId());
        teamDto.setName(entity.getName());
        teamDto.setDescription(entity.getDescription());
        teamDto.setEmail(entity.getEmail());
        teamDto.setTeamUsers(entity.getTeamUsers().stream()
                .map(teamUser -> TeamUserDto.builder()
                        .id(teamUser.getId())
                        .userUsername(teamUser.getUser().getUsername())
                        .teamRole(teamUser.getTeamRole())
                        .build())
                .collect(Collectors.toSet()));
        teamDto.setProjects(entity.getProjects().stream()
                .map(project -> new TeamDto.ProjectDto(entity.getId(), entity.getName()))
                .collect(Collectors.toSet()));

        teamDto.add(linkTo(methodOn(TeamController.class).getById(entity.getId())).withSelfRel());

        return teamDto;
    }

}
