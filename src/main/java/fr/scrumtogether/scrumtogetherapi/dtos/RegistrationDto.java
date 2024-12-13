package fr.scrumtogether.scrumtogetherapi.dtos;

import fr.scrumtogether.scrumtogetherapi.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import fr.scrumtogether.scrumtogetherapi.annotations.PasswordMatching;

import java.io.Serializable;

/**
 * DTO for {@link User}
 * <p>
 * A Data Transfer Object (DTO) representing user registration details.
 * This class encapsulates the input parameters required during user registration.
 * All input fields are validated to ensure data integrity and security requirements.
 * <p>
 * The {@link PasswordMatching} annotation ensures that the password and confirmPassword fields match.
 * The class is immutable and implements {@link Serializable} for distributed system compatibility.
 */
@Value
@ToString(exclude = {"password", "confirmPassword"})
@PasswordMatching(
        password = "password",
        confirmPassword = "confirmPassword"
)
public class RegistrationDto implements Serializable {
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9._-]+$";

    /**
     * The user's last name.
     * Must not be blank and is trimmed of leading/trailing whitespace.
     */
    @NotBlank(message = "Last Name is required")
    String lastName;

    /**
     * The user's first name.
     * Must not be blank and is trimmed of leading/trailing whitespace.
     */
    @NotBlank(message = "First Name is required")
    String firstName;

    /**
     * The user's email address.
     * Must be a valid email format and is trimmed of leading/trailing whitespace.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be a valid email address")
    String email;

    /**
     * The user's chosen username.
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
     * The user's chosen password.
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
     * Confirmation of the user's chosen password.
     * Must match the password field exactly.
     * For security reasons, this field is excluded from toString() output.
     */
    @NotBlank(message = "Password confirmation is required")
    String confirmPassword;

    /**
     * Creates a new RegistrationDto instance.
     * Trims any leading or trailing whitespace from string fields while preserving passwords as-is.
     */
    @Builder
    public RegistrationDto(String lastName, String firstName, String email,
                           String username, String password, String confirmPassword) {
        this.lastName = lastName != null ? lastName.trim() : null;
        this.firstName = firstName != null ? firstName.trim() : null;
        this.email = email != null ? email.trim() : null;
        this.username = username != null ? username.trim() : null;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}