package fr.scrumtogether.scrumtogetherapi.controllers;

import javax.annotation.Nullable;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.scrumtogether.scrumtogetherapi.dtos.TeamDto;
import fr.scrumtogether.scrumtogetherapi.entities.Team;
import fr.scrumtogether.scrumtogetherapi.mappers.TeamMapper;
import fr.scrumtogether.scrumtogetherapi.services.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {
    private final TeamService teamService;
    private final PagedResourcesAssembler<Team> pagedResourcesAssembler;
    private final TeamMapper teamMapper;

    @GetMapping
    public ResponseEntity<PagedModel<TeamDto>> getAll(@RequestParam @Nullable Integer page,
            @RequestParam @Nullable Integer size) {
        Page<Team> paginated = teamService.getAll(page, size);
        PagedModel<TeamDto> paginatedDto = pagedResourcesAssembler.toModel(paginated, teamMapper);
        return new ResponseEntity<>(paginatedDto, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<TeamDto> getById(@PathVariable Long id) {
        Team team = teamService.getById(id);
        TeamDto dto = teamMapper.toModel(team);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<TeamDto> update(@PathVariable Long id, @RequestBody TeamDto teamDto) {
        Team team = teamService.update(id, teamDto);
        TeamDto dto = teamMapper.toModel(team);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teamService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/create")
    public ResponseEntity<TeamDto> create(@RequestBody TeamDto teamDto) {
        Team team = teamService.create(teamDto);
        TeamDto dto = teamMapper.toModel(team);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

}
