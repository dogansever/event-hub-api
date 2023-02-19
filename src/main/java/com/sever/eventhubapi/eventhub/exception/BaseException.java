package com.sever.eventhubapi.eventhub.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BaseException extends RuntimeException{
    public BaseException(String errorMessage) {
        super(errorMessage);
    }
}
