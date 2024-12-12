package scrumtogether.scrumtogetherapi.dtos;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link scrumtogether.scrumtogetherapi.entities.User}
 * <p>
 * Represents the response returned to the client upon successful user sign-in.
 * This class encapsulates the key details of the authentication result, including
 * the issued token and its validity duration.
 * <p>
 * The sign-in response is a critical part of the authentication workflow, providing
 * both the credentials needed for authorized access and metadata about their validity.
 * It supports serialization, enabling its usage in distributed systems or storage.
 * <p>
 * This class is immutable and leverages the builder pattern for safe and convenient construction.
 */
@Value
@Builder
public class SignInResponse implements Serializable {
    /**
     * Represents the authentication token issued upon successful user sign-in.
     * This field is typically a string that contains encoded information, used to
     * validate the user's identity and grant access to resources or actions.
     * It is included in the sign-in response as part of the authentication workflow.
     */
    String token;

    /**
     * Represents the duration (in seconds) for which the authentication token remains valid.
     * This field indicates the expiration time of the token issued during the sign-in process.
     * It is typically used to determine when the token will no longer be accepted for authentication
     * and a new token will need to be generated or obtained.
     */
    Long expiresIn;
}
