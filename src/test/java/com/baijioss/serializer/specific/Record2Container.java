package com.baijioss.serializer.specific;

import java.util.*;
import com.baijioss.serializer.exception.*;
import com.baijioss.serializer.schema.*;
import com.baijioss.serializer.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class Record2Container extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"Record2Container\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"record2list\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"Record2\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"enum2\",\"type\":[{\"type\":\"enum\",\"name\":\"Enum2Values\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"symbols\":[\"CAR\",\"BIKE\",\"PLANE\"]},\"null\"]},{\"name\":\"bigint2\",\"type\":[\"long\",\"null\"]},{\"name\":\"nullablebigint\",\"type\":[\"long\",\"null\"]},{\"name\":\"list2\",\"type\":[{\"type\":\"array\",\"items\":\"int\"},\"null\"]},{\"name\":\"map2\",\"type\":[{\"type\":\"map\",\"values\":{\"type\":\"record\",\"name\":\"Record\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"sInt\",\"type\":[\"int\",\"null\"]},{\"name\":\"sBoolean\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"sString\",\"type\":[\"string\",\"null\"]}]}},\"null\"]},{\"name\":\"byteslist\",\"type\":[{\"type\":\"array\",\"items\":\"bytes\"},\"null\"]},{\"name\":\"filling\",\"type\":[{\"type\":\"record\",\"name\":\"ModelFilling\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"stringfilling1\",\"type\":[\"string\",\"null\"]},{\"name\":\"stringfilling2\",\"type\":[\"string\",\"null\"]},{\"name\":\"stringfilling3\",\"type\":[\"string\",\"null\"]},{\"name\":\"stringfilling4\",\"type\":[\"string\",\"null\"]},{\"name\":\"intfilling\",\"type\":[\"int\",\"null\"]},{\"name\":\"boolfilling\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"modelfilling\",\"type\":[{\"type\":\"record\",\"name\":\"ModelFilling2\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"longfilling\",\"type\":[\"long\",\"null\"]},{\"name\":\"stringfilling\",\"type\":[\"string\",\"null\"]},{\"name\":\"listfilling\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"enumfilling\",\"type\":[\"Enum2Values\",\"null\"]}]},\"null\"]},{\"name\":\"modelfilling3\",\"type\":[{\"type\":\"record\",\"name\":\"ModelFilling3\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"intfilling\",\"type\":[\"int\",\"null\"]},{\"name\":\"doublefilling\",\"type\":[\"double\",\"null\"]},{\"name\":\"listsfilling\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":\"int\"}},\"null\"]},{\"name\":\"mapsfilling\",\"type\":[{\"type\":\"map\",\"values\":{\"type\":\"map\",\"values\":\"string\"}},\"null\"]}]},\"null\"]},{\"name\":\"enumfilling\",\"type\":[{\"type\":\"enum\",\"name\":\"Enum1Values\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"symbols\":[\"BLUE\",\"RED\",\"GREEN\"]},\"null\"]}]},\"null\"]}]}},\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public Record2Container(
        List<Record2> record2list
    ) {
        this.record2list = record2list;
    }

    public Record2Container() {
    }

    public List<Record2> record2list;

    public List<Record2> getRecord2list() {
        return record2list;
    }

    public void setRecord2list(final List<Record2> record2list) {
        this.record2list = record2list;
    }

    // Used by DatumWriter. Applications should not call.
    public Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.record2list;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, Object fieldValue) {
        switch (fieldPos) {
            case 0: this.record2list = (List<Record2>)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final Record2Container other = (Record2Container)obj;
        return 
            Objects.equal(this.record2list, other.record2list);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.record2list == null ? 0 : this.record2list.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("record2list", record2list)
            .toString();
    }
}
