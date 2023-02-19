package com.sever.eventhubapi.eventhub.exception;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;

@Slf4j
public class AssertionUtil {
    public static void failRandom() {
        if (new SecureRandom().nextInt() % 2 == 0)
            throw new BaseException("Unexpected Error");
    }

    public static void assertTrue(boolean mustBeTrue, String errorMessage) {
        if (!mustBeTrue)
            throw new PaymentDeclinedException(errorMessage);
    }

    public static void assertTrue(boolean mustBeTrue, String errorMessage, Class<TooManyAttemptException> clazz) {
        try {
            if (!mustBeTrue) {
                Constructor<?> constructor = clazz.getConstructor(String.class);
                throw clazz.cast(constructor.newInstance(new Object[]{errorMessage}));
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            log.error(e.getMessage());
        }
    }
}
