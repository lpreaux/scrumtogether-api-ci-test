package fr.scrumtogether.scrumtogetherapi.mappers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import fr.scrumtogether.scrumtogetherapi.controllers.TeamController;
import fr.scrumtogether.scrumtogetherapi.dtos.TeamDto;
import fr.scrumtogether.scrumtogetherapi.entities.Team;
import lombok.extern.slf4j.Slf4j;

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
                .teamUsers(teamDto.getTeamUsers())
                .build();
    }

    public void updateEntity(Team team, TeamDto teamDto) {
        log.debug("Updating team: {}", team.getName());

        team.setName(teamDto.getName());
        team.setDescription(teamDto.getDescription());
        team.setEmail(teamDto.getEmail());
        team.setTeamUsers(teamDto.getTeamUsers());
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
        teamDto.setTeamUsers(entity.getTeamUsers());

        teamDto.add(linkTo(methodOn(TeamController.class).getById(entity.getId())).withSelfRel());

        return teamDto;
    }

}
