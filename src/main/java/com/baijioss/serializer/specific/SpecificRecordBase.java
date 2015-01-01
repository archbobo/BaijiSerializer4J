package com.baijioss.serializer.specific;

import com.baijioss.serializer.schema.Field;
import com.baijioss.serializer.schema.RecordSchema;
import com.baijioss.serializer.schema.Schema;

public abstract class SpecificRecordBase implements SpecificRecord {

    @Override
    public Object get(String fieldName) {
        Schema schema = getSchema();
        if (!(schema instanceof RecordSchema)) {
            return null;
        }
        Field field = ((RecordSchema) schema).getField(fieldName);
        return field != null ? get(field.getPos()) : null;
    }

    @Override
    public void put(String fieldName, Object fieldValue) {
        Schema schema = getSchema();
        if (!(schema instanceof RecordSchema)) {
            return;
        }
        Field field = ((RecordSchema) schema).getField(fieldName);
        if (field != null) {
            put(field.getPos(), fieldValue);
        }
    }
}
