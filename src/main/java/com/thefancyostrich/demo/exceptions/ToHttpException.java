package com.thefancyostrich.demo.exceptions;

import org.springframework.http.HttpStatus;

/**
 * When we throw an exception related to an incoming http request and want to
 * inform the sender.
 */
public class ToHttpException extends RuntimeException {
    private static final long serialVersionUID = 1l;
    private final String message;
    private final HttpStatus httpStatus;

    public ToHttpException(String message, HttpStatus status) {
        this.message = message;
        this.httpStatus = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
