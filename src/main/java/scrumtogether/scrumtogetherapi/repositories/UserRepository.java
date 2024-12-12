package scrumtogether.scrumtogetherapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import scrumtogether.scrumtogetherapi.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}