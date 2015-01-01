package com.baijioss.serializer.generic;


import com.baijioss.serializer.schema.Schema;
import com.baijioss.serializer.specific.SpecificRecordBase;
import org.codehaus.jackson.annotate.JsonIgnore;

public class GenericBenchmarkRecord extends SpecificRecordBase {

    private Object fieldValue;

    public static String recordType;

    @Override
    @JsonIgnore public Schema getSchema() {
        String s = "{\"type\":\"record\",\"name\":\"GenericBenchmarkRecord\",\"namespace\":\"com.baijioss.serializer.generic\","
                + "\"fields\":[{\"name\":\"fieldValue\",\"type\": " + recordType + "}]}";
        return Schema.parse(s);
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Override
    public Object get(int fieldPos) {
        return fieldValue;
    }

    @Override
    public void put(int fieldPos, Object fieldValue) {
        this.fieldValue = fieldValue;
    }
}
