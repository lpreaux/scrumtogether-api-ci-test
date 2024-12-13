package fr.scrumtogether.scrumtogetherapi.mappers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import fr.scrumtogether.scrumtogetherapi.controllers.TeamController;
import fr.scrumtogether.scrumtogetherapi.controllers.teamController;
import fr.scrumtogether.scrumtogetherapi.dtos.TeamDto;
import fr.scrumtogether.scrumtogetherapi.dtos.teamDto;
import fr.scrumtogether.scrumtogetherapi.entities.Team;
import fr.scrumtogether.scrumtogetherapi.entities.team;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TeamMapper extends RepresentationModelAssemblerSupport<Team, TeamDto> {

    public TeamMapper() {
        super(TeamController.class, TeamDto.class);
    }

    public void updateEntity(Team team, TeamDto teamDto) {
        log.debug("Updating team: {}", team.getname());

        team.setLastName(teamDto.getLastName());
        team.setFirstName(teamDto.getFirstName());
        team.setEmail(teamDto.getEmail());
        team.setteamname(teamDto.getteamname());
        team.setVerifiedEmail(teamDto.getVerifiedEmail());
        team.setRole(teamDto.getRole());
    }

    @Override
    @NonNull
    public TeamDto toModel(@NonNull Team entity) {
        log.debug("Mapping team to team DTO: {}", entity.getName());

        TeamDto teamDto = instantiateModel(entity);
        teamDto.setId(entity.getId());
        teamDto.setLastName(entity.getLastName());
        teamDto.setFirstName(entity.getFirstName());
        teamDto.setEmail(entity.getEmail());
        teamDto.setteamname(entity.getteamname());
        teamDto.setVerifiedEmail(entity.getVerifiedEmail());
        teamDto.setRole(entity.getRole());
        teamDto.setVersion(entity.getVersion());
        teamDto.setCreatedAt(entity.getCreatedAt());
        teamDto.setUpdatedAt(entity.getUpdatedAt());

        teamDto.add(linkTo(methodOn(TeamController.class).getById(entity.getId())).withSelfRel());

        return teamDto;
    }

    
}
