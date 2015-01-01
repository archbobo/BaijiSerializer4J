package com.baijioss.serializer.specific;

import com.baijioss.serializer.exception.BaijiRuntimeException;
import com.baijioss.serializer.schema.Schema;
import com.google.common.base.Objects;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class TestSerializerSample extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"TestSerializerSample\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"int1\",\"type\":[\"int\",\"null\"]},{\"name\":\"tinyint1\",\"type\":[\"int\",\"null\"]},{\"name\":\"smallint1\",\"type\":[\"int\",\"null\"]},{\"name\":\"bigint1\",\"type\":[\"long\",\"null\"]},{\"name\":\"boolean1\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"double1\",\"type\":[\"double\",\"null\"]},{\"name\":\"string1\",\"type\":[\"string\",\"null\"]},{\"name\":\"record\",\"type\":[{\"type\":\"record\",\"name\":\"Record\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"sInt\",\"type\":[\"int\",\"null\"]},{\"name\":\"sBoolean\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"sString\",\"type\":[\"string\",\"null\"]}]},\"null\"]},{\"name\":\"list1\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"map1\",\"type\":[{\"type\":\"map\",\"values\":\"int\"},\"null\"]},{\"name\":\"enum1\",\"type\":[{\"type\":\"enum\",\"name\":\"Enum1Values\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"symbols\":[\"BLUE\",\"RED\",\"GREEN\"]},\"null\"]},{\"name\":\"nullableint\",\"type\":[\"int\",\"null\"]},{\"name\":\"bytes1\",\"type\":[\"bytes\",\"null\"]},{\"name\":\"container1\",\"type\":[{\"type\":\"record\",\"name\":\"Record2Container\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"record2list\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"Record2\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"enum2\",\"type\":[{\"type\":\"enum\",\"name\":\"Enum2Values\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"symbols\":[\"CAR\",\"BIKE\",\"PLANE\"]},\"null\"]},{\"name\":\"bigint2\",\"type\":[\"long\",\"null\"]},{\"name\":\"nullablebigint\",\"type\":[\"long\",\"null\"]},{\"name\":\"list2\",\"type\":[{\"type\":\"array\",\"items\":\"int\"},\"null\"]},{\"name\":\"map2\",\"type\":[{\"type\":\"map\",\"values\":\"Record\"},\"null\"]},{\"name\":\"byteslist\",\"type\":[{\"type\":\"array\",\"items\":\"bytes\"},\"null\"]},{\"name\":\"filling\",\"type\":[{\"type\":\"record\",\"name\":\"ModelFilling\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"stringfilling1\",\"type\":[\"string\",\"null\"]},{\"name\":\"stringfilling2\",\"type\":[\"string\",\"null\"]},{\"name\":\"stringfilling3\",\"type\":[\"string\",\"null\"]},{\"name\":\"stringfilling4\",\"type\":[\"string\",\"null\"]},{\"name\":\"intfilling\",\"type\":[\"int\",\"null\"]},{\"name\":\"boolfilling\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"modelfilling\",\"type\":[{\"type\":\"record\",\"name\":\"ModelFilling2\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"longfilling\",\"type\":[\"long\",\"null\"]},{\"name\":\"stringfilling\",\"type\":[\"string\",\"null\"]},{\"name\":\"listfilling\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"enumfilling\",\"type\":[\"Enum2Values\",\"null\"]}]},\"null\"]},{\"name\":\"modelfilling3\",\"type\":[{\"type\":\"record\",\"name\":\"ModelFilling3\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"intfilling\",\"type\":[\"int\",\"null\"]},{\"name\":\"doublefilling\",\"type\":[\"double\",\"null\"]},{\"name\":\"listsfilling\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":\"int\"}},\"null\"]},{\"name\":\"mapsfilling\",\"type\":[{\"type\":\"map\",\"values\":{\"type\":\"map\",\"values\":\"string\"}},\"null\"]}]},\"null\"]},{\"name\":\"enumfilling\",\"type\":[\"Enum1Values\",\"null\"]}]},\"null\"]}]}},\"null\"]}]},\"null\"]},{\"name\":\"innerSample\",\"type\":[\"TestSerializerSample\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public TestSerializerSample(
        Integer int1,
        Integer tinyint1,
        Integer smallint1,
        Long bigint1,
        Boolean boolean1,
        Double double1,
        String string1,
        Record record,
        List<String> list1,
        Map<String, Integer> map1,
        Enum1Values enum1,
        Integer nullableint,
        byte[] bytes1,
        Record2Container container1,
        TestSerializerSample innerSample
    ) {
        this.int1 = int1;
        this.tinyint1 = tinyint1;
        this.smallint1 = smallint1;
        this.bigint1 = bigint1;
        this.boolean1 = boolean1;
        this.double1 = double1;
        this.string1 = string1;
        this.record = record;
        this.list1 = list1;
        this.map1 = map1;
        this.enum1 = enum1;
        this.nullableint = nullableint;
        this.bytes1 = bytes1;
        this.container1 = container1;
        this.innerSample = innerSample;
    }

    public TestSerializerSample() {
    }

    public Integer int1;

    public Integer tinyint1;

    public Integer smallint1;

    public Long bigint1;

    public Boolean boolean1;

    public Double double1;

    public String string1;

    public Record record;

    public List<String> list1;

    public Map<String, Integer> map1;

    public Enum1Values enum1;

    public Integer nullableint;

    public byte[] bytes1;

    public Record2Container container1;

    public TestSerializerSample innerSample;

    public Integer getInt1() {
        return int1;
    }

    public void setInt1(final Integer int1) {
        this.int1 = int1;
    }

    public Integer getTinyint1() {
        return tinyint1;
    }

    public void setTinyint1(final Integer tinyint1) {
        this.tinyint1 = tinyint1;
    }

    public Integer getSmallint1() {
        return smallint1;
    }

    public void setSmallint1(final Integer smallint1) {
        this.smallint1 = smallint1;
    }

    public Long getBigint1() {
        return bigint1;
    }

    public void setBigint1(final Long bigint1) {
        this.bigint1 = bigint1;
    }

    public Boolean isBoolean1() {
        return boolean1;
    }

    public void setBoolean1(final Boolean boolean1) {
        this.boolean1 = boolean1;
    }

    public Double getDouble1() {
        return double1;
    }

    public void setDouble1(final Double double1) {
        this.double1 = double1;
    }

    public String getString1() {
        return string1;
    }

    public void setString1(final String string1) {
        this.string1 = string1;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(final Record record) {
        this.record = record;
    }

    public List<String> getList1() {
        return list1;
    }

    public void setList1(final List<String> list1) {
        this.list1 = list1;
    }

    public Map<String, Integer> getMap1() {
        return map1;
    }

    public void setMap1(final Map<String, Integer> map1) {
        this.map1 = map1;
    }

    public Enum1Values getEnum1() {
        return enum1;
    }

    public void setEnum1(final Enum1Values enum1) {
        this.enum1 = enum1;
    }

    public Integer getNullableint() {
        return nullableint;
    }

    public void setNullableint(final Integer nullableint) {
        this.nullableint = nullableint;
    }

    public byte[] getBytes1() {
        return bytes1;
    }

    public void setBytes1(final byte[] bytes1) {
        this.bytes1 = bytes1;
    }

    public Record2Container getContainer1() {
        return container1;
    }

    public void setContainer1(final Record2Container container1) {
        this.container1 = container1;
    }

    public TestSerializerSample getInnerSample() {
        return innerSample;
    }

    public void setInnerSample(final TestSerializerSample innerSample) {
        this.innerSample = innerSample;
    }

    // Used by DatumWriter. Applications should not call.
    public Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.int1;
            case 1: return this.tinyint1;
            case 2: return this.smallint1;
            case 3: return this.bigint1;
            case 4: return this.boolean1;
            case 5: return this.double1;
            case 6: return this.string1;
            case 7: return this.record;
            case 8: return this.list1;
            case 9: return this.map1;
            case 10: return this.enum1;
            case 11: return this.nullableint;
            case 12: return this.bytes1;
            case 13: return this.container1;
            case 14: return this.innerSample;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, Object fieldValue) {
        switch (fieldPos) {
            case 0: this.int1 = (Integer)fieldValue; break;
            case 1: this.tinyint1 = (Integer)fieldValue; break;
            case 2: this.smallint1 = (Integer)fieldValue; break;
            case 3: this.bigint1 = (Long)fieldValue; break;
            case 4: this.boolean1 = (Boolean)fieldValue; break;
            case 5: this.double1 = (Double)fieldValue; break;
            case 6: this.string1 = (String)fieldValue; break;
            case 7: this.record = (Record)fieldValue; break;
            case 8: this.list1 = (List<String>)fieldValue; break;
            case 9: this.map1 = (Map<String, Integer>)fieldValue; break;
            case 10: this.enum1 = (Enum1Values)fieldValue; break;
            case 11: this.nullableint = (Integer)fieldValue; break;
            case 12: this.bytes1 = (byte[])fieldValue; break;
            case 13: this.container1 = (Record2Container)fieldValue; break;
            case 14: this.innerSample = (TestSerializerSample)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final TestSerializerSample other = (TestSerializerSample)obj;
        return 
            Objects.equal(this.int1, other.int1) &&
            Objects.equal(this.tinyint1, other.tinyint1) &&
            Objects.equal(this.smallint1, other.smallint1) &&
            Objects.equal(this.bigint1, other.bigint1) &&
            Objects.equal(this.boolean1, other.boolean1) &&
            Objects.equal(this.double1, other.double1) &&
            Objects.equal(this.string1, other.string1) &&
            Objects.equal(this.record, other.record) &&
            Objects.equal(this.list1, other.list1) &&
            Objects.equal(this.map1, other.map1) &&
            Objects.equal(this.enum1, other.enum1) &&
            Objects.equal(this.nullableint, other.nullableint) &&
            Arrays.equals(this.bytes1, other.bytes1) &&
            Objects.equal(this.container1, other.container1) &&
            Objects.equal(this.innerSample, other.innerSample);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.int1 == null ? 0 : this.int1.hashCode());
        result = 31 * result + (this.tinyint1 == null ? 0 : this.tinyint1.hashCode());
        result = 31 * result + (this.smallint1 == null ? 0 : this.smallint1.hashCode());
        result = 31 * result + (this.bigint1 == null ? 0 : this.bigint1.hashCode());
        result = 31 * result + (this.boolean1 == null ? 0 : this.boolean1.hashCode());
        result = 31 * result + (this.double1 == null ? 0 : this.double1.hashCode());
        result = 31 * result + (this.string1 == null ? 0 : this.string1.hashCode());
        result = 31 * result + (this.record == null ? 0 : this.record.hashCode());
        result = 31 * result + (this.list1 == null ? 0 : this.list1.hashCode());
        result = 31 * result + (this.map1 == null ? 0 : this.map1.hashCode());
        result = 31 * result + (this.enum1 == null ? 0 : this.enum1.hashCode());
        result = 31 * result + (this.nullableint == null ? 0 : this.nullableint.hashCode());
        result = 31 * result + (this.bytes1 == null ? 0 : Arrays.hashCode(this.bytes1));
        result = 31 * result + (this.container1 == null ? 0 : this.container1.hashCode());
        result = 31 * result + (this.innerSample == null ? 0 : this.innerSample.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("int1", int1)
            .add("tinyint1", tinyint1)
            .add("smallint1", smallint1)
            .add("bigint1", bigint1)
            .add("boolean1", boolean1)
            .add("double1", double1)
            .add("string1", string1)
            .add("record", record)
            .add("list1", list1)
            .add("map1", map1)
            .add("enum1", enum1)
            .add("nullableint", nullableint)
            .add("bytes1", bytes1)
            .add("container1", container1)
            .add("innerSample", innerSample)
            .toString();
    }
}
