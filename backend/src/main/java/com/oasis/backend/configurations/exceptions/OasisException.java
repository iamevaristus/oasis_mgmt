package com.oasis.backend.configurations.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 * The OasisException class represents an exception related to any other Serch operations.
 * It extends the RuntimeException class, indicating that it is an unchecked exception.
 * <p></p>
 * @see RuntimeException
 */
@Getter
@Setter
public class OasisException extends RuntimeException {
    private String code;

    public OasisException(String message) {
        super(message);
    }

    public OasisException(String code, String message) {
        super(message);
        this.code = code;
    }
}
