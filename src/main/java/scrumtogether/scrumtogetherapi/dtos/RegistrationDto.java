package scrumtogether.scrumtogetherapi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import org.hibernate.validator.constraints.Length;
import scrumtogether.scrumtogetherapi.annotations.PasswordMatching;

import java.io.Serializable;

/**
 * DTO for {@link scrumtogether.scrumtogetherapi.entities.User}
 * <p>
 * A Data Transfer Object (DTO) representing user registration details.
 * <br>
 * This class is used to encapsulate the input parameters required during user registration.
 * Validation annotations are applied to ensure data integrity, including checking for non-blank
 * fields and specific format constraints (e.g., email adherence and minimum username length).
 * <br>
 * The {@link PasswordMatching} annotation is applied at the class level to validate
 * that the "password" and "confirmPassword" fields contain matching values.
 * Implements {@link Serializable} for ease of use in distributed systems or storage purposes.
 * <br>
 * This class is immutable and automatically generates getters through the use of
 * the {@code @Value} annotation from Lombok.
 */
@Value
@PasswordMatching(
        password = "password",
        confirmPassword = "confirmPassword"
)
public class RegistrationDto implements Serializable {
    /**
     * Represents the last name of the user.
     * This field is mandatory and must not be blank.
     * It is used to store the user's last name, ensuring a valid value is provided during validation.
     */
    @NotBlank(message = "Last Name is required")
    String lastName;

    /**
     * Represents the first name of the user.
     * This field is mandatory and must not be blank.
     * It is used to store the user's first name, ensuring a valid value is provided during validation.
     */
    @NotBlank(message = "First Name is required")
    String firstName;

    /**
     * Represents the email address of a user in the registration process.
     * This field is mandatory and must not be blank.
     * Additionally, the email must conform to a valid email format.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be a valid email address")
    String email;

    /**
     * Represents the username provided by the user in the registration or sign-in process.
     * This field is mandatory and must not be blank. It is validated to ensure
     * the value has a minimum length of 3 characters. This ensures the username
     * meets the required format and standards for the application.
     */
    @NotBlank(message = "Username is required")
    @Length(message = "Username should have at least 3 characters", min = 3)
    String username;

    /**
     * Represents the password provided by the user during registration or sign-in.
     * This field is mandatory and must not be blank. Validation ensures that a
     * non-empty password is provided as part of the input.
     */
    @NotBlank(message = "Password is required")
    String password;

    /**
     * Represents the confirmation password provided by the user during the registration process.
     * This field is mandatory and must not be blank. It is used to ensure that the provided
     * password matches the user's confirmation input, as part of the password validation.
     */
    @NotBlank(message = "Password is required")
    String confirmPassword;
}