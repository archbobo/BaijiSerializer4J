package com.baijioss.serializer.generic;

import com.baijioss.serializer.exception.BaijiRuntimeException;
import com.baijioss.serializer.exception.BaijiTypeException;
import com.baijioss.serializer.io.Encoder;
import com.baijioss.serializer.schema.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A general purpose writer of data from Baiji streams. This writer analyzes the writer schema
 * when constructed so that writes can be more efficient. Once constructed, a writer can be reused or shared among threads
 * to avoid incurring more resolution costs.
 *
 * @param <T>
 */
public abstract class PreresolvingDatumWriter<T> implements DatumWriter<T> {

    private final Schema _schema;
    private final ItemWriter _writer;
    private final ArrayAccess _arrayAccess;
    private final MapAccess _mapAccess;
    private final Map<RecordSchema, ItemWriter> _recordWriters = new HashMap<RecordSchema, ItemWriter>();

    protected PreresolvingDatumWriter(Schema schema, ArrayAccess arrayAccess, MapAccess mapAccess) {
        _schema = schema;
        _arrayAccess = arrayAccess;
        _mapAccess = mapAccess;
        _writer = resolveWriter(schema);
    }

    @Override
    public Schema getSchema() {
        return _schema;
    }

    @Override
    public void write(T datum, Encoder out) throws IOException {
        _writer.write(datum, out);
    }

    private ItemWriter resolveWriter(Schema schema) {
        switch (schema.getType()) {
            case NULL:
                return NullItemWriter.INSTANCE;
            case BOOLEAN:
                return BooleanItemWriter.INSTANCE;
            case INT:
                return IntItemWriter.INSTANCE;
            case LONG:
                return LongItemWriter.INSTANCE;
            case FLOAT:
                return FloatItemWriter.INSTANCE;
            case DOUBLE:
                return DoubleItemWriter.INSTANCE;
            case STRING:
                return StringItemWriter.INSTANCE;
            case BYTES:
                return BytesItemWriter.INSTANCE;
            case RECORD:
                return resolveRecord((RecordSchema) schema);
            case ENUM:
                return resolveEnum((EnumSchema) schema);
            case ARRAY:
                return resolveArray((ArraySchema) schema);
            case MAP:
                return resolveMap((MapSchema) schema);
            case UNION:
                return resolveUnion((UnionSchema) schema);
            default:
                return new ErrorItemWriter(schema);
        }
    }

    /**
     * Serialized a record using the given RecordSchema. It uses GetField method
     * to extract the field value from the given object.
     *
     * @param schema The RecordSchema to use for serialization
     * @return
     */
    private ItemWriter resolveRecord(RecordSchema schema) {
        ItemWriter recordWriter = _recordWriters.get(schema);
        if (recordWriter != null) {
            return recordWriter;
        }

        RecordFieldWriter[] writeSteps = new RecordFieldWriter[schema.size()];
        recordWriter = new RecordFieldsWriter(writeSteps);
        _recordWriters.put(schema, recordWriter);

        int index = 0;
        for (Field field : schema) {
            RecordFieldWriter record = new RecordFieldWriter(resolveWriter(field.getSchema()),
                    field);
            writeSteps[index++] = record;
        }

        return recordWriter;
    }


    protected static class RecordFieldWriter {
        public ItemWriter _fieldWriter;

        public Field _field;

        public RecordFieldWriter(ItemWriter fieldWriter, Field field) {
            _fieldWriter = fieldWriter;
            _field = field;
        }
    }

    protected abstract void writeRecordFields(Object record, RecordFieldWriter[] writers, Encoder encoder)
            throws IOException;

    /**
     * Serializes an enumeration.
     *
     * @param es The EnumSchema for serialization
     * @return
     */
    protected abstract ItemWriter resolveEnum(EnumSchema es);

    /**
     * Serialized an array. The default implementation calls EnsureArrayObject() to ascertain that the
     * given value is an array. It then calls GetArrayLength() and GetArrayElement()
     * to access the members of the array and then serialize them.
     *
     * @param schema The ArraySchema for serialization
     * @return
     */
    protected ItemWriter resolveArray(ArraySchema schema) {
        ItemWriter itemWriter = resolveWriter(schema.getItemSchema());
        return new ArrayItemWriter(itemWriter);
    }

    private ItemWriter resolveMap(MapSchema mapSchema) {
        ItemWriter itemWriter = resolveWriter(mapSchema.getValueSchema());
        return new MapItemWriter(itemWriter);
    }

    private ItemWriter resolveUnion(UnionSchema unionSchema) {
        Schema[] branchSchemas = unionSchema.getSchemas().toArray(new Schema[0]);
        ItemWriter[] branchWriters = new ItemWriter[branchSchemas.length];
        int branchIndex = 0;
        for (Schema branch : branchSchemas) {
            branchWriters[branchIndex++] = resolveWriter(branch);
        }
        return new UnionItemWriter(unionSchema, branchSchemas, branchWriters);
    }

    private class UnionItemWriter implements ItemWriter {
        private final UnionSchema _schema;
        private final Schema[] _branches;
        private final ItemWriter[] _branchWriters;

        public UnionItemWriter(UnionSchema schema, Schema[] branches, ItemWriter[] branchWriters) {
            _schema = schema;
            _branches = branches;
            _branchWriters = branchWriters;
        }

        @Override
        public void write(Object value, Encoder encoder) throws IOException {
            int index = resolveBranch(value);
            encoder.writeUnionIndex(index);
            _branchWriters[index].write(value, encoder);
        }

        /**
         * Finds the branch within the given UnionSchema that matches the given object. The default implementation
         * calls unionBranchMatches() method in the order of branches within the UnionSchema. If nothing matches, throws
         * an exception.
         *
         * @param obj The object that should be used in matching
         * @return
         */
        protected int resolveBranch(Object obj) {
            for (int i = 0; i < _branches.length; i++) {
                if (unionBranchMatches(_branches[i], obj)) return i;
            }
            throw new BaijiRuntimeException("Cannot find a match for " + obj.getClass() + " in " + _schema);
        }
    }

    protected static BaijiRuntimeException typeMismatch(Object obj, String schemaType, String type) {
        return new BaijiRuntimeException(type + " required to write against " + schemaType + " schema but found " +
                (null == obj ? "null" : obj.getClass().toString()));
    }

    protected abstract boolean unionBranchMatches(Schema sc, Object obj);

    protected static interface ItemWriter {
        void write(Object value, Encoder encoder) throws IOException;
    }

    private static class NullItemWriter implements ItemWriter {
        private static final NullItemWriter INSTANCE = new NullItemWriter();

        @Override
        public void write(Object value, Encoder encoder) {
            if (value != null) {
                throw typeMismatch(value, "null", "null");
            }
        }
    }

    private static class BooleanItemWriter implements ItemWriter {
        private static final BooleanItemWriter INSTANCE = new BooleanItemWriter();

        @Override
        public void write(Object value, Encoder encoder) throws IOException {
            if (!(value instanceof Boolean)) {
                throw typeMismatch(value, SchemaType.BOOLEAN.toString(), Boolean.class.toString());
            }
            encoder.writeBoolean((Boolean) value);
        }
    }

    private static class IntItemWriter implements ItemWriter {
        private static final IntItemWriter INSTANCE = new IntItemWriter();

        @Override
        public void write(Object value, Encoder encoder) throws IOException {
            if (!(value instanceof Integer)) {
                throw typeMismatch(value, SchemaType.INT.toString(), Integer.class.toString());
            }
            encoder.writeInt((Integer) value);
        }
    }

    private static class LongItemWriter implements ItemWriter {
        private static final LongItemWriter INSTANCE = new LongItemWriter();

        @Override
        public void write(Object value, Encoder encoder) throws IOException {
            if (!(value instanceof Long)) {
                throw typeMismatch(value, SchemaType.LONG.toString(), Long.class.toString());
            }
            encoder.writeLong((Long) value);
        }
    }

    private static class FloatItemWriter implements ItemWriter {
        private static final FloatItemWriter INSTANCE = new FloatItemWriter();

        @Override
        public void write(Object value, Encoder encoder) throws IOException {
            if (!(value instanceof Float)) {
                throw typeMismatch(value, SchemaType.FLOAT.toString(), Float.class.toString());
            }
            encoder.writeFloat((Float) value);
        }
    }

    private static class DoubleItemWriter implements ItemWriter {
        private static final DoubleItemWriter INSTANCE = new DoubleItemWriter();

        @Override
        public void write(Object value, Encoder encoder) throws IOException {
            if (!(value instanceof Double)) {
                throw typeMismatch(value, SchemaType.DOUBLE.toString(), Double.class.toString());
            }
            encoder.writeDouble((Double) value);
        }
    }

    private static class StringItemWriter implements ItemWriter {
        private static final StringItemWriter INSTANCE = new StringItemWriter();

        @Override
        public void write(Object value, Encoder encoder) throws IOException {
            if (!(value instanceof String)) {
                throw typeMismatch(value, SchemaType.STRING.toString(), String.class.toString());
            }
            encoder.writeString((String) value);
        }
    }

    private static class BytesItemWriter implements ItemWriter {
        private static final BytesItemWriter INSTANCE = new BytesItemWriter();

        @Override
        public void write(Object value, Encoder encoder) throws IOException {
            if (!(value instanceof byte[])) {
                throw typeMismatch(value, SchemaType.BYTES.toString(), byte[].class.toString());
            }
            encoder.writeBytes((byte[]) value);
        }
    }

    private class RecordFieldsWriter implements ItemWriter {
        private final RecordFieldWriter[] _fieldWriters;

        public RecordFieldsWriter(RecordFieldWriter[] fieldWriters) {
            _fieldWriters = fieldWriters;
        }

        @Override
        public void write(Object value, Encoder encoder) throws IOException {
            writeRecordFields(value, _fieldWriters, encoder);
        }
    }

    private class ArrayItemWriter implements ItemWriter {
        private final ItemWriter _itemWriter;

        public ArrayItemWriter(ItemWriter itemWriter) {
            _itemWriter = itemWriter;
        }

        @Override
        public void write(Object value, Encoder encoder) throws IOException {
            _arrayAccess.ensureArrayObject(value);
            long l = _arrayAccess.getArrayLength(value);
            encoder.writeArrayStart();
            encoder.setItemCount(l);
            _arrayAccess.writeArrayValues(value, _itemWriter, encoder);
            encoder.writeArrayEnd();
        }
    }

    private class MapItemWriter implements ItemWriter {
        private final ItemWriter _valueWriter;

        public MapItemWriter(ItemWriter valueWriter) {
            _valueWriter = valueWriter;
        }

        @Override
        public void write(Object value, Encoder encoder) throws IOException {
            _mapAccess.ensureMapObject(value);
            encoder.writeMapStart();
            encoder.setItemCount(_mapAccess.getMapSize(value));
            _mapAccess.writeMapValues(value, _valueWriter, encoder);
            encoder.writeMapEnd();
        }
    }

    private static class ErrorItemWriter implements ItemWriter {
        private final Schema _schema;

        public ErrorItemWriter(Schema schema) {
            _schema = schema;
        }

        @Override
        public void write(Object value, Encoder encoder) {
            throw new BaijiTypeException("Not a " + _schema + ": " + value);
        }
    }

    protected static interface EnumAccess {
        void writeEnum(Object value);
    }

    protected static interface ArrayAccess {
        /**
         * Checks if the given object is an array. If it is a valid array, this function returns normally. Otherwise,
         * it throws an exception. The default implementation checks if the value is an array.
         *
         * @param value
         */
        void ensureArrayObject(Object value);

        /**
         * Returns the length of an array. The default implementation requires the object
         * to be an array of objects and returns its length. The default implementation
         * guarantees that ensureArrayObject() has been called on the value before this
         * function is called.
         *
         * @param value The object whose array length is required
         * @return The array length of the given object
         */
        long getArrayLength(Object value);


        /**
         * Returns the element at the given index from the given array object. The default implementation
         * requires that the value is an object array and returns the element in that array. The defaul implementation
         * guarantees that ensureArrayObject() has been called on the value before this
         * function is called.
         *
         * @param array       The array object
         * @param valueWriter
         * @param encoder
         * @throws IOException
         */
        void writeArrayValues(Object array, ItemWriter valueWriter, Encoder encoder) throws IOException;
    }

    protected static interface MapAccess {
        /**
         * Checks if the given object is a map. If it is a valid map, this function returns normally. Otherwise,
         * it throws an exception. The default implementation checks if the value is an IDictionary{string, object}.
         *
         * @param value
         */
        void ensureMapObject(Object value);

        /**
         * Returns the size of the map object. The default implementation guarantees that EnsureMapObject has been
         * successfully called with the given value. The default implementation requires the value
         * to be a {@link java.util.Map} and returns the number of elements in it.
         *
         * @param value The map object whose size is desired
         * @return The size of the given map object
         */
        long getMapSize(Object value);


        /**
         * Returns the contents of the given map object. The default implementation guarantees that EnsureMapObject
         * has been called with the given value. The default implementation of this method requires that
         * the value is a {@link java.util.Map} and returns its contents.
         *
         * @param map         The map object whose size is desired
         * @param valueWriter
         * @param encoder
         * @throws IOException
         */
        void writeMapValues(Object map, ItemWriter valueWriter, Encoder encoder) throws IOException;
    }

    protected static class DefaultMapAccess implements MapAccess {

        public DefaultMapAccess() {
        }

        public void ensureMapObject(Object value) {
            if (!(value instanceof Map)) {
                throw typeMismatch(value, "map", "Map");
            }
        }

        public long getMapSize(Object value) {
            return ((Map) value).size();
        }

        public void writeMapValues(Object map, ItemWriter valueWriter, Encoder encoder) throws IOException {
            for (Map.Entry entry : ((Map<?, ?>) map).entrySet()) {
                encoder.startItem();
                encoder.writeString(entry.getKey().toString());
                valueWriter.write(entry.getValue(), encoder);
            }
        }
    }
}
