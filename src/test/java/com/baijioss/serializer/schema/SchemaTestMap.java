package com.baijioss.serializer.schema;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SchemaTestMap extends SchemaTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"{\"type\": \"map\", \"values\": \"long\"}", "long"}
        });
    }

    private final String _schema;
    private final String _valueType;

    public SchemaTestMap(String schema, String valueType) {
        _schema = schema;
        _valueType = valueType;
    }

    @Test
    public void testMap() {
        Schema sc = Schema.parse(_schema);
        Assert.assertTrue(sc instanceof MapSchema);
        Assert.assertEquals(SchemaType.MAP, sc.getType());

        MapSchema ms = (MapSchema) sc;
        Assert.assertEquals(_valueType, ms.getValueSchema().getName());

        testEquality(_schema, sc);
        testToString(sc);
    }
}
