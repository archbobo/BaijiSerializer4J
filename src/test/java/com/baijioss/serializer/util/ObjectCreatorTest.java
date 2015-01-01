package com.baijioss.serializer.util;

import com.baijioss.serializer.schema.ArraySchema;
import com.baijioss.serializer.schema.MapSchema;
import com.baijioss.serializer.schema.PrimitiveSchema;
import com.baijioss.serializer.specific.ObjectCreator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class ObjectCreatorTest {

    @Test
    public void testPrimitiveSchemas() {
        Class<?> clazz = ObjectCreator.INSTANCE.getClass(PrimitiveSchema.newInstance("null"));
        assertEquals(null, clazz);
        clazz = ObjectCreator.INSTANCE.getClass(PrimitiveSchema.newInstance("boolean"));
        assertEquals(Boolean.class, clazz);
        clazz = ObjectCreator.INSTANCE.getClass(PrimitiveSchema.newInstance("int"));
        assertEquals(Integer.class, clazz);
        clazz = ObjectCreator.INSTANCE.getClass(PrimitiveSchema.newInstance("long"));
        assertEquals(Long.class, clazz);
        clazz = ObjectCreator.INSTANCE.getClass(PrimitiveSchema.newInstance("float"));
        assertEquals(Float.class, clazz);
        clazz = ObjectCreator.INSTANCE.getClass(PrimitiveSchema.newInstance("double"));
        assertEquals(Double.class, clazz);
        clazz = ObjectCreator.INSTANCE.getClass(PrimitiveSchema.newInstance("bytes"));
        assertEquals(byte[].class, clazz);
        clazz = ObjectCreator.INSTANCE.getClass(PrimitiveSchema.newInstance("string"));
        assertEquals(String.class, clazz);
    }

    @Test
    public void testContainerSchemas() {
        Class<?> clazz = ObjectCreator.INSTANCE.getClass(new ArraySchema(PrimitiveSchema.newInstance("int"), null));
        assertEquals(ArrayList.class, clazz);
        clazz = ObjectCreator.INSTANCE.getClass(new MapSchema(PrimitiveSchema.newInstance("string"), null));
        assertEquals(HashMap.class, clazz);
    }
}
