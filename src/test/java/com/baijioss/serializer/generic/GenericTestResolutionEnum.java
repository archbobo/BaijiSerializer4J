package com.baijioss.serializer.generic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class GenericTestResolutionEnum extends GenericTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"{\"type\":\"enum\", \"symbols\":[\"a\", \"b\"], \"name\":\"e\"}", "a"}
        });
    }

    private final String _schema;
    private final String _symbol;

    public GenericTestResolutionEnum(String schema, String symbol) {
        _schema = schema;
        _symbol = symbol;
    }

    @Test
    public void testResolutionEnum() throws IOException {
        testResolution(_schema, makeEnum(_schema, "a"), makeEnum(_schema, "a"));
    }
}
