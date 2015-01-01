package com.baijioss.serializer.schema;

import com.baijioss.serializer.exception.BaijiRuntimeException;

/**
 * Thrown for errors parsing schemas.
 */
public class SchemaParseException extends BaijiRuntimeException {
    public SchemaParseException(Throwable cause) {
        super(cause);
    }

    public SchemaParseException(String message) {
        super(message);
    }
}

