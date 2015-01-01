package com.baijioss.serializer.schema;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SchemaTestArray extends SchemaTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"{\"type\": \"array\", \"items\": \"long\"}", "long"}
        });
    }

    private final String _schema;
    private final String _itemType;

    public SchemaTestArray(String schema, String itemType) {
        _schema = schema;
        _itemType = itemType;
    }

    @Test
    public void testArray() {
        Schema sc = Schema.parse(_schema);
        Assert.assertTrue(sc instanceof ArraySchema);
        Assert.assertEquals(SchemaType.ARRAY, sc.getType());

        ArraySchema ars = (ArraySchema)sc;
        Assert.assertEquals(_itemType, ars.getItemSchema().getName());

        testEquality(_schema, sc);
        testToString(sc);
    }
}
