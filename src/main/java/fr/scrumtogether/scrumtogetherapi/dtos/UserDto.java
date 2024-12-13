package fr.scrumtogether.scrumtogetherapi.dtos;

import fr.scrumtogether.scrumtogetherapi.entities.User;
import fr.scrumtogether.scrumtogetherapi.entities.enums.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link User}
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDto extends RepresentationModel<UserDto> implements Serializable {
    Long id;
    String lastName;
    String firstName;
    String email;
    String username;
    Role role;
    Boolean verifiedEmail;
    Long version;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}