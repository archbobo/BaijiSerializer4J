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
public class GenericTestResolutionRecord extends GenericTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"{\"type\":\"record\",\"name\":\"r\",\"fields\":" +
                        "[{\"name\":\"f1\",\"type\":\"boolean\"},{\"name\":\"f2\",\"type\":\"int\"}]}",
                        new Object[]{"f1", true, "f2", 100},
                        new Object[]{"f1", true, "f2", 100}}
        });
    }

    private final String _schema;
    private final Object[] _actual;
    private final Object[] _expected;

    public GenericTestResolutionRecord(String schema, Object[] actual, Object[] expected) {
        _schema = schema;
        _actual = actual;
        _expected = expected;
    }

    @Test
    public void testResolutionRecord() throws IOException {
        RecordSchema s = (RecordSchema) Schema.parse(_schema);
        testResolution(_schema, makeRecord(_actual, s), makeRecord(_expected, s));
    }
}
