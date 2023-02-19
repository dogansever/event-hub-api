package com.sever.eventhubapi.eventhub.exception;

public class PaymentDeclinedException extends BaseException {
    public PaymentDeclinedException(String errorMessage) {
        super(errorMessage);
    }
}
