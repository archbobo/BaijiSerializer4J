package com.baijioss.serializer;

import com.baijioss.serializer.generic.DatumReader;
import com.baijioss.serializer.generic.DatumWriter;
import com.baijioss.serializer.io.BinaryDecoder;
import com.baijioss.serializer.io.BinaryEncoder;
import com.baijioss.serializer.specific.SpecificDatumReader;
import com.baijioss.serializer.specific.SpecificDatumWriter;
import com.baijioss.serializer.specific.SpecificRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BinarySerializer implements Serializer {

    private static final ConcurrentMap<Class<?>, DatumReader> _readerCache =
            new ConcurrentHashMap<Class<?>, DatumReader>();

    private static final ConcurrentMap<Class<?>, DatumWriter> _writerCache =
            new ConcurrentHashMap<Class<?>, DatumWriter>();

    @Override
    public <T extends SpecificRecord> void serialize(T obj, OutputStream stream) throws IOException {
        DatumWriter<T> writer = getWriter(obj);
        writer.write(obj, new BinaryEncoder(stream));
    }

    @Override
    public <T extends SpecificRecord> T deserialize(Class<T> objClass, InputStream stream) throws IOException {
        DatumReader<T> reader = getReader(objClass);
        return reader.read(null, new BinaryDecoder(stream));
    }

    private static <T> DatumWriter<T> getWriter(T obj) {
        Class clazz = obj.getClass();
        DatumWriter<T> writer = _writerCache.get(clazz);
        if (writer == null) {
            writer = new SpecificDatumWriter<T>(((SpecificRecord) obj).getSchema());
            DatumWriter<T> existedWriter = _writerCache.putIfAbsent(clazz, writer);
            if (existedWriter != null) {
                writer = existedWriter;
            }
        }
        return writer;
    }

    private static <T extends SpecificRecord> DatumReader<T> getReader(Class<T> clazz) {
        DatumReader<T> reader = _readerCache.get(clazz);
        if (reader == null) {
            SpecificRecord record;
            try {
                Constructor<T> ctor = clazz.getDeclaredConstructor();
                ctor.setAccessible(true);
                record = ctor.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            reader = new SpecificDatumReader<T>(record.getSchema());
            DatumReader<T> existedReader = _readerCache.putIfAbsent(clazz, reader);
            if (existedReader != null) {
                reader = existedReader;
            }
        }
        return reader;
    }

    public void clearCache() {
        _readerCache.clear();
        _writerCache.clear();
    }
}
