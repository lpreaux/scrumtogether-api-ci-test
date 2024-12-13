package fr.scrumtogether.scrumtogetherapi.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import fr.scrumtogether.scrumtogetherapi.entities.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

    
    Optional<Team> findByName(String name);
}
