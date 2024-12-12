package scrumtogether.scrumtogetherapi.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import scrumtogether.scrumtogetherapi.annotations.PasswordMatching;

/**
 * Validator that ensures two password fields match in the target class.
 * It is typically used with the {@code @PasswordMatching} annotation
 * and is applied at the class level.
 * <p>
 * Implements the validation logic by comparing the values of the fields
 * specified by the {@code password} and {@code confirmPassword} attributes
 * in the {@code PasswordMatching} annotation.
 * <p>
 * Implements the {@code ConstraintValidator} interface to provide custom validation.
 */
public class PasswordMatchingValidator implements ConstraintValidator<PasswordMatching, Object> {
    private String password;
    private String confirmPassword;

    /**
     * Initializes the validator by extracting the field names for password and confirm password
     * from the {@code PasswordMatching} annotation.
     *
     * @param constraintAnnotation the {@code PasswordMatching} annotation instance containing
     *                              the configuration for the password and confirm password fields
     */
    @Override
    public void initialize(PasswordMatching constraintAnnotation) {
        this.password = constraintAnnotation.password();
        this.confirmPassword = constraintAnnotation.confirmPassword();
    }

    /**
     * Validates that the values of the password and confirm password fields in the
     * target object are equal and not null.
     *
     * @param o the target object being validated
     * @param constraintValidatorContext the context in which the constraint is evaluated
     * @return {@code true} if the password and confirm password fields match and are not null,
     *         {@code false} otherwise
     */
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        Object passwordValue = new BeanWrapperImpl(o).getPropertyValue(password);
        Object confirmPasswordValue = new BeanWrapperImpl(o).getPropertyValue(confirmPassword);

        return passwordValue != null && passwordValue.equals(confirmPasswordValue);
    }
}
