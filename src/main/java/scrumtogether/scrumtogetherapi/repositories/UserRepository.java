package scrumtogether.scrumtogetherapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import scrumtogether.scrumtogetherapi.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}