package com.baijioss.serializer.specific;

import com.baijioss.serializer.schema.Schema;

public enum EnumTest implements SpecificRecord {
    CAR,
    BIKE,
    PLANE;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"enum\",\"name\":\"EnumTest\",\"symbols\":[\"CAR\",\"BIKE\",\"PLANE\"]}");


    @Override
    public Schema getSchema() {
        return SCHEMA;
    }

    @Override
    public Object get(int fieldPos) {
        return null;
    }

    @Override
    public void put(int fieldPos, Object fieldValue) {

    }

    @Override
    public Object get(String fieldName) {
        return null;
    }

    @Override
    public void put(String fieldName, Object fieldValue) {

    }
}
