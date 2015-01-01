package com.baijioss.serializer.schema;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SchemaTestPrimitive extends SchemaTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"null", SchemaType.NULL},
                new Object[]{"boolean", SchemaType.BOOLEAN},
                new Object[]{"int", SchemaType.INT},
                new Object[]{"long", SchemaType.LONG},
                new Object[]{"float", SchemaType.FLOAT},
                new Object[]{"double", SchemaType.DOUBLE},
                new Object[]{"bytes", SchemaType.BYTES},
                new Object[]{"string", SchemaType.STRING},
                new Object[]{"{ \"type\": \"null\" }", SchemaType.NULL},
                new Object[]{"{ \"type\": \"boolean\" }", SchemaType.BOOLEAN},
                new Object[]{"{ \"type\": \"int\" }", SchemaType.INT},
                new Object[]{"{ \"type\": \"long\" }", SchemaType.LONG},
                new Object[]{"{ \"type\": \"float\" }", SchemaType.FLOAT},
                new Object[]{"{ \"type\": \"double\" }", SchemaType.DOUBLE},
                new Object[]{"{ \"type\": \"bytes\" }", SchemaType.BYTES},
                new Object[]{"{ \"type\": \"string\" }", SchemaType.STRING},
        });
    }

    private final String _schema;
    private final SchemaType _type;

    public SchemaTestPrimitive(String schema, SchemaType type) {
        _schema = schema;
        _type = type;
    }

    @Test
    public void testPrimitive() {
        Schema sc = Schema.parse(_schema);
        Assert.assertTrue(sc instanceof PrimitiveSchema);
        Assert.assertEquals(_type, sc.getType());

        testEquality(_schema, sc);
        testToString(sc);
    }
}
