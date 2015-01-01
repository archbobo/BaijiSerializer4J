package com.baijioss.serializer.schema;


/**
 * The type of a schema.
 */
public enum SchemaType {
    RECORD, ENUM, ARRAY, MAP, UNION, STRING, BYTES,
    INT, LONG, FLOAT, DOUBLE, BOOLEAN, NULL;
    private String name;

    private SchemaType() {
        this.name = this.name().toLowerCase();
    }

    public String getName() {
        return name;
    }
}
