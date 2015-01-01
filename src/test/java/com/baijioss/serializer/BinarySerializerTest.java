package com.baijioss.serializer;

import com.baijioss.serializer.exception.BaijiRuntimeException;
import com.baijioss.serializer.schema.RecordSchema;
import com.baijioss.serializer.specific.SpecificRecord;
import com.baijioss.serializer.specific.SpecificRecordBase;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BinarySerializerTest {

    @Test
    public void testEnumResolution() throws IOException {
        EnumRecord testRecord = new EnumRecord();

        testRecord.enumType = EnumType.SECOND;
        testRecord.enumTypeExplicit = EnumTypeExplicit.SECOND;

        // serialize
        byte[] data = serialize(testRecord);

        // deserialize
        EnumRecord rec2 = deserialize(data, EnumRecord.class);
        assertEquals(testRecord.enumType, rec2.enumType);
        assertEquals(testRecord.enumTypeExplicit, rec2.enumTypeExplicit);
    }

    private static <S extends SpecificRecord> S deserialize(byte[] data, Class<S> clazz)
            throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        Serializer serializer = new BinarySerializer();
        S output = serializer.deserialize(clazz, is);
        assertEquals(0, is.available()); // Ensure we have read everything.
        checkAlternateDeserializers(output, clazz, data);
        return output;
    }

    private static <S extends SpecificRecord> void checkAlternateDeserializers(S expected, Class<S> clazz, byte[] data)
            throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        Serializer serializer = new BinarySerializer();
        S output = serializer.deserialize(clazz, is);
        assertEquals(0, is.available()); // Ensure we have read everything.
        assertSpecificRecordEqual(expected, output);
    }

    private static void assertSpecificRecordEqual(SpecificRecord rec1, SpecificRecord rec2) {
        RecordSchema recordSchema = (RecordSchema) rec1.getSchema();
        for (int i = 0; i < recordSchema.size(); i++) {
            Object rec1Val = rec1.get(i);
            Object rec2Val = rec2.get(i);
            if (rec1Val instanceof SpecificRecord) {
                assertSpecificRecordEqual((SpecificRecord) rec1Val, (SpecificRecord) rec2Val);
            } else if (rec1Val instanceof List) {
                List rec1List = (List) rec1Val;
                if (!rec1List.isEmpty() && rec1List.get(0) instanceof SpecificRecord) {
                    List rec2List = (List) rec2Val;
                    assertEquals(rec1List.size(), rec2List.size());
                    for (int j = 0; j < rec1List.size(); j++) {
                        assertSpecificRecordEqual((SpecificRecord) rec1List.get(j), (SpecificRecord) rec2List.get(j));
                    }
                } else {
                    assertEquals(rec1Val, rec2Val);
                }
            } else if (rec1Val instanceof Map) {
                Map rec1Dict = (Map) rec1Val;
                Map rec2Dict = (Map) rec2Val;
                assertEquals(rec2Dict.size(), rec2Dict.size());
                for (Object key : rec1Dict.keySet()) {
                    Object val1 = rec1Dict.get(key);
                    Object val2 = rec2Dict.get(key);
                    if (val1 instanceof SpecificRecord) {
                        assertSpecificRecordEqual((SpecificRecord) val1, (SpecificRecord) val2);
                    } else {
                        assertEquals(val1, val2);
                    }
                }
            } else {
                assertEquals(rec1Val, rec2Val);
            }
        }
    }

    private static <T extends SpecificRecord> byte[] serialize(T actual) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Serializer serliazer = new BinarySerializer();
        serliazer.serialize(actual, os);
        byte[] data = os.toByteArray();
        checkAlternateSerializers(data, actual);
        return data;
    }

    private static <T extends SpecificRecord> void checkAlternateSerializers(byte[] expected, T value)
            throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Serializer serliazer = new BinarySerializer();
        serliazer.serialize(value, os);
        byte[] output = os.toByteArray();

        assertEquals(expected.length, output.length);
        assertArrayEquals(expected, output);
    }
}

enum EnumType {
    FIRST(0),
    SECOND(1),
    THIRD(2),
    FOUR(3);

    private final int _value;

    EnumType(int value) {
        _value = value;
    }

    public int getValue() {
        return _value;
    }

    public static EnumType findByValue(int value) {
        switch (value) {
            case 0:
                return FIRST;
            case 1:
                return SECOND;
            case 2:
                return THIRD;
            case 3:
                return FOUR;
            default:
                return null;
        }
    }
}

enum EnumTypeExplicit {
    THIRD(3),
    FOUR(4),
    FIRST(1),
    SECOND(2);

    private final int _value;

    EnumTypeExplicit(int value) {
        _value = value;
    }

    public int getValue() {
        return _value;
    }

    public static EnumTypeExplicit findByValue(int value) {
        switch (value) {
            case 1:
                return FIRST;
            case 2:
                return SECOND;
            case 3:
                return THIRD;
            case 4:
                return FOUR;
            default:
                return null;
        }
    }
}

class EnumRecord extends SpecificRecordBase {
    public static final com.baijioss.serializer.schema.Schema SCHEMA = com.baijioss.serializer.schema.Schema.parse(
            "{\"type\":\"record\",\"name\":\"EnumRecord\",\"namespace\":\"com.baijioss.serializer\"," +
                    "\"fields\":[{\"name\":\"enumType\",\"type\": { \"type\": \"enum\", \"name\":" +
                    " \"EnumType\", \"symbols\": [\"FIRST\", \"SECOND\", \"THIRD\", \"FOUR\"]} }," +
                    "{\"name\":\"enumTypeExplicit\",\"type\": { \"type\": \"enum\", \"name\":" +
                    " \"EnumTypeExplicit\", \"symbols\": [{\"name\": \"THIRD\", \"value\": 3}, \"FOUR\", {\"name\": \"FIRST\", \"value\": 1}, \"SECOND\"]} }]}");

    public EnumType enumType;

    public EnumTypeExplicit enumTypeExplicit;

    public EnumRecord() {
    }

    public com.baijioss.serializer.schema.Schema getSchema() {
        return SCHEMA;
    }

    public EnumType getEnumType() {
        return enumType;
    }

    public void setEnumType(EnumType enumType) {
        this.enumType = enumType;
    }

    public EnumTypeExplicit getEnumTypeExplicit() {
        return enumTypeExplicit;
    }

    public void setEnumTypeExplicit(EnumTypeExplicit enumTypeExplicit) {
        this.enumTypeExplicit = enumTypeExplicit;
    }

    @Override
    public Object get(int fieldPos) {
        switch (fieldPos) {
            case 0:
                return enumType;
            case 1:
                return enumTypeExplicit;
            default:
                throw new BaijiRuntimeException("Bad index " + fieldPos + " in Get()");
        }
    }

    @Override
    public void put(int fieldPos, Object fieldValue) {
        switch (fieldPos) {
            case 0:
                enumType = (EnumType) fieldValue;
                break;
            case 1:
                enumTypeExplicit = (EnumTypeExplicit) fieldValue;
                break;
            default:
                throw new BaijiRuntimeException("Bad index " + fieldPos + " in Put()");
        }
    }
}
