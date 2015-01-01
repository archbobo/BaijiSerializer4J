package com.baijioss.serializer.generic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class GenericTestUnionMap extends GenericTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"[{\"type\": \"map\", \"values\": \"int\"}, \"string\"]",
                        "{\"type\": \"map\", \"values\": \"int\"}", new Object[]{"a", 1, "b", 2}}
        });
    }

    private final String _unionSchema;
    private final String _mapSchema;
    private final Object[] _value;

    public GenericTestUnionMap(String unionSchema, String mapSchema, Object[] value) {
        _unionSchema = unionSchema;
        _mapSchema = mapSchema;
        _value = value;
    }

    @Test
    public void testUnionMap() throws IOException {
        test(_unionSchema, makeMap(_value));
    }
}
