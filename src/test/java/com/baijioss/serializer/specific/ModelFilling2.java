package com.baijioss.serializer.specific;

import java.util.*;
import com.baijioss.serializer.exception.*;
import com.baijioss.serializer.schema.*;
import com.baijioss.serializer.specific.*;
import com.google.common.base.Objects;
import org.codehaus.jackson.annotate.JsonIgnore;

@SuppressWarnings("all")
public class ModelFilling2 extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"ModelFilling2\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"longfilling\",\"type\":[\"long\",\"null\"]},{\"name\":\"stringfilling\",\"type\":[\"string\",\"null\"]},{\"name\":\"listfilling\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"enumfilling\",\"type\":[{\"type\":\"enum\",\"name\":\"Enum2Values\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"symbols\":[\"CAR\",\"BIKE\",\"PLANE\"]},\"null\"]}]}");

    @Override
    @JsonIgnore public Schema getSchema() { return SCHEMA; }

    public ModelFilling2(
        Long longfilling,
        String stringfilling,
        List<String> listfilling,
        Enum2Values enumfilling
    ) {
        this.longfilling = longfilling;
        this.stringfilling = stringfilling;
        this.listfilling = listfilling;
        this.enumfilling = enumfilling;
    }

    public ModelFilling2() {
    }

    public Long longfilling;

    public String stringfilling;

    public List<String> listfilling;

    public Enum2Values enumfilling;

    public Long getLongfilling() {
        return longfilling;
    }

    public void setLongfilling(final Long longfilling) {
        this.longfilling = longfilling;
    }

    public String getStringfilling() {
        return stringfilling;
    }

    public void setStringfilling(final String stringfilling) {
        this.stringfilling = stringfilling;
    }

    public List<String> getListfilling() {
        return listfilling;
    }

    public void setListfilling(final List<String> listfilling) {
        this.listfilling = listfilling;
    }

    public Enum2Values getEnumfilling() {
        return enumfilling;
    }

    public void setEnumfilling(final Enum2Values enumfilling) {
        this.enumfilling = enumfilling;
    }

    // Used by DatumWriter. Applications should not call.
    public Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.longfilling;
            case 1: return this.stringfilling;
            case 2: return this.listfilling;
            case 3: return this.enumfilling;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, Object fieldValue) {
        switch (fieldPos) {
            case 0: this.longfilling = (Long)fieldValue; break;
            case 1: this.stringfilling = (String)fieldValue; break;
            case 2: this.listfilling = (List<String>)fieldValue; break;
            case 3: this.enumfilling = (Enum2Values)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final ModelFilling2 other = (ModelFilling2)obj;
        return 
            Objects.equal(this.longfilling, other.longfilling) &&
            Objects.equal(this.stringfilling, other.stringfilling) &&
            Objects.equal(this.listfilling, other.listfilling) &&
            Objects.equal(this.enumfilling, other.enumfilling);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.longfilling == null ? 0 : this.longfilling.hashCode());
        result = 31 * result + (this.stringfilling == null ? 0 : this.stringfilling.hashCode());
        result = 31 * result + (this.listfilling == null ? 0 : this.listfilling.hashCode());
        result = 31 * result + (this.enumfilling == null ? 0 : this.enumfilling.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("longfilling", longfilling)
            .add("stringfilling", stringfilling)
            .add("listfilling", listfilling)
            .add("enumfilling", enumfilling)
            .toString();
    }
}
