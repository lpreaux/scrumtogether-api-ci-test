package fr.scrumtogether.scrumtogetherapi.repositories;

import fr.scrumtogether.scrumtogetherapi.entities.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<AuditLog, Long> {
}