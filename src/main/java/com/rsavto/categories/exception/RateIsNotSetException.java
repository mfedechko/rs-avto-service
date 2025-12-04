package com.rsavto.categories.exception;

/**
 * @author Mykola Fedechko
 */
public class RateIsNotSetException extends RuntimeException {

    public RateIsNotSetException(final String message) {
        super(message);
    }
}
