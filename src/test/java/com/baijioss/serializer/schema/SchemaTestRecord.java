package com.baijioss.serializer.schema;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SchemaTestRecord extends SchemaTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"{\"type\":\"record\",\"name\":\"LongList\"," +
                        "\"fields\":[{\"name\":\"f1\",\"type\":\"long\"}," +
                        "{\"name\":\"f2\",\"type\": \"int\"}]}",
                        new String[]{"f1", "long", "100", "f2", "int", "10"}},
                new Object[]{"{\"type\":\"record\",\"name\":\"LongList\"," +
                        "\"fields\":[{\"name\":\"f1\",\"type\":\"long\", \"default\": \"100\"}," +
                        "{\"name\":\"f2\",\"type\": \"int\"}]}",
                        new String[]{"f1", "long", "100", "f2", "int", "10"}},
                new Object[]{"{\"type\":\"record\",\"name\":\"LongList\"," +
                        "\"fields\":[{\"name\":\"value\",\"type\":\"long\", \"default\": \"100\"}," +
                        "{\"name\":\"next\",\"type\":[\"LongList\",\"null\"]}]}",
                        new String[] {"value", "long", "100", "next", "union", null}},
        });
    }

    private final String _schema;
    private final String[] _fields;

    public SchemaTestRecord(String schema, String[] fields) {
        _schema = schema;
        _fields = fields;
    }

    @Test
    public void testRecord() {
        Schema sc = Schema.parse(_schema);
        Assert.assertEquals(SchemaType.RECORD, sc.getType());
        Assert.assertTrue(sc instanceof RecordSchema);
        RecordSchema record = (RecordSchema) sc;
        Assert.assertEquals(_fields.length / 3, record.size());
        for (int i = 0; i < _fields.length; i += 3) {
            Field f = record.getField(_fields[i]);
            Assert.assertEquals(_fields[i + 1], f.getSchema().getName());
        }

        testEquality(_schema, sc);
        testToString(sc);
    }
}
