package com.distance.app.controller;

import com.distance.app.util.Error;
import com.distance.app.util.PostCodeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handles exceptions thrown in this application
 */
@RestControllerAdvice
public class ExceptionHandlerController {

    /**
     * Handles PostCodeException and converts into error.
     *
     * @param exception catches generic exception with error code and description
     * @return error with code and description
     */
    @ExceptionHandler(value = {PostCodeException.class})
    public ResponseEntity<Error> handleExceptions(PostCodeException exception) {
        return formErrorResponse(exception);
    }

    private ResponseEntity<Error> formErrorResponse(PostCodeException exception) {
        return new ResponseEntity<>(exception.getError(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
