package com.baijioss.serializer.specific;

import com.baijioss.serializer.exception.BaijiRuntimeException;
import com.baijioss.serializer.schema.Schema;
import com.google.common.base.Objects;

import java.util.List;
import java.util.Map;

public class Record3 extends SpecificRecordBase implements SpecificRecord {

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"Record2\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"enum2\",\"type\":[{\"type\":\"enum\",\"name\":\"Enum2Values\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"symbols\":[\"CAR\",\"BIKE\",\"PLANE\"]},\"null\"]},{\"name\":\"bigint2\",\"type\":[\"float\",\"null\"]},{\"name\":\"nullablebigint\",\"type\":[\"long\",\"null\"]},{\"name\":\"list2\",\"type\":[{\"type\":\"array\",\"items\":\"int\"},\"null\"]},{\"name\":\"map2\",\"type\":[{\"type\":\"map\",\"values\":{\"type\":\"record\",\"name\":\"Record\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"sInt\",\"type\":[\"int\",\"null\"]},{\"name\":\"sBoolean\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"sString\",\"type\":[\"string\",\"null\"]}]}},\"null\"]},{\"name\":\"byteslist\",\"type\":[{\"type\":\"array\",\"items\":\"bytes\"},\"null\"]},{\"name\":\"filling\",\"type\":[{\"type\":\"record\",\"name\":\"ModelFilling\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"stringfilling1\",\"type\":[\"string\",\"null\"]},{\"name\":\"stringfilling2\",\"type\":[\"string\",\"null\"]},{\"name\":\"stringfilling3\",\"type\":[\"string\",\"null\"]},{\"name\":\"stringfilling4\",\"type\":[\"string\",\"null\"]},{\"name\":\"intfilling\",\"type\":[\"int\",\"null\"]},{\"name\":\"boolfilling\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"modelfilling\",\"type\":[{\"type\":\"record\",\"name\":\"ModelFilling2\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"longfilling\",\"type\":[\"long\",\"null\"]},{\"name\":\"stringfilling\",\"type\":[\"string\",\"null\"]},{\"name\":\"listfilling\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"enumfilling\",\"type\":[\"Enum2Values\",\"null\"]}]},\"null\"]},{\"name\":\"modelfilling3\",\"type\":[{\"type\":\"record\",\"name\":\"ModelFilling3\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"intfilling\",\"type\":[\"int\",\"null\"]},{\"name\":\"doublefilling\",\"type\":[\"double\",\"null\"]},{\"name\":\"listsfilling\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":\"int\"}},\"null\"]},{\"name\":\"mapsfilling\",\"type\":[{\"type\":\"map\",\"values\":{\"type\":\"map\",\"values\":\"string\"}},\"null\"]}]},\"null\"]},{\"name\":\"enumfilling\",\"type\":[{\"type\":\"enum\",\"name\":\"Enum1Values\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"symbols\":[\"BLUE\",\"RED\",\"GREEN\"]},\"null\"]}]},\"null\"]}]}");

    @Override
    public Schema getSchema() {
        return SCHEMA;
    }

    public Record3() {
    }

    public Record3(
        Enum2Values enum2,
        Float bigint2,
        Long nullablebigint,
        List<Integer> list2,
        Map<String, Record> map2,
        List<byte[]> byteslist,
        ModelFilling filling
    ) {
        this.enum2 = enum2;
        this.bigint2 = bigint2;
        this.nullablebigint = nullablebigint;
        this.list2 = list2;
        this.map2 = map2;
        this.byteslist = byteslist;
        this.filling = filling;
    }

    @Override
    public Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.enum2;
            case 1: return this.bigint2;
            case 2: return this.nullablebigint;
            case 3: return this.list2;
            case 4: return this.map2;
            case 5: return this.byteslist;
            case 6: return this.filling;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    @Override
    public void put(int fieldPos, Object fieldValue) {
        switch (fieldPos) {
            case 0: this.enum2 = (Enum2Values)fieldValue; break;
            case 1: this.bigint2 = (Float)fieldValue; break;
            case 2: this.nullablebigint = (Long)fieldValue; break;
            case 3: this.list2 = (List<Integer>)fieldValue; break;
            case 4: this.map2 = (Map<String, Record>)fieldValue; break;
            case 5: this.byteslist = (List<byte[]>)fieldValue; break;
            case 6: this.filling = (ModelFilling)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    public Enum2Values enum2;

    public Float bigint2;

    public Long nullablebigint;

    public List<Integer> list2;

    public Map<String, Record> map2;

    public List<byte[]> byteslist;

    public ModelFilling filling;

    public Enum2Values getEnum2() {
        return enum2;
    }

    public void setEnum2(final Enum2Values enum2) {
        this.enum2 = enum2;
    }

    public Float getBigint2() {
        return bigint2;
    }

    public void setBigint2(final Float bigint2) {
        this.bigint2 = bigint2;
    }

    public Long getNullablebigint() {
        return nullablebigint;
    }

    public void setNullablebigint(final Long nullablebigint) {
        this.nullablebigint = nullablebigint;
    }

    public List<Integer> getList2() {
        return list2;
    }

    public void setList2(final List<Integer> list2) {
        this.list2 = list2;
    }

    public Map<String, Record> getMap2() {
        return map2;
    }

    public void setMap2(final Map<String, Record> map2) {
        this.map2 = map2;
    }

    public List<byte[]> getByteslist() {
        return byteslist;
    }

    public void setByteslist(final List<byte[]> byteslist) {
        this.byteslist = byteslist;
    }

    public ModelFilling getFilling() {
        return filling;
    }

    public void setFilling(final ModelFilling filling) {
        this.filling = filling;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final Record2 other = (Record2)obj;
        return
                Objects.equal(this.enum2, other.enum2) &&
                        Objects.equal(this.bigint2, other.bigint2) &&
                        Objects.equal(this.nullablebigint, other.nullablebigint) &&
                        Objects.equal(this.list2, other.list2) &&
                        Objects.equal(this.map2, other.map2) &&
                        Objects.equal(this.byteslist, other.byteslist) &&
                        Objects.equal(this.filling, other.filling);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.enum2 == null ? 0 : this.enum2.hashCode());
        result = 31 * result + (this.bigint2 == null ? 0 : this.bigint2.hashCode());
        result = 31 * result + (this.nullablebigint == null ? 0 : this.nullablebigint.hashCode());
        result = 31 * result + (this.list2 == null ? 0 : this.list2.hashCode());
        result = 31 * result + (this.map2 == null ? 0 : this.map2.hashCode());
        result = 31 * result + (this.byteslist == null ? 0 : this.byteslist.hashCode());
        result = 31 * result + (this.filling == null ? 0 : this.filling.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("enum2", enum2)
                .add("bigint2", bigint2)
                .add("nullablebigint", nullablebigint)
                .add("list2", list2)
                .add("map2", map2)
                .add("byteslist", byteslist)
                .add("filling", filling)
                .toString();
    }
}
