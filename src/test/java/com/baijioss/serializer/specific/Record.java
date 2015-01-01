package com.baijioss.serializer.specific;

import java.util.*;
import com.baijioss.serializer.exception.*;
import com.baijioss.serializer.schema.*;
import com.baijioss.serializer.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class Record extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"Record\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"sInt\",\"type\":[\"int\",\"null\"]},{\"name\":\"sBoolean\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"sString\",\"type\":[\"string\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public Record(
        Integer sInt,
        Boolean sBoolean,
        String sString
    ) {
        this.sInt = sInt;
        this.sBoolean = sBoolean;
        this.sString = sString;
    }

    public Record() {
    }

    public Integer sInt;

    public Boolean sBoolean;

    public String sString;

    public Integer getSInt() {
        return sInt;
    }

    public void setSInt(final Integer sInt) {
        this.sInt = sInt;
    }

    public Boolean isSBoolean() {
        return sBoolean;
    }

    public void setSBoolean(final Boolean sBoolean) {
        this.sBoolean = sBoolean;
    }

    public String getSString() {
        return sString;
    }

    public void setSString(final String sString) {
        this.sString = sString;
    }

    // Used by DatumWriter. Applications should not call.
    public Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.sInt;
            case 1: return this.sBoolean;
            case 2: return this.sString;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, Object fieldValue) {
        switch (fieldPos) {
            case 0: this.sInt = (Integer)fieldValue; break;
            case 1: this.sBoolean = (Boolean)fieldValue; break;
            case 2: this.sString = (String)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final Record other = (Record)obj;
        return 
            Objects.equal(this.sInt, other.sInt) &&
            Objects.equal(this.sBoolean, other.sBoolean) &&
            Objects.equal(this.sString, other.sString);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.sInt == null ? 0 : this.sInt.hashCode());
        result = 31 * result + (this.sBoolean == null ? 0 : this.sBoolean.hashCode());
        result = 31 * result + (this.sString == null ? 0 : this.sString.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("sInt", sInt)
            .add("sBoolean", sBoolean)
            .add("sString", sString)
            .toString();
    }
}
