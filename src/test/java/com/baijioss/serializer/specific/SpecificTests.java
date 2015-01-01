package com.baijioss.serializer.specific;

import com.baijioss.serializer.exception.BaijiRuntimeException;
import com.baijioss.serializer.generic.DatumReader;
import com.baijioss.serializer.generic.DatumWriter;
import com.baijioss.serializer.io.BinaryDecoder;
import com.baijioss.serializer.io.BinaryEncoder;
import com.baijioss.serializer.io.Decoder;
import com.baijioss.serializer.io.Encoder;
import com.baijioss.serializer.schema.RecordSchema;
import com.baijioss.serializer.schema.Schema;
import com.google.common.base.Objects;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SpecificTests {

    @Test
    public void testEnumResolution() throws Exception {
        EnumRecord testRecord = new EnumRecord();
        testRecord.enumType = EnumType.SECOND;

        // Serialize
        byte[] data = serialize(testRecord, EnumRecord.class, EnumRecord.SCHEMA);

        // Deserialize
        EnumRecord rec2 = deserialize(data, EnumRecord.class, EnumRecord.SCHEMA);
        assertEquals(testRecord.enumType, rec2.enumType);
    }

    @Test
    public void testRecordNullFields() throws Exception {
        TestRecord testRecord = new TestRecord();
        // Serialize
        byte[] data = serialize(testRecord, TestRecord.class, TestRecord.SCHEMA);

        // Deserialize
        TestRecord rec2 = deserialize(data, TestRecord.class, TestRecord.SCHEMA);
        assertEquals(testRecord, rec2);
    }

    @Test
    public void testRecordValidFields() throws Exception {
        List<Integer> list = new ArrayList<Integer>();
        list.add(Integer.MIN_VALUE);
        list.add(-1);
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(5);
        list.add(8);
        list.add(13);
        list.add(Integer.MAX_VALUE);
        Map<String, String> map = new HashMap<String, String>();
        map.put("Name", "Value");
        map.put("Beijing", "China");
        map.put("San Francisco", "USA");
        InnerRecord innerRecord = new InnerRecord(777, "Baiji Test");
        byte[] binary = new byte[]{1, 2, 3, 4, 5, -100};
        TestRecord testRecord = new TestRecord(true, 123456, 9876543211234567L, 12341981.1234234f, 1273891231.787,
                innerRecord, new TestRecord(), list, map, binary);
        // Serialize
        byte[] data = serialize(testRecord, TestRecord.class, TestRecord.SCHEMA);

        // Deserialize
        TestRecord rec2 = deserialize(data, TestRecord.class, TestRecord.SCHEMA);
        assertEquals(testRecord, rec2);
    }

    private static <S extends SpecificRecord> S deserialize(byte[] data, Class<S> clazz, Schema schema)
            throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        DatumReader<S> r = new SpecificDatumReader<S>(schema);
        Decoder d = new BinaryDecoder(is);
        S output = r.read(null, d);
        assertEquals(0, is.available()); // Ensure we have read everything.
        checkAlternateDeserializers(output, data, clazz, schema);
        return output;
    }

    private static <S extends SpecificRecord> void checkAlternateDeserializers(S expected, byte[] data, Class<S> clazz,
                                                                               Schema schema)
            throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        DatumReader<S> r = new SpecificDatumReader<S>(schema);
        Decoder d = new BinaryDecoder(is);
        S output = r.read(null, d);
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
            } else if (rec1Val instanceof  byte[]){
                assertArrayEquals((byte[])rec1Val, (byte[])rec2Val);
            } else {
                assertEquals(rec1Val, rec2Val);
            }
        }
    }

    private static <T> byte[] serialize(T actual, Class<?> clazz, Schema schema) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Encoder e = new BinaryEncoder(os);
        DatumWriter<T> w = new SpecificDatumWriter<T>(schema);
        w.write(actual, e);
        byte[] data = os.toByteArray();
        checkAlternateSerializers(data, actual, schema);
        return data;
    }

    private static void checkAlternateSerializers(byte[] expected, Object value, Schema schema)
            throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Encoder e = new BinaryEncoder(os);
        DatumWriter w = new SpecificDatumWriter(schema);
        w.write(value, e);
        byte[] output = os.toByteArray();

        assertEquals(expected.length, output.length);
        assertArrayEquals(expected, output);
    }
}

enum EnumType {
    THIRD(0),
    FIRST(1),
    SECOND(2);

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
                return THIRD;
            case 1:
                return FIRST;
            case 2:
                return SECOND;
            default:
                return null;
        }
    }
}

class EnumRecord extends SpecificRecordBase {
    public static final com.baijioss.serializer.schema.Schema SCHEMA = com.baijioss.serializer.schema.Schema.parse(
            "{\"type\":\"record\",\"name\":\"EnumRecord\",\"namespace\":\"com.baijioss.serializer.specific\"," +
                    "\"fields\":[{\"name\":\"enumType\",\"type\": { \"type\": \"enum\", \"name\":" +
                    " \"EnumType\", \"symbols\": [\"THIRD\", \"FIRST\", \"SECOND\"]} }]}");
    public EnumType enumType;

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

    @Override
    public Object get(int fieldPos) {
        switch (fieldPos) {
            case 0:
                return enumType;
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
            default:
                throw new BaijiRuntimeException("Bad index " + fieldPos + " in Put()");
        }
    }
}

@SuppressWarnings("all")
class InnerRecord extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"InnerRecord\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"id\",\"type\":[\"int\",\"null\"]},{\"name\":\"name\",\"type\":[\"string\",\"null\"]}]}");

    @Override
    public Schema getSchema() {
        return SCHEMA;
    }

    public InnerRecord(
            Integer id,
            String name
    ) {
        this.id = id;
        this.name = name;
    }

    public InnerRecord() {
    }

    public Integer id;

    public String name;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0:
                return this.id;
            case 1:
                return this.name;
            default:
                throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value = "unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0:
                this.id = (Integer) fieldValue;
                break;
            case 1:
                this.name = (String) fieldValue;
                break;
            default:
                throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final InnerRecord other = (InnerRecord) obj;
        return
                Objects.equal(this.id, other.id) &&
                        Objects.equal(this.name, other.name);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (this.id == null ? 0 : this.id.hashCode());
        result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
        return result;
    }
}

@SuppressWarnings("all")
class TestRecord extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"TestRecord\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"flag\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"num1\",\"type\":[\"int\",\"null\"]},{\"name\":\"num2\",\"type\":[\"long\",\"null\"]},{\"name\":\"realNum1\",\"type\":[\"float\",\"null\"]},{\"name\":\"realNum2\",\"type\":[\"double\",\"null\"]},{\"name\":\"record\",\"type\":[{\"type\":\"record\",\"name\":\"InnerRecord\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"id\",\"type\":[\"int\",\"null\"]},{\"name\":\"name\",\"type\":[\"string\",\"null\"]}]},\"null\"]},{\"name\":\"parent\",\"type\":[\"TestRecord\",\"null\"]},{\"name\":\"nums\",\"type\":[{\"type\":\"array\",\"items\":\"int\"},\"null\"]},{\"name\":\"names\",\"type\":[{\"type\":\"map\",\"values\":\"string\"},\"null\"]},{\"name\":\"data\",\"type\":[\"bytes\",\"null\"]}]}");

    @Override
    public Schema getSchema() {
        return SCHEMA;
    }

    public TestRecord(
            Boolean flag,
            Integer num1,
            Long num2,
            Float realNum1,
            Double realNum2,
            InnerRecord record,
            TestRecord parent,
            List<Integer> nums,
            Map<String, String> names,
            byte[] data
    ) {
        this.flag = flag;
        this.num1 = num1;
        this.num2 = num2;
        this.realNum1 = realNum1;
        this.realNum2 = realNum2;
        this.record = record;
        this.parent = parent;
        this.nums = nums;
        this.names = names;
        this.data = data;
    }

    public TestRecord() {
    }

    public Boolean flag;

    public Integer num1;

    public Long num2;

    public Float realNum1;

    public Double realNum2;

    public InnerRecord record;

    public TestRecord parent;

    public List<Integer> nums;

    public Map<String, String> names;

    private byte[] data;

    public Boolean isFlag() {
        return flag;
    }

    public void setFlag(final Boolean flag) {
        this.flag = flag;
    }

    public Integer getNum1() {
        return num1;
    }

    public void setNum1(final Integer num1) {
        this.num1 = num1;
    }

    public Long getNum2() {
        return num2;
    }

    public void setNum2(final Long num2) {
        this.num2 = num2;
    }

    public Float getRealNum1() {
        return realNum1;
    }

    public void setRealNum2(final Float realNum1) {
        this.realNum1 = realNum1;
    }

    public Double getRealNum2() {
        return realNum2;
    }

    public void setRealNum2(final Double realNum2) {
        this.realNum2 = realNum2;
    }

    public InnerRecord getRecord() {
        return record;
    }

    public void setRecord(final InnerRecord record) {
        this.record = record;
    }

    public TestRecord getParent() {
        return parent;
    }

    public void setParent(final TestRecord parent) {
        this.parent = parent;
    }

    public List<Integer> getNums() {
        return nums;
    }

    public void setNums(final List<Integer> nums) {
        this.nums = nums;
    }

    public Map<String, String> getNames() {
        return names;
    }

    public void setNames(final Map<String, String> names) {
        this.names = names;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(final byte[] data) {
        this.data = data;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0:
                return this.flag;
            case 1:
                return this.num1;
            case 2:
                return this.num2;
            case 3:
                return this.realNum1;
            case 4:
                return this.realNum2;
            case 5:
                return this.record;
            case 6:
                return this.parent;
            case 7:
                return this.nums;
            case 8:
                return this.names;
            case 9:
                return this.data;
            default:
                throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value = "unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0:
                this.flag = (Boolean) fieldValue;
                break;
            case 1:
                this.num1 = (Integer) fieldValue;
                break;
            case 2:
                this.num2 = (Long) fieldValue;
                break;
            case 3:
                this.realNum1 = (Float) fieldValue;
                break;
            case 4:
                this.realNum2 = (Double) fieldValue;
                break;
            case 5:
                this.record = (InnerRecord) fieldValue;
                break;
            case 6:
                this.parent = (TestRecord) fieldValue;
                break;
            case 7:
                this.nums = (List<Integer>) fieldValue;
                break;
            case 8:
                this.names = (Map<String, String>) fieldValue;
                break;
            case 9:
                this.data = (byte[]) fieldValue;
                break;
            default:
                throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final TestRecord other = (TestRecord) obj;
        return
                Objects.equal(this.flag, other.flag) &&
                        Objects.equal(this.num1, other.num1) &&
                        Objects.equal(this.num2, other.num2) &&
                        Objects.equal(this.realNum1, other.realNum1) &&
                        Objects.equal(this.realNum2, other.realNum2) &&
                        Objects.equal(this.record, other.record) &&
                        Objects.equal(this.parent, other.parent) &&
                        Objects.equal(this.nums, other.nums) &&
                        Objects.equal(this.names, other.names) &&
                        Arrays.equals(this.data, other.data);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (this.flag == null ? 0 : this.flag.hashCode());
        result = 31 * result + (this.num1 == null ? 0 : this.num1.hashCode());
        result = 31 * result + (this.num2 == null ? 0 : this.num2.hashCode());
        result = 31 * result + (this.realNum1 == null ? 0 : this.realNum1.hashCode());
        result = 31 * result + (this.realNum2 == null ? 0 : this.realNum2.hashCode());
        result = 31 * result + (this.record == null ? 0 : this.record.hashCode());
        result = 31 * result + (this.parent == null ? 0 : this.parent.hashCode());
        result = 31 * result + (this.nums == null ? 0 : this.nums.hashCode());
        result = 31 * result + (this.names == null ? 0 : this.names.hashCode());
        result = 31 * result + (this.data == null ? 0 : Arrays.hashCode(data));
        return result;
    }
}
