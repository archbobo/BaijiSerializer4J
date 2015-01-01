package com.baijioss.serializer.specific;

import java.util.*;
import com.baijioss.serializer.exception.*;
import com.baijioss.serializer.schema.*;
import com.baijioss.serializer.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class ModelFilling extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"ModelFilling\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"stringfilling1\",\"type\":[\"string\",\"null\"]},{\"name\":\"stringfilling2\",\"type\":[\"string\",\"null\"]},{\"name\":\"stringfilling3\",\"type\":[\"string\",\"null\"]},{\"name\":\"stringfilling4\",\"type\":[\"string\",\"null\"]},{\"name\":\"intfilling\",\"type\":[\"int\",\"null\"]},{\"name\":\"boolfilling\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"modelfilling\",\"type\":[{\"type\":\"record\",\"name\":\"ModelFilling2\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"longfilling\",\"type\":[\"long\",\"null\"]},{\"name\":\"stringfilling\",\"type\":[\"string\",\"null\"]},{\"name\":\"listfilling\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"enumfilling\",\"type\":[{\"type\":\"enum\",\"name\":\"Enum2Values\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"symbols\":[\"CAR\",\"BIKE\",\"PLANE\"]},\"null\"]}]},\"null\"]},{\"name\":\"modelfilling3\",\"type\":[{\"type\":\"record\",\"name\":\"ModelFilling3\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"fields\":[{\"name\":\"intfilling\",\"type\":[\"int\",\"null\"]},{\"name\":\"doublefilling\",\"type\":[\"double\",\"null\"]},{\"name\":\"listsfilling\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":\"int\"}},\"null\"]},{\"name\":\"mapsfilling\",\"type\":[{\"type\":\"map\",\"values\":{\"type\":\"map\",\"values\":\"string\"}},\"null\"]}]},\"null\"]},{\"name\":\"enumfilling\",\"type\":[{\"type\":\"enum\",\"name\":\"Enum1Values\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"symbols\":[\"BLUE\",\"RED\",\"GREEN\"]},\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public ModelFilling(
        String stringfilling1,
        String stringfilling2,
        String stringfilling3,
        String stringfilling4,
        Integer intfilling,
        Boolean boolfilling,
        ModelFilling2 modelfilling,
        ModelFilling3 modelfilling3,
        Enum1Values enumfilling
    ) {
        this.stringfilling1 = stringfilling1;
        this.stringfilling2 = stringfilling2;
        this.stringfilling3 = stringfilling3;
        this.stringfilling4 = stringfilling4;
        this.intfilling = intfilling;
        this.boolfilling = boolfilling;
        this.modelfilling = modelfilling;
        this.modelfilling3 = modelfilling3;
        this.enumfilling = enumfilling;
    }

    public ModelFilling() {
    }

    public String stringfilling1;

    public String stringfilling2;

    public String stringfilling3;

    public String stringfilling4;

    public Integer intfilling;

    public Boolean boolfilling;

    public ModelFilling2 modelfilling;

    public ModelFilling3 modelfilling3;

    public Enum1Values enumfilling;

    public String getStringfilling1() {
        return stringfilling1;
    }

    public void setStringfilling1(final String stringfilling1) {
        this.stringfilling1 = stringfilling1;
    }

    public String getStringfilling2() {
        return stringfilling2;
    }

    public void setStringfilling2(final String stringfilling2) {
        this.stringfilling2 = stringfilling2;
    }

    public String getStringfilling3() {
        return stringfilling3;
    }

    public void setStringfilling3(final String stringfilling3) {
        this.stringfilling3 = stringfilling3;
    }

    public String getStringfilling4() {
        return stringfilling4;
    }

    public void setStringfilling4(final String stringfilling4) {
        this.stringfilling4 = stringfilling4;
    }

    public Integer getIntfilling() {
        return intfilling;
    }

    public void setIntfilling(final Integer intfilling) {
        this.intfilling = intfilling;
    }

    public Boolean isBoolfilling() {
        return boolfilling;
    }

    public void setBoolfilling(final Boolean boolfilling) {
        this.boolfilling = boolfilling;
    }

    public ModelFilling2 getModelfilling() {
        return modelfilling;
    }

    public void setModelfilling(final ModelFilling2 modelfilling) {
        this.modelfilling = modelfilling;
    }

    public ModelFilling3 getModelfilling3() {
        return modelfilling3;
    }

    public void setModelfilling3(final ModelFilling3 modelfilling3) {
        this.modelfilling3 = modelfilling3;
    }

    public Enum1Values getEnumfilling() {
        return enumfilling;
    }

    public void setEnumfilling(final Enum1Values enumfilling) {
        this.enumfilling = enumfilling;
    }

    // Used by DatumWriter. Applications should not call.
    public Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.stringfilling1;
            case 1: return this.stringfilling2;
            case 2: return this.stringfilling3;
            case 3: return this.stringfilling4;
            case 4: return this.intfilling;
            case 5: return this.boolfilling;
            case 6: return this.modelfilling;
            case 7: return this.modelfilling3;
            case 8: return this.enumfilling;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, Object fieldValue) {
        switch (fieldPos) {
            case 0: this.stringfilling1 = (String)fieldValue; break;
            case 1: this.stringfilling2 = (String)fieldValue; break;
            case 2: this.stringfilling3 = (String)fieldValue; break;
            case 3: this.stringfilling4 = (String)fieldValue; break;
            case 4: this.intfilling = (Integer)fieldValue; break;
            case 5: this.boolfilling = (Boolean)fieldValue; break;
            case 6: this.modelfilling = (ModelFilling2)fieldValue; break;
            case 7: this.modelfilling3 = (ModelFilling3)fieldValue; break;
            case 8: this.enumfilling = (Enum1Values)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final ModelFilling other = (ModelFilling)obj;
        return 
            Objects.equal(this.stringfilling1, other.stringfilling1) &&
            Objects.equal(this.stringfilling2, other.stringfilling2) &&
            Objects.equal(this.stringfilling3, other.stringfilling3) &&
            Objects.equal(this.stringfilling4, other.stringfilling4) &&
            Objects.equal(this.intfilling, other.intfilling) &&
            Objects.equal(this.boolfilling, other.boolfilling) &&
            Objects.equal(this.modelfilling, other.modelfilling) &&
            Objects.equal(this.modelfilling3, other.modelfilling3) &&
            Objects.equal(this.enumfilling, other.enumfilling);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.stringfilling1 == null ? 0 : this.stringfilling1.hashCode());
        result = 31 * result + (this.stringfilling2 == null ? 0 : this.stringfilling2.hashCode());
        result = 31 * result + (this.stringfilling3 == null ? 0 : this.stringfilling3.hashCode());
        result = 31 * result + (this.stringfilling4 == null ? 0 : this.stringfilling4.hashCode());
        result = 31 * result + (this.intfilling == null ? 0 : this.intfilling.hashCode());
        result = 31 * result + (this.boolfilling == null ? 0 : this.boolfilling.hashCode());
        result = 31 * result + (this.modelfilling == null ? 0 : this.modelfilling.hashCode());
        result = 31 * result + (this.modelfilling3 == null ? 0 : this.modelfilling3.hashCode());
        result = 31 * result + (this.enumfilling == null ? 0 : this.enumfilling.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("stringfilling1", stringfilling1)
            .add("stringfilling2", stringfilling2)
            .add("stringfilling3", stringfilling3)
            .add("stringfilling4", stringfilling4)
            .add("intfilling", intfilling)
            .add("boolfilling", boolfilling)
            .add("modelfilling", modelfilling)
            .add("modelfilling3", modelfilling3)
            .add("enumfilling", enumfilling)
            .toString();
    }
}
