package scrumtogether.scrumtogetherapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link scrumtogether.scrumtogetherapi.entities.User}
 * <p>
 * Represents a request object used during the user sign-in process.
 * This class encapsulates the necessary credentials required for authentication.
 * All input fields are validated to ensure data integrity and security requirements.
 * The class is immutable and implements {@link Serializable} for distributed system compatibility.
 */
@Value
@ToString(exclude = "password")
public class SignInRequest implements Serializable {
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9._-]+$";

    /**
     * The username provided during sign-in process.
     * Must be between 3 and 50 characters and can only contain letters, numbers,
     * dots, underscores and hyphens. The username is automatically trimmed of
     * leading and trailing whitespace.
     * <p>
     * Validation constraints:
     * - Must not be blank
     * - Length: 3-50 characters
     * - Pattern: letters, numbers, dots, underscores, hyphens only
     */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = USERNAME_PATTERN, message = "Username can only contain letters, numbers, dots, underscores and hyphens")
    String username;

    /**
     * The password provided during sign-in process.
     * Must meet minimum security requirements for length.
     * For security reasons, this field is excluded from toString() output.
     * <p>
     * Validation constraints:
     * - Must not be blank
     * - Minimum length: 8 characters
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long to meet security requirements")
    String password;

    /**
     * Creates a new SignInRequest instance.
     * Trims any leading or trailing whitespace from the username while preserving the password as-is.
     *
     * @param username the user's username (will be trimmed)
     * @param password the user's password (untrimmed)
     */
    @Builder
    public SignInRequest(String username, String password) {
        this.username = username != null ? username.trim() : null;
        this.password = password;
    }
}