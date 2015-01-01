package com.baijioss.serializer;


import com.baijioss.serializer.specific.SpecificJsonReader;
import com.baijioss.serializer.specific.SpecificJsonWriter;
import com.baijioss.serializer.specific.SpecificRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JsonSerializer implements Serializer {

    private static final ConcurrentMap<Class<?>, SpecificJsonReader> _readerCache =
            new ConcurrentHashMap<Class<?>, SpecificJsonReader>();

    private static final ConcurrentMap<Class<?>, SpecificJsonWriter> _writerCache =
            new ConcurrentHashMap<Class<?>, SpecificJsonWriter>();

    @Override
    public <T extends SpecificRecord> void serialize(T obj, OutputStream stream) throws IOException {
        SpecificJsonWriter<T> writer = getWriter(obj);
        writer.write(obj.getSchema(), obj, stream);
    }

    @Override
    public <T extends SpecificRecord> T deserialize(Class<T> objClass, InputStream stream) throws IOException {
        SpecificJsonReader<T> reader = (SpecificJsonReader) getReader(objClass);
        return reader.read(null, stream);
    }

    private static <T extends SpecificRecord> SpecificJsonWriter<T> getWriter(T obj) {
        Class clazz = obj.getClass();
        SpecificJsonWriter<T> writer = _writerCache.get(clazz);
        if (writer == null) {
            writer = new SpecificJsonWriter<T>();
            SpecificJsonWriter<T> existedWriter = _writerCache.putIfAbsent(clazz, writer);
            if (existedWriter != null) {
                writer = existedWriter;
            }
        }
        return writer;
    }

    private static <T extends SpecificRecord> SpecificJsonReader<T> getReader(Class<T> clazz) {
        SpecificJsonReader<T> datumReader = _readerCache.get(clazz);
        if (datumReader == null) {
            SpecificRecord record;
            try {
                record = clazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            datumReader = new SpecificJsonReader<T>(record.getSchema());
            SpecificJsonReader<T> existedReader = _readerCache.putIfAbsent(clazz, datumReader);
            if (existedReader != null) {
                datumReader = existedReader;
            }
        }

        return datumReader;
    }

   /* public void clearCache() {
        _readerCache.clear();
        _writerCache.clear();
    }*/
}
