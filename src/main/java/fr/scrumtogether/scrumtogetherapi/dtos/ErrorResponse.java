package fr.scrumtogether.scrumtogetherapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Value;


/**
 * Standard error response format for the API.
 *
 * @param message the main error message
 * @param details additional error details (can be a List for validation errors)
 */
@AllArgsConstructor
@Value
public class ErrorResponse {
    String message;
    Object details;

    /**
     * Creates an error response with only a message.
     */
    public ErrorResponse(String message) {
        this(message, null);
    }
}
