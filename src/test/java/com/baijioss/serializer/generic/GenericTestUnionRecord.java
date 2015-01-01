package com.baijioss.serializer.generic;

import com.baijioss.serializer.schema.RecordSchema;
import com.baijioss.serializer.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class GenericTestUnionRecord extends GenericTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"[{\"type\":\"record\", \"name\":\"n\", \"fields\":[{\"name\":\"f1\", \"type\":\"string\"}]}, \"string\"]",
                        "{\"type\":\"record\", \"name\":\"n\", \"fields\":[{\"name\":\"f1\", \"type\":\"string\"}]}",
                        new Object[]{"f1", "v1"}}
        });
    }

    private final String _unionSchema;
    private final String _recordSchema;
    private final Object[] _value;

    public GenericTestUnionRecord(String unionSchema, String recordSchema, Object[] value) {
        _unionSchema = unionSchema;
        _recordSchema = recordSchema;
        _value = value;
    }

    @Test
    public void testUnionRecord() throws IOException {
        test(_unionSchema, makeRecord(_value, (RecordSchema) Schema.parse(_recordSchema)));
    }
}
