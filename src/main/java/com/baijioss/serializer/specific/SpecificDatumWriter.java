package com.baijioss.serializer.specific;

import com.baijioss.serializer.exception.BaijiRuntimeException;
import com.baijioss.serializer.exception.BaijiTypeException;
import com.baijioss.serializer.generic.PreresolvingDatumWriter;
import com.baijioss.serializer.io.Encoder;
import com.baijioss.serializer.schema.EnumSchema;
import com.baijioss.serializer.schema.RecordSchema;
import com.baijioss.serializer.schema.Schema;
import com.baijioss.serializer.schema.SchemaType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link com.baijioss.serializer.generic.DatumWriter DatumWriter} for generated Java classes.
 */
public class SpecificDatumWriter<T> extends PreresolvingDatumWriter<T> {

    public SpecificDatumWriter(Schema schema) {
        super(schema, new SpecificArrayAccess(), new DefaultMapAccess());
    }

    @Override
    protected void writeRecordFields(Object recordObj, RecordFieldWriter[] writers, Encoder encoder) throws IOException {
        SpecificRecord record = (SpecificRecord) recordObj;
        for (int i = 0; i < writers.length; i++) {
            RecordFieldWriter writer = writers[i];
            writer._fieldWriter.write(record.get(writer._field.getPos()), encoder);
        }
    }

    @Override
    protected ItemWriter resolveEnum(EnumSchema es) {
        return new EnumItemWriter(es);
    }

    private static class EnumItemWriter implements ItemWriter {
        private final EnumSchema _schema;

        public EnumItemWriter(EnumSchema schema) {
            _schema = schema;
        }

        @Override
        public void write(Object value, Encoder encoder) throws IOException {
            if (value == null) {
                throw new BaijiTypeException("value is null in SpecificDatumWriter.EnumItemWriter.write");
            }
            encoder.writeEnum(_schema.ordinal(value.toString()));
        }
    }

    @Override
    protected boolean unionBranchMatches(Schema sc, Object obj) {
        if (obj == null && sc.getType() != SchemaType.NULL) {
            return false;
        }
        switch (sc.getType()) {
            case NULL:
                return obj == null;
            case BOOLEAN:
                return obj instanceof Boolean;
            case INT:
                return obj instanceof Integer;
            case LONG:
                return obj instanceof Long;
            case FLOAT:
                return obj instanceof Float;
            case DOUBLE:
                return obj instanceof Double;
            case BYTES:
                return obj instanceof byte[];
            case STRING:
                return obj instanceof String;
            case RECORD:
                return obj instanceof SpecificRecord &&
                        (((RecordSchema) ((SpecificRecord) obj).getSchema())).getSchemaName().equals(
                                ((RecordSchema) (sc)).getSchemaName());
            case ENUM:
                return obj.getClass().isEnum() && ((EnumSchema) sc).getSymbols().contains(obj.toString());
            case ARRAY:
                return obj instanceof List;
            case MAP:
                return obj instanceof Map;
            case UNION:
                return false; // Union directly within another union not allowed!
            default:
                throw new BaijiRuntimeException("Unknown schema type: " + sc.getType());
        }
    }

    private static class SpecificArrayAccess implements ArrayAccess {
        @Override
        public void ensureArrayObject(Object value) {
            if (!(value instanceof List)) {
                throw new BaijiTypeException("Array does not implement non-generic IList");
            }
        }

        @Override
        public long getArrayLength(Object value) {
            return ((List) value).size();
        }

        @Override
        public void writeArrayValues(Object array, ItemWriter valueWriter, Encoder encoder)
                throws IOException {
            List list = (List) array;
            for (int i = 0; i < list.size(); i++) {
                valueWriter.write(list.get(i), encoder);
            }
        }
    }
}

