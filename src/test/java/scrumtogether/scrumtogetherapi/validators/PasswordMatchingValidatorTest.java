package scrumtogether.scrumtogetherapi.validators;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import scrumtogether.scrumtogetherapi.annotations.PasswordMatching;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordMatchingValidatorTest {

    @Test
    void testIsValid_WhenPasswordsMatch_ShouldReturnTrue() {
        PasswordMatchingValidator validator = new PasswordMatchingValidator();
        PasswordMatching mockAnnotation = Mockito.mock(PasswordMatching.class);

        Mockito.when(mockAnnotation.password()).thenReturn("password");
        Mockito.when(mockAnnotation.confirmPassword()).thenReturn("confirmPassword");
        validator.initialize(mockAnnotation);

        Object validatedObject = new Object() {
            private final String password = "securePassword";
            private final String confirmPassword = "securePassword";

            public String getPassword() {
                return password;
            }

            public String getConfirmPassword() {
                return confirmPassword;
            }
        };

        boolean result = validator.isValid(validatedObject, null);
        assertTrue(result);
    }

    @Test
    void testIsValid_WhenPasswordsDoNotMatch_ShouldReturnFalse() {
        PasswordMatchingValidator validator = new PasswordMatchingValidator();
        PasswordMatching mockAnnotation = Mockito.mock(PasswordMatching.class);

        Mockito.when(mockAnnotation.password()).thenReturn("password");
        Mockito.when(mockAnnotation.confirmPassword()).thenReturn("confirmPassword");
        validator.initialize(mockAnnotation);

        Object validatedObject = new Object() {
            private final String password = "securePassword";
            private final String confirmPassword = "differentPassword";

            public String getPassword() {
                return password;
            }

            public String getConfirmPassword() {
                return confirmPassword;
            }
        };

        boolean result = validator.isValid(validatedObject, null);
        assertFalse(result);
    }

    @Test
    void testIsValid_WhenPasswordIsNull_ShouldReturnFalse() {
        PasswordMatchingValidator validator = new PasswordMatchingValidator();
        PasswordMatching mockAnnotation = Mockito.mock(PasswordMatching.class);

        Mockito.when(mockAnnotation.password()).thenReturn("password");
        Mockito.when(mockAnnotation.confirmPassword()).thenReturn("confirmPassword");
        validator.initialize(mockAnnotation);

        Object validatedObject = new Object() {
            private final String password = null;
            private final String confirmPassword = "securePassword";

            public String getPassword() {
                return password;
            }

            public String getConfirmPassword() {
                return confirmPassword;
            }
        };

        boolean result = validator.isValid(validatedObject, null);
        assertFalse(result);
    }

    @Test
    void testIsValid_WhenConfirmPasswordIsNull_ShouldReturnFalse() {
        PasswordMatchingValidator validator = new PasswordMatchingValidator();
        PasswordMatching mockAnnotation = Mockito.mock(PasswordMatching.class);

        Mockito.when(mockAnnotation.password()).thenReturn("password");
        Mockito.when(mockAnnotation.confirmPassword()).thenReturn("confirmPassword");
        validator.initialize(mockAnnotation);

        Object validatedObject = new Object() {
            private final String password = "securePassword";
            private final String confirmPassword = null;

            public String getPassword() {
                return password;
            }

            public String getConfirmPassword() {
                return confirmPassword;
            }
        };

        boolean result = validator.isValid(validatedObject, null);
        assertFalse(result);
    }

    @Test
    void testIsValid_WhenBothValuesAreNull_ShouldReturnFalse() {
        PasswordMatchingValidator validator = new PasswordMatchingValidator();
        PasswordMatching mockAnnotation = Mockito.mock(PasswordMatching.class);

        Mockito.when(mockAnnotation.password()).thenReturn("password");
        Mockito.when(mockAnnotation.confirmPassword()).thenReturn("confirmPassword");
        validator.initialize(mockAnnotation);

        Object validatedObject = new Object() {
            private final String password = null;
            private final String confirmPassword = null;

            public String getPassword() {
                return password;
            }

            public String getConfirmPassword() {
                return confirmPassword;
            }
        };

        boolean result = validator.isValid(validatedObject, null);
        assertFalse(result);
    }
}