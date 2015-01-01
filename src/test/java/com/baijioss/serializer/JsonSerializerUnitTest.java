package com.baijioss.serializer;

import com.baijioss.serializer.exception.BaijiRuntimeException;
import com.baijioss.serializer.specific.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class JsonSerializerUnitTest {

    JsonSerializer serializer;

    @Before
    public void setUp() throws Exception {
        serializer = new JsonSerializer();
    }

    @Test
    public void testNonRecursiveSerialize() throws Exception {
        OutputStream os = null;
        InputStream is = null;

        try {
            TestSerializerSample expected = new TestSerializerSample();
            expected.bigint1 = 16 * 1024L;
            expected.boolean1 = false;
            expected.double1 = 2.099328;
            expected.enum1 = Enum1Values.GREEN;
            expected.int1 = 231;
            expected.string1 = "testserialize";
            expected.list1 = Arrays.asList("a", "b", "c");
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("1a", 1);
            map.put("2b", 2);
            map.put("3c", 3);
            expected.map1 = map;
            expected.bytes1 = "testBytes".getBytes();

            os = serialize(expected);

            is = new ByteArrayInputStream(((ByteArrayOutputStream) os).toByteArray());
            TestSerializerSample actual = deserialize(TestSerializerSample.class, is);

            Assert.assertEquals(expected.bigint1, actual.bigint1);
            Assert.assertEquals(expected.boolean1, actual.boolean1);
            Assert.assertEquals(expected.double1, actual.double1, 5*Math.ulp(expected.double1));
            Assert.assertEquals(expected.enum1, actual.enum1);
            Assert.assertEquals(expected.int1, actual.int1);
            Assert.assertEquals(expected.string1, actual.string1);
            Assert.assertEquals(expected.list1, actual.list1);
            Assert.assertEquals(expected.map1, actual.map1);
            Assert.assertArrayEquals(expected.bytes1, actual.bytes1);
        } finally {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    @Test
    public void testNestedSerialize() throws Exception {
        OutputStream os = null;
        InputStream is = null;

        try {
            Record2 expected = new Record2();
            expected.bigint2 = 1024 * 1024 * 32L;
            List<byte[]> bytes = new ArrayList<byte[]>();
            bytes.add("testBytes1".getBytes());
            bytes.add("testBytes2".getBytes());
            bytes.add("testBytes3".getBytes());
            expected.byteslist = bytes;
            expected.enum2 = Enum2Values.PLANE;
            expected.list2 = Arrays.asList(1, 3, 5);
            Record record = new Record(1, true, "testRecord");
            Map<String, Record> map = new HashMap<String, Record>();
            map.put("1", record);
            map.put("2", record);
            expected.put("map2", map);
            expected.nullablebigint = 1024 * 1024 * 32L;

            os = serialize(expected);
            is = new ByteArrayInputStream(((ByteArrayOutputStream) os).toByteArray());
            Record2 actual = deserialize(Record2.class, is);

            Assert.assertEquals(expected.bigint2, actual.bigint2);
            Assert.assertEquals(expected.enum2, actual.enum2);
            Assert.assertEquals(expected.nullablebigint, actual.nullablebigint);
            for (int i = 0; i < expected.byteslist.size(); i++) {
                Assert.assertArrayEquals(expected.byteslist.get(i), actual.byteslist.get(i));
            }
            Assert.assertEquals(expected.list2, actual.list2);
            Assert.assertEquals(expected.map2, actual.map2);
        } finally {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    @Test
    public void testCircularSerialize() throws Exception {
        OutputStream os = null;
        InputStream is = null;

        try {
            TestSerializerSample expected = new TestSerializerSample();
            expected.bigint1 = 1024 * 1024 * 16L;
            expected.boolean1 = false;
            expected.double1 = 2.099328;
            expected.list1 = Arrays.asList("a", "b", "c");
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("1a", 1);
            map.put("2b", 2);
            map.put("3c", 3);
            expected.map1 = map;

            TestSerializerSample innerSample = new TestSerializerSample();
            innerSample.bigint1 = 16 * 1024L;
            innerSample.boolean1 = true;
            innerSample.double1 = 2.099328;
            innerSample.list1 = Arrays.asList("aa", "bb", "cc");
            innerSample.map1 = map;

            expected.innerSample = innerSample;

            os = serialize(expected);
            is = new ByteArrayInputStream(((ByteArrayOutputStream) os).toByteArray());
            TestSerializerSample actual = deserialize(TestSerializerSample.class, is);

            Assert.assertNotNull(actual.innerSample);
            Assert.assertEquals(expected.innerSample.bigint1, actual.innerSample.bigint1);
            Assert.assertEquals(expected.innerSample.boolean1, actual.innerSample.boolean1);
            Assert.assertEquals(expected.innerSample.double1, actual.innerSample.double1);
            Assert.assertEquals(expected.innerSample.list1, actual.innerSample.list1);
            Assert.assertEquals(expected.innerSample.map1, actual.innerSample.map1);
        } finally {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    @Test
    public void testNotSupportSchemaType() throws Exception {
        OutputStream os = null;
        InputStream is = null;

        try {
            Record3 expected = new Record3();
            expected.bigint2 = 1.0f;
            expected.enum2 = Enum2Values.BIKE;

            os = serialize(expected);
        } catch (BaijiRuntimeException e) {
        }
    }

    @Test
    public void testNoRecordSerialize() throws Exception {
        OutputStream os = null;
        InputStream is = null;

        try {
            os = serialize(EnumTest.CAR);
        } catch (BaijiRuntimeException e) {
        }
    }

    private <T extends SpecificRecord> OutputStream serialize(T record) throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        serializer.serialize(record, os);

        return os;
    }

    private <T extends SpecificRecord> T deserialize(Class<T> clazz, InputStream is) throws IOException {
        return serializer.deserialize(clazz, is);
    }
}
