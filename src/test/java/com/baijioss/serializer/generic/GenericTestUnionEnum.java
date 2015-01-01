package com.baijioss.serializer.generic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class GenericTestUnionEnum extends GenericTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"\"int\"", 10, 10}
        });
    }

    private final String _schema;
    private final Object _actual;
    private final Object _expected;

    public GenericTestUnionEnum(String schema, Object actual, Object expected) {
        _schema = schema;
        _actual = actual;
        _expected = expected;
    }

    @Test
    public void testUnionEnum() throws IOException {
        testResolution(_schema, _actual, _expected);
    }
}
