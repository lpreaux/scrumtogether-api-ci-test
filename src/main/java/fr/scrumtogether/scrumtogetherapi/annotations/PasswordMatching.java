package fr.scrumtogether.scrumtogetherapi.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import fr.scrumtogether.scrumtogetherapi.validators.PasswordMatchingValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to validate that two specified fields within a class have matching values.
 * Commonly used for password confirmation scenarios, ensuring the password and confirm password
 * fields contain identical values.
 * <p>
 * The validation is performed at the class level by the {@code PasswordMatchingValidator}.
 * <p>
 * This annotation can be used on DTOs or other classes by specifying the fields to be validated
 * via the {@code password} and {@code confirmPassword} attributes.
 * <p>
 * Attributes:
 * - {@code password}: The name of the field representing the password.
 * - {@code confirmPassword}: The name of the field representing the confirm password.
 * - {@code message}: The error message to be returned if the validation fails.
 * - {@code groups}: Allows specification of validation groups for this constraint.
 * - {@code payload}: Can be used by clients to assign custom payload objects to the constraint.
 * <p>
 * The nested {@code @List} annotation allows multiple {@code PasswordMatching} annotations
 * to be combined on the same target class.
 * <p>
 * Example usage:
 * {@code @PasswordMatching(password = "password", confirmPassword = "confirmPassword")}
 *
 * See Also:
 * {@code PasswordMatchingValidator} for validation logic.
 */
@Constraint(validatedBy = PasswordMatchingValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatching {
    /**
     * Specifies the field name representing the primary password in the target
     * class during validation. This is used in conjunction with the {@code confirmPassword}
     * field to ensure that both values match.
     *
     * @return a string representing the field name for the primary password
     */
    String password();

    /**
     * Specifies the field name to be used as the confirmation for the password field
     * during validation. This method is typically used within the {@code PasswordMatching}
     * annotation to reference the field that holds the confirm password value.
     *
     * @return a string representing the field name for the confirm password
     */
    String confirmPassword();

    /**
     * Specifies the default error message to be used when the password fields
     * do not match during validation.
     *
     * @return a string representing the default error message, defaults to
     *         "Passwords do not match"
     */
    String message() default "Passwords do not match";

    /**
     * Specifies the groups the constraint belongs to. This allows validation
     * to be selectively applied depending on the validation group(s) targeted.
     *
     * @return an array of {@code Class} objects specifying the validation groups
     *         the constraint belongs to; defaults to an empty array
     */
    Class<?>[] groups() default {};

    /**
     * Specifies an array of classes extending {@code Payload} that can be used by clients
     * of a validation constraint to associate custom metadata or severity levels
     * with the constraint violation.
     * <p>
     * This attribute is typically used in advanced scenarios where additional information
     * about the validation failure must be conveyed to the client application.
     *
     * @return an array of {@code Class} objects extending {@code Payload},
     *         allowing consumers to define metadata associated with the constraint.
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Container annotation for defining multiple {@code @PasswordMatching} annotations on the same target.
     * <p>
     * This annotation can be used when different pairs of fields within a class need to be validated for
     * matching values. Each contained {@code @PasswordMatching} annotation specifies its own
     * {@code password} and {@code confirmPassword} field pair, along with an optional custom error message.
     * <p>
     * Can be applied at the class level to group multiple password match constraints.
     * <p>
     * Attributes:
     * - {@code value}: An array of {@code @PasswordMatching} annotations to be applied.
     * <p>
     * Example usage and validation logic are performed at the class level through the
     * {@code PasswordMatchingValidator}.
     */
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        PasswordMatching[] value();
    }
}
