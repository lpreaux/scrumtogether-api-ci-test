package scrumtogether.scrumtogetherapi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import org.hibernate.validator.constraints.Length;
import scrumtogether.scrumtogetherapi.annotations.PasswordMatching;
import scrumtogether.scrumtogetherapi.entities.User;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@Value
@PasswordMatching(
        password = "password",
        confirmPassword = "confirmPassword"
)
public class RegistrationDto implements Serializable {
    @NotBlank(message = "Last Name is required")
    String lastName;

    @NotBlank(message = "First Name is required")
    String firstName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be a valid email address")
    String email;

    @NotBlank(message = "Username is required")
    @Length(message = "Username should have at least 3 characters", min = 3)
    String username;

    @NotBlank(message = "Password is required")
    String password;

    @NotBlank(message = "Password is required")
    String confirmPassword;
}