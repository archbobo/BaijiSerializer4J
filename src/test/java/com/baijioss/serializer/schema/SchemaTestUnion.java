package com.baijioss.serializer.schema;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SchemaTestUnion extends SchemaTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"[\"string\", \"null\", \"long\"]", new String[]{"string", "null", "long"}}
        });
    }

    private final String _schema;
    private final String[] _types;

    public SchemaTestUnion(String schema, String[] types) {
        _schema = schema;
        _types = types;
    }

    @Test
    public void testUnion() {
        Schema sc = Schema.parse(_schema);
        Assert.assertTrue(sc instanceof UnionSchema);
        Assert.assertEquals(SchemaType.UNION, sc.getType());

        UnionSchema us = (UnionSchema) sc;
        Assert.assertEquals(_types.length, us.size());

        for (int i = 0; i < us.size(); i++) {
            Assert.assertEquals(_types[i], us.get(i).getName());
        }

        testEquality(_schema, sc);
        testToString(sc);
    }
}
