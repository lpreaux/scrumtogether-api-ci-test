package fr.scrumtogether.scrumtogetherapi.services;

import org.springframework.stereotype.Service;

import fr.scrumtogether.scrumtogetherapi.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class TeamService {
    private final TeamRepository teamRepository;
}
