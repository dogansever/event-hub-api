package com.sever.eventhubapi.eventhub.exception;

public class AssertionUtil {
    public static void fail() {
        throw new InvalidMessageException();
    }
}
