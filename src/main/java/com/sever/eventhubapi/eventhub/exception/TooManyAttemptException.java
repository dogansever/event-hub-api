package com.sever.eventhubapi.eventhub.exception;

public class TooManyAttemptException extends BaseException {
    public TooManyAttemptException(String errorMessage) {
        super(errorMessage);
    }
}
