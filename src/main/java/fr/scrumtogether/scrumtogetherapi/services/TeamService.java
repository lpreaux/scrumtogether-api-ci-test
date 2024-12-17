package fr.scrumtogether.scrumtogetherapi.services;

import fr.scrumtogether.scrumtogetherapi.dtos.TeamDto;
import fr.scrumtogether.scrumtogetherapi.dtos.TeamUserDto;
import fr.scrumtogether.scrumtogetherapi.entities.Team;
import fr.scrumtogether.scrumtogetherapi.entities.TeamUser;
import fr.scrumtogether.scrumtogetherapi.entities.User;
import fr.scrumtogether.scrumtogetherapi.exceptions.EntityNotFoundException;
import fr.scrumtogether.scrumtogetherapi.exceptions.TeamException;
import fr.scrumtogether.scrumtogetherapi.mappers.TeamMapper;
import fr.scrumtogether.scrumtogetherapi.repositories.TeamRepository;
import fr.scrumtogether.scrumtogetherapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<Team> getAll(Integer pageNumber, Integer pageSize) {
        log.debug("Getting all users - paginated");

        // Validate and set default page number
        int validatedPageNumber = Optional.ofNullable(pageNumber)
                .filter(page -> page > 0)
                .orElse(0);

        // Validate and set default page size with upper bound
        int validatedPageSize = Optional.ofNullable(pageSize)
                .filter(size -> size > 0 && size <= 100)
                .orElse(20);

        log.debug("Using page number: {}, page size: {}", validatedPageNumber, validatedPageSize);

        PageRequest pageRequest = PageRequest.of(validatedPageNumber, validatedPageSize);
        return teamRepository.findAll(pageRequest);
    }

    @Transactional(readOnly = true)
    public Team getById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Transactional
    public Team update(TeamDto teamDto) {
        Team team = teamRepository.findById(teamDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        teamMapper.updateEntity(team, teamDto);
        teamDto.getTeamUsers().forEach(teamUserDto -> {
            team.getTeamUsers().stream()
                    .filter(teamUser -> teamUser.getId().equals(teamUserDto.getId()))
                    .findFirst()
                    .ifPresent(teamUser -> {
                        teamUserUpdate(teamUser, teamUserDto, team);
                    });
        });

        //TODO gérer les projects
        return team;
    }

    @Transactional
    public Team update(Long teamId, TeamDto teamDto) {
        if (!teamId.equals(teamDto.getId())) {
            throw new IllegalArgumentException("User id in path and body must be the same");
        }

        return update(teamDto);
    }

    @Transactional
    public void delete(Long id) {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isPresent()) {
            teamRepository.delete(team.get());
        } else {
            throw new EntityNotFoundException("Team not found");
        }
    }

    @Transactional
    public Team create(@RequestBody TeamDto teamDto) {
        log.debug("Processing team creation: {}", teamDto.getName());
        try {
            Team team = teamMapper.toEntity(teamDto);
            teamRepository.save(team);
            return team;

        } catch (Exception e) {
            log.error("Unexpected error during creation for team: {}", teamDto.getName(), e);
            throw new TeamException("Failled to create the team", e);
        }
    }

    private TeamUser teamUserFromDto(TeamUserDto teamUserDto, Team team) {
        log.debug("association d'un utilisateur et d'une team, for user: {} ; and for team: {}", teamUserDto.getUserUsername(), team.getName());
        User user = userRepository.findByUsernameAndDeletedAtIsNull(teamUserDto.getUserUsername()).orElseThrow(() -> new EntityNotFoundException("User " + teamUserDto.getUserUsername() + " not find"));
        return TeamUser.builder()
                .team(team)
                .user(user)
                .teamRole(teamUserDto.getTeamRole())
                .build();
    }

    private void teamUserUpdate(TeamUser teamUser, TeamUserDto teamUserDto, Team team) {
        log.debug("association d'un utilisateur et d'une team, for user: {} ; and for team: {}", teamUserDto.getUserUsername(), team.getName());
        if (!team.getId().equals(teamUser.getTeam().getId())) {
            throw new TeamException("failed to modify the user role in the team, the team is different");
        }
        if (!teamUserDto.getUserUsername().equals(teamUser.getUser().getUsername())) {
            throw new TeamException("failed to modify the user role in the team, the name is different");
        }

        teamUser.setTeamRole(teamUserDto.getTeamRole());


        /*  todo avec modification du teamuser avec des ancienne valeur mettre les nouvelles valeur de teamuserdto 
         juste update le role verif entre teamuserdto et team
         verif si user et le meme entre teamUserDto et teamUser
         si le cas j'update */
    }
}
