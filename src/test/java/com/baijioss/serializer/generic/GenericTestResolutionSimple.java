package com.baijioss.serializer.generic;

import com.baijioss.serializer.exception.BaijiRuntimeException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class GenericTestResolutionSimple extends GenericTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"[{\"type\": \"enum\", \"symbols\": [\"s1\", \"s2\"], \"name\": \"e\"}, \"string\"]", "{\"type\": \"enum\", \"symbols\": [\"s1\", \"s2\"], \"name\": \"e\"}", "s1", true},
                new Object[]{"[{\"type\": \"enum\", \"symbols\": [\"s1\", \"s2\"], \"name\": \"e\"}, \"string\"]", "{\"type\": \"enum\", \"symbols\": [\"s1\", \"s2\"], \"name\": \"e\"}", "s2", true},
                new Object[]{"[{\"type\": \"enum\", \"symbols\": [\"s1\", \"s2\"], \"name\": \"e\"}, \"string\"]", "{\"type\": \"enum\", \"symbols\": [\"s1\", \"s2\"], \"name\": \"e\"}", "s3", false}
        });
    }

    private final String _unionSchema;
    private final String _enumSchema;
    private final String _value;
    private final boolean _valid;

    public GenericTestResolutionSimple(String unionSchema, String enumSchema, String value, boolean valid) {
        _unionSchema = unionSchema;
        _enumSchema = enumSchema;
        _value = value;
        _valid = valid;
    }

    @Test
    public void testResolutionSimple() throws IOException {
        try {
            test(_unionSchema, makeEnum(_enumSchema, _value));
        } catch (BaijiRuntimeException ex) {
            ex.printStackTrace();
            Assert.assertFalse(_valid);
        }
    }
}
