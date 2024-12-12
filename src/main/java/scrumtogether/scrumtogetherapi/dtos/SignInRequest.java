package scrumtogether.scrumtogetherapi.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link scrumtogether.scrumtogetherapi.entities.User}
 * <p>
 * Represents a request object used during the user sign-in process.
 * This class encapsulates the necessary credentials required for authentication.
 * The fields are validated to ensure the input adheres to the expected constraints.
 * Implements {@link Serializable} for ease of transport and compatibility in distributed systems.
 */
@Value
public class SignInRequest implements Serializable {
    /**
     * Represents the username provided by the user during the sign-in process.
     * This field is mandatory and must not be blank. Validation is enforced to ensure
     * that the provided username is not empty.
     * It is used to uniquely identify a user within the system.
     */
    @NotBlank(message = "Username is required")
    String username;

    /**
     * Represents the password provided by the user during the sign-in process.
     * This field is mandatory and must not be blank. Validation is enforced to
     * ensure that a non-empty password is provided as part of the input.
     */
    @NotBlank(message = "Password is required")
    String password;
}