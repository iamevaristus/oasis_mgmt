package com.oasis.backend.models.bases;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * The ApiResponse class represents a generic response model for API endpoints.
 * It encapsulates the HTTP status, response code, message, and optional data payload.
 * This class is typically used to standardize the format of responses returned by API endpoints.
 *
 * @param <T> The type of data payload included in the response.
 */
@Getter
@Setter
@ToString
public class ApiResponse<T> {
    private HttpStatus status;
    private Integer code;
    private String message;
    private T data;

    /**
     * Constructs an ApiResponse object with a message and sets default values for status and code.
     *
     * @param message The message associated with the response.
     */
    public ApiResponse(String message) {
        this.data = null;
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
        this.code = HttpStatus.BAD_REQUEST.value();
    }

    /**
     * Constructs an ApiResponse object with data and sets default values for message, status, and code.
     *
     * @param data The data payload included in the response.
     */
    public ApiResponse(T data) {
        this.data = data;
        this.message = "Successful";
        this.status = HttpStatus.OK;
        this.code = HttpStatus.OK.value();
    }

    /**
     * Constructs an ApiResponse object with a message, status, and sets code based on the status.
     *
     * @param message The message associated with the response.
     * @param status  The HTTP status of the response.
     */
    public ApiResponse(String message, HttpStatus status) {
        this.data = null;
        this.message = message;
        this.status = status;
        this.code = status.value();
    }

    /**
     * Constructs an ApiResponse object with a message, data, status, and sets code based on the status.
     *
     * @param message The message associated with the response.
     * @param data    The data payload included in the response.
     * @param status  The HTTP status of the response.
     */
    public ApiResponse(String message, T data, HttpStatus status) {
        this.data = data;
        this.message = message;
        this.status = status;
        this.code = status.value();
    }
}