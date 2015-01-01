package com.baijioss.serializer.schema;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SchemaTestFullName extends SchemaTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"a", "o.a.h", "o.a.h.a"}
        });
    }

    private final String _name;
    private final String _namespace;
    private final String _fullName;

    public SchemaTestFullName(String name, String namespace, String fullName) {
        _name = name;
        _namespace = namespace;
        _fullName = fullName;
    }

    @Test
    public void testFullName() {
        SchemaName schemaName = new SchemaName(_name, _namespace, null);
        Assert.assertEquals(_fullName, schemaName.getFullName());
    }
}
