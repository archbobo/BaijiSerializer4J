package com.baijioss.serializer.generic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class GenericTestMap extends GenericTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"{\"type\": \"map\", \"values\": \"string\"}",
                        new Object[]{"a", "0", "b", "1", "c", "101"}}
        });
    }

    private final String _schema;
    private final Object[] _values;

    public GenericTestMap(String schema, Object[] values) {
        _schema = schema;
        _values = values;
    }

    @Test
    public void testMap() throws IOException {
        test(_schema, makeMap(_values));
    }
}
