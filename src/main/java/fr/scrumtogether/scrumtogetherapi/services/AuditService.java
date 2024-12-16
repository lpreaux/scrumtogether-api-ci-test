package fr.scrumtogether.scrumtogetherapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.scrumtogether.scrumtogetherapi.entities.AuditLog;
import fr.scrumtogether.scrumtogetherapi.repositories.AuditRepository;
import fr.scrumtogether.scrumtogetherapi.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {
    private final AuditRepository auditRepository;
    private final ObjectMapper objectMapper;
    private final SecurityUtils securityUtils;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logUpdate(String entityType, Long entityId, Object oldValue, Object newValue) {
        try {
            String oldValueJson = objectMapper.writeValueAsString(oldValue);
            String newValueJson = objectMapper.writeValueAsString(newValue);

            AuditLog auditLog = AuditLog.builder()
                    .entityType(entityType)
                    .entityId(entityId)
                    .action("UPDATE")
                    .oldValue(oldValueJson)
                    .newValue(newValueJson)
                    .createdBy(securityUtils.getCurrentUser().getUsername())
                    .build();

            auditRepository.save(auditLog);
        } catch (Exception e) {
            // Log error but don't prevent the main transaction from completing
            log.error("Failed to create audit log", e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logDelete(String entityType, Long entityId, Object deletedEntity) {
        try {
            String entityJson = objectMapper.writeValueAsString(deletedEntity);

            AuditLog auditLog = AuditLog.builder()
                    .entityType(entityType)
                    .entityId(entityId)
                    .action("DELETE")
                    .oldValue(entityJson)
                    .newValue(null) // No new value for deletion
                    .createdBy(securityUtils.getCurrentUser().getUsername())
                    .build();

            auditRepository.save(auditLog);
            log.info("Created delete audit log for {} with ID: {}", entityType, entityId);

        } catch (Exception e) {
            // Log error but don't prevent the main transaction from completing
            log.error("Failed to create delete audit log for {} with ID: {}", entityType, entityId, e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logRestore(String entityType, Long entityId, Object restoredEntity) {
        try {
            String entityJson = objectMapper.writeValueAsString(restoredEntity);

            AuditLog auditLog = AuditLog.builder()
                    .entityType(entityType)
                    .entityId(entityId)
                    .action("RESTORE")
                    .oldValue(null) // No old value for restore
                    .newValue(entityJson)
                    .createdBy(securityUtils.getCurrentUser().getUsername())
                    .build();

            auditRepository.save(auditLog);
            log.info("Created restore audit log for {} with ID: {}", entityType, entityId);

        } catch (Exception e) {
            // Log error but don't prevent the main transaction from completing
            log.error("Failed to create restore audit log for {} with ID: {}", entityType, entityId, e);
        }
    }
}