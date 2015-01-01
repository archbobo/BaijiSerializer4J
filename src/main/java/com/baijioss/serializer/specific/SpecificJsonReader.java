package com.baijioss.serializer.specific;

import com.baijioss.serializer.exception.BaijiRuntimeException;
import com.baijioss.serializer.schema.*;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SpecificJsonReader<T> {

    private final Schema root;
    // ObjectMapper is thread safe
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public SpecificJsonReader(Schema schema) {
        this.root = schema;
    }

    /**
     * Parse as JSON Node
     * @param reuse
     * @param is the source stream
     * @return a record instance
     */
    public T read(T reuse, InputStream is) throws IOException {
        JsonNode jsonNode = MAPPER.readTree(is);
        if (root instanceof RecordSchema) {
            RecordSchema recordSchema = (RecordSchema) root;
            try {
                return (T) readRecord(jsonNode, reuse, new RecordReader(recordSchema), recordSchema);
            } catch (Exception e) {
                throw new BaijiRuntimeException(e);
            }
        } else {
            throw new BaijiRuntimeException("Schema must be RecordSchema");
        }
    }

    /** Called to read a record.*/
    protected Object readRecord(JsonNode node, Object reuse, JsonReadable recordReader, RecordSchema recordSchema) throws Exception {
        Object r = recordReader.read(reuse);
        for (Field field : recordSchema.getFields()) {
            if (node.has(field.getName())) {
                Object value = readField(field.getSchema(), node.get(field.getName()));
                put(r, field.getPos(), value);
            }
        }

        return r;
    }

    /** Called to read the inner record.*/
    protected Object readInnerRecord(Object reuse, JsonReadable recordReader, RecordSchema recordSchema) throws Exception {
        Object obj = recordReader.read(null);
        for (Field field : recordSchema.getFields()) {
            if (((ObjectNode) reuse).has(field.getName())) {
                Object value = readField(field.getSchema(), ((ObjectNode) reuse).get(field.getName()));
                put(obj, field.getPos(), value);
            }
        }

        return obj;
    }

    protected void put(Object obj, int fieldPos, Object fieldValue) {
        ((SpecificRecord) obj).put(fieldPos, fieldValue);
    }

    /** Called to read a field of a record.*/
    protected Object readField(Schema schema, Object datum) throws Exception {
        try {
            switch (schema.getType()) {
                case NULL:
                    return readNull();
                case INT:
                    return readInt(datum);
                case BOOLEAN:
                    return readBoolean(datum);
                case DOUBLE:
                    return readDouble(datum);
                case LONG:
                    return readLong(datum);
                case STRING:
                    return readString(datum);
                case BYTES:
                    return readBytes(datum);
                case RECORD:
                    RecordReader recordReader = new RecordReader((RecordSchema) schema);
                    return readInnerRecord(datum, recordReader, (RecordSchema) schema);
                case MAP:
                    MapSchema mapSchema = (MapSchema) schema;
                    return readMap(datum, mapSchema);
                case ENUM:
                    EnumReader enumReader = new EnumReader(schema);
                    return enumReader.read(datum);
                case UNION:
                    UnionSchema unionSchema = (UnionSchema) schema;
                    return readUnion(datum, unionSchema);
                case ARRAY:
                    ArraySchema arraySchema = (ArraySchema) schema;
                    return readArray(datum, arraySchema);
                default:
                    throw new BaijiRuntimeException("");
            }
        } catch (NullPointerException e) {
            throw e;
        }
    }

    private Object readNull() {
        return null;
    }

    private Object readInt(Object obj) {
        return (obj instanceof JsonNode) ? ((JsonNode) obj).getIntValue() : obj;
    }

    private Object readBoolean(Object obj) {
        return (obj instanceof JsonNode) ? ((JsonNode) obj).getBooleanValue() : obj;
    }

    private Object readDouble(Object obj) {
        return (obj instanceof JsonNode) ? ((JsonNode) obj).getDoubleValue() : obj;
    }

    private Object readLong(Object obj) {
        return (obj instanceof JsonNode) ? ((JsonNode) obj).getLongValue() : obj;
    }

    private Object readString(Object obj) {
        return (obj instanceof JsonNode) ? ((JsonNode) obj).getTextValue() : obj;
    }

    private byte[] readBytes(Object obj) throws IOException {
        return ((JsonNode) obj).getBinaryValue();
    }

    private Map readMap(Object obj, MapSchema mapSchema) throws Exception {
        MapReader mapReader = new MapReader(mapSchema);
        Map map = (Map) mapReader.read(null);
        for (Iterator<Map.Entry<String, JsonNode>> iterator = ((JsonNode) obj).getFields(); iterator.hasNext();) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            Schema valueSchema = mapSchema.getValueSchema();
            Object value = readField(valueSchema, entry.getValue());
            mapReader.add(map, entry.getKey(), value);
        }

        return map;
    }

    private List readArray(Object obj, ArraySchema arraySchema) throws Exception {
        ArrayReader arrayReader = new ArrayReader(arraySchema);
        List list = (List) arrayReader.read(null);
        for (Iterator<JsonNode> iterator = ((JsonNode) obj).getElements(); iterator.hasNext();) {
            JsonNode node = iterator.next();
            Schema itemSchema = arraySchema.getItemSchema();
            Object value = readField(itemSchema, node);
            list.add(value);
        }

        return list;
    }

    private Object readUnion(Object obj, UnionSchema unionSchema) throws Exception {
        for (int i = 0; i < unionSchema.size(); i++) {
            Schema schema = unionSchema.get(i);
            if (schema.getType() == SchemaType.NULL)
                continue;

            return readField(schema, obj);
        }

        throw new BaijiRuntimeException("");
    }

    private class RecordReader implements JsonReadable {

        private final Constructor constructor;

        public RecordReader(RecordSchema recordSchema) {
            this.constructor = getConstructor(recordSchema);
        }

        @Override
        public Object read(Object reuse) throws Exception {
            return reuse == null ? constructor.newInstance() : reuse;
        }
    }

    private class MapReader implements JsonReadable {

        private final Constructor constructor;

        public MapReader(Schema schema) {
            this.constructor = getConstructor(schema);
        }

        @Override
        public Object read(Object reuse) throws Exception {
            return reuse == null ? constructor.newInstance() : reuse;
        }

        public void add(Object map, String key, Object value) {
            ((Map) map).put(key, value);
        }
    }

    private class ArrayReader implements JsonReadable {

        private final Constructor constructor;

        public ArrayReader(Schema schema) {
            this.constructor = getConstructor(schema);
        }

        @Override
        public Object read(Object reuse) throws Exception {
            List list;
            if (reuse != null) {
                try {
                    list = (List) reuse;
                } catch (ClassCastException e) {
                    throw new BaijiRuntimeException(e);
                }
                list.clear();
            } else {
                list = (List) constructor.newInstance();
            }

            return list;
        }
    }

    private class EnumReader implements JsonReadable {

        private int[] translator;
        private EnumSchema enumSchema;

        public EnumReader(Schema schema) {
            enumSchema = (EnumSchema) schema;
            translator = new int[enumSchema.getSymbols().size()];
            for (String symbol : enumSchema.getSymbols()) {
                int index = enumSchema.ordinal(symbol);
                translator[index] = enumSchema.ordinal(symbol);
            }
        }

        @Override
        public Object read(Object reuse) throws Exception {
            String value = ((JsonNode) reuse).getTextValue();
            return Enum.valueOf((Class)ObjectCreator.INSTANCE.getClass(enumSchema), value);
        }
    }

    private interface JsonReadable {
        Object read(Object reuse) throws Exception;
    }

    private static Constructor getConstructor(Schema schema) {
        ObjectCreator objectCreator = ObjectCreator.INSTANCE;
        Constructor constructor = null;
        try {
            constructor = objectCreator.getClass(schema).getConstructor(new Class[]{});
        } catch (NoSuchMethodException e) {
            System.out.println("null pointer exception");
            return null;
        }

        return constructor;
    }
}
