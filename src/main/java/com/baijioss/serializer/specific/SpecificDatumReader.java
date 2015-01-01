package com.baijioss.serializer.specific;

import com.baijioss.serializer.exception.BaijiRuntimeException;
import com.baijioss.serializer.generic.GenericDatumReader;
import com.baijioss.serializer.generic.PreresolvingDatumReader;
import com.baijioss.serializer.io.Decoder;
import com.baijioss.serializer.schema.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link com.baijioss.serializer.generic.DatumReader DatumReader} for generated Java classes.
 */
public class SpecificDatumReader<T> extends PreresolvingDatumReader<T> {
    public SpecificDatumReader(Schema schema) {
        super(schema);
    }

    @Override
    protected boolean isReusable(SchemaType type) {
        switch (type) {
            case DOUBLE:
            case BOOLEAN:
            case INT:
            case LONG:
            case FLOAT:
            case BYTES:
            case ENUM:
            case STRING:
            case NULL:
                return false;
        }
        return true;
    }

    @Override
    protected ArrayAccess getArrayAccess(ArraySchema schema) {
        return new SpecificArrayAccess();
    }

    @Override
    protected EnumAccess getEnumAccess(EnumSchema schema) {
        return new SpecificEnumAccess(schema);
    }

    @Override
    protected MapAccess getMapAccess(MapSchema schema) {
        return new SpecificMapAccess();
    }

    @Override
    protected RecordAccess getRecordAccess(RecordSchema schema) {
        if (schema.getName() == null) {
            return new GenericDatumReader.GenericRecordAccess(schema);
        }
        return new SpecificRecordAccess(schema);
    }

    private static class SpecificEnumAccess implements EnumAccess {
        private final Method _findByValueMethod;

        public SpecificEnumAccess(EnumSchema schema) {
            Class clazz = ObjectCreator.INSTANCE.getClass(schema);
            try {
                _findByValueMethod = clazz.getMethod("findByValue", int.class);
                _findByValueMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Object createEnum(Object reuse, int ordinal) {
            Object value = null;
            if (_findByValueMethod != null) {
                try {
                    value = _findByValueMethod.invoke(null, ordinal);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return value;
        }
    }

    private static class SpecificRecordAccess implements RecordAccess {
        private final Class<?> _clazz;
        private Constructor<?> _ctor;

        public SpecificRecordAccess(RecordSchema schema) {
            _clazz = ObjectCreator.INSTANCE.getClass(schema);
            try {
                _ctor = _clazz != null ? _clazz.getDeclaredConstructor() : null;
            } catch (NoSuchMethodException e) {
            }
            if (_ctor != null) {
                _ctor.setAccessible(true);
            }
        }

        @Override
        public Object createRecord(Object reuse) {
            try {
                return reuse != null ? reuse : (_ctor != null ? _ctor.newInstance() : null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Object getField(Object record, String fieldName, int fieldPos) {
            return ((SpecificRecord) record).get(fieldPos);
        }

        @Override
        public void addField(Object record, String fieldName, int fieldPos, Object fieldValue) {
            ((SpecificRecord) record).put(fieldPos, fieldValue);
        }
    }

    private static class SpecificArrayAccess implements ArrayAccess {
        @Override
        public Object create(Object reuse) {
            List array;

            if (reuse != null) {

                if (!(reuse instanceof List)) {
                    throw new BaijiRuntimeException("array object does not implement non-generic List");
                }
                array = (List) reuse;
                // retaining existing behavior where array contents aren't reused
                // TODO: try to reuse contents?
                array.clear();
            } else {
                array = new ArrayList();
            }
            return array;
        }

        @Override
        public Object ensureSize(Object array, int targetSize) {
            return array;
        }

        @Override
        public Object resize(Object array, int targetSize) {
            // no action needed
            return array;
        }

        @Override
        public void addElements(Object array, int elements, int index, ItemReader itemReader, Decoder decoder,
                                boolean reuse) throws IOException {
            List list = (List) array;
            for (int i = 0; i < elements; i++) {
                list.add(itemReader.read(null, decoder));
            }
        }
    }

    private static class SpecificMapAccess implements MapAccess {
        @Override
        public Object create(Object reuse) {
            Map map;
            if (reuse != null) {
                if (!(reuse instanceof Map)) {
                    throw new BaijiRuntimeException("map object does not implement non-generic IList");
                }

                map = (Map) reuse;
                map.clear();
            } else {
                map = new HashMap();
            }
            return map;
        }

        @Override
        public void addElements(Object mapObj, int elements, ItemReader itemReader, Decoder decoder, boolean reuse)
                throws IOException {
            Map map = ((Map) mapObj);
            for (int i = 0; i < elements; i++) {
                String key = decoder.readString();
                map.put(key, itemReader.read(null, decoder));
            }
        }
    }
}

