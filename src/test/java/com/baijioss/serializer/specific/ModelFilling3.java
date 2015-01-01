package com.baijioss.serializer.specific;

import java.util.*;
import com.baijioss.serializer.exception.*;
import com.baijioss.serializer.schema.*;
import com.baijioss.serializer.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class ModelFilling3 extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"ModelFilling3\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"intfilling\",\"type\":[\"int\",\"null\"]},{\"name\":\"doublefilling\",\"type\":[\"double\",\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public ModelFilling3(
        Integer intfilling,
        Double doublefilling,
        List<List<Integer>> listsfilling,
        Map<String, Map<String, String>> mapsfilling
    ) {
        this.intfilling = intfilling;
        this.doublefilling = doublefilling;
        this.listsfilling = listsfilling;
        this.mapsfilling = mapsfilling;
    }

    public ModelFilling3() {
    }

    public Integer intfilling;

    public Double doublefilling;

    public List<List<Integer>> listsfilling;

    public Map<String, Map<String, String>> mapsfilling;

    public Integer getIntfilling() {
        return intfilling;
    }

    public void setIntfilling(final Integer intfilling) {
        this.intfilling = intfilling;
    }

    public Double getDoublefilling() {
        return doublefilling;
    }

    public void setDoublefilling(final Double doublefilling) {
        this.doublefilling = doublefilling;
    }

    public List<List<Integer>> getListsfilling() {
        return listsfilling;
    }

    public void setListsfilling(final List<List<Integer>> listsfilling) {
        this.listsfilling = listsfilling;
    }

    public Map<String, Map<String, String>> getMapsfilling() {
        return mapsfilling;
    }

    public void setMapsfilling(final Map<String, Map<String, String>> mapsfilling) {
        this.mapsfilling = mapsfilling;
    }

    // Used by DatumWriter. Applications should not call.
    public Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.intfilling;
            case 1: return this.doublefilling;
            case 2: return this.listsfilling;
            case 3: return this.mapsfilling;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, Object fieldValue) {
        switch (fieldPos) {
            case 0: this.intfilling = (Integer)fieldValue; break;
            case 1: this.doublefilling = (Double)fieldValue; break;
            case 2: this.listsfilling = (List<List<Integer>>)fieldValue; break;
            case 3: this.mapsfilling = (Map<String, Map<String, String>>)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final ModelFilling3 other = (ModelFilling3)obj;
        return
            Objects.equal(this.intfilling, other.intfilling) &&
            Objects.equal(this.doublefilling, other.doublefilling) &&
            Objects.equal(this.listsfilling, other.listsfilling) &&
            Objects.equal(this.mapsfilling, other.mapsfilling);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.intfilling == null ? 0 : this.intfilling.hashCode());
        result = 31 * result + (this.doublefilling == null ? 0 : this.doublefilling.hashCode());
        result = 31 * result + (this.listsfilling == null ? 0 : this.listsfilling.hashCode());
        result = 31 * result + (this.mapsfilling == null ? 0 : this.mapsfilling.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("intfilling", intfilling)
            .add("doublefilling", doublefilling)
            .add("listsfilling", listsfilling)
            .add("mapsfilling", mapsfilling)
            .toString();
    }
}
