package com.baijioss.serializer.exception;


/**
 * Thrown when an illegal type is used.
 */
public class BaijiTypeException extends BaijiRuntimeException {
    public BaijiTypeException(String message) {
        super(message);
    }

    public BaijiTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}

