package scrumtogether.scrumtogetherapi.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link scrumtogether.scrumtogetherapi.entities.User}
 */
@Value
public class SignInRequest implements Serializable {
    @NotBlank(message = "Username is required")
    String username;
    @NotBlank(message = "Password is required")
    String password;
}