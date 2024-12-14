package fr.scrumtogether.scrumtogetherapi.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.scrumtogether.scrumtogetherapi.dtos.TeamDto;
import fr.scrumtogether.scrumtogetherapi.dtos.UserDto;
import fr.scrumtogether.scrumtogetherapi.entities.Team;
import fr.scrumtogether.scrumtogetherapi.entities.User;
import fr.scrumtogether.scrumtogetherapi.exceptions.EntityNotFoundException;
import fr.scrumtogether.scrumtogetherapi.mappers.TeamMapper;
import fr.scrumtogether.scrumtogetherapi.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

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
}
