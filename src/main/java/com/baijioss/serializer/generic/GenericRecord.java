package com.baijioss.serializer.generic;

import com.baijioss.serializer.exception.BaijiRuntimeException;
import com.baijioss.serializer.schema.RecordSchema;
import com.baijioss.serializer.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

public class GenericRecord {

    private final RecordSchema _schema;

    private final Map<String, Object> _contents = new HashMap<String, Object>();

    public GenericRecord(RecordSchema schema) {
        _schema = schema;
    }

    public RecordSchema getSchema() {
        return _schema;
    }

    public void add(String fieldName, Object fieldValue) {
        if (!_schema.contains(fieldName)) {
            throw new BaijiRuntimeException("No such field: " + fieldName);
        }
        _contents.put(fieldName, fieldValue);
    }

    public Object get(String fieldName) {
        return _contents.get(fieldName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GenericRecord)) {
            return false;
        }
        GenericRecord other = (GenericRecord) obj;
        return _schema.equals(other._schema) && ObjectUtils.equals(_contents, other._contents);
    }

    @Override
    public int hashCode() {
        return 31 * _contents.hashCode() /* + 29 * _schema.hashCode() */;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Schema: ");
        sb.append(_schema);
        sb.append(", contents: ");
        sb.append("{ ");
        for (Map.Entry<String, Object> kv : _contents.entrySet()) {
            sb.append(kv.getKey());
            sb.append(": ");
            sb.append(kv.getValue());
            sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }
}
