package com.distance.app.util;

import org.springframework.stereotype.Component;

/**
 * PostCode Exception
 */
@Component
public class PostCodeException extends RuntimeException {

    private final Error error;

    /**
     * Constructor
     */
    public PostCodeException() {
        error = new Error();
    }

    /**
     * Forms Exception
     *
     * @param message error message
     * @param code error code
     */
    public PostCodeException(String message, String code) {
        super(message);
        error = new Error();
        error.setErrorCode(code);
        error.setErrorDescription(message);
    }

    /**
     * Retrieve Error
     *
     * @return {@link Error}
     */
    public Error getError() {
        return error;
    }
}