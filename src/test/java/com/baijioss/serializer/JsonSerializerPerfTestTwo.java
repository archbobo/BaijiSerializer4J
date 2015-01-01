package com.baijioss.serializer;

import com.baijioss.serializer.specific.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class JsonSerializerPerfTestTwo {

    private static JsonSerializer serializer = new JsonSerializer();

    @Test
    public void testMultiThreadSerialize() throws Exception {
        final int threadNumber = 10;
        final CountDownLatch countDownLatch = new CountDownLatch(threadNumber);
        for (int i = 0; i < threadNumber; i++) {
            Serializer ser = new Serializer(countDownLatch);
            Thread thread = new Thread(ser);
            thread.start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    class Serializer implements Runnable {

        private CountDownLatch cdl;

        public Serializer(CountDownLatch cdl) {
            this.cdl = cdl;
        }

        @Override
        public void run() {
            long tid = Thread.currentThread().getId();
            int loop = 10;

            for (int i = 0; i < loop; i++) {
                try {
                	OutputStream os = new ByteArrayOutputStream();
                    TestSerializerSample expected = createSample(tid + i);
                    serializer.serialize(expected, os);
                    InputStream is = new ByteArrayInputStream(((ByteArrayOutputStream) os).toByteArray());
                    TestSerializerSample actual = serializer.deserialize(TestSerializerSample.class, is);
                    checkStatus(expected, actual);
                    Assert.assertEquals((long)actual.bigint1, tid + i);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            cdl.countDown();
        }
    }

    private void checkStatus(TestSerializerSample expected, TestSerializerSample actual) {
        Assert.assertEquals(expected.bigint1, actual.bigint1);
        Assert.assertEquals(expected.boolean1, actual.boolean1);
        Assert.assertEquals(expected.double1, actual.double1);
        Assert.assertEquals(expected.enum1, actual.enum1);
        Assert.assertEquals(expected.int1, actual.int1);
        Assert.assertEquals(expected.string1, actual.string1);
        Assert.assertEquals(expected.list1.size(), actual.list1.size());
        Assert.assertEquals(expected.map1.size(), actual.map1.size());
        Assert.assertNull(expected.nullableint);

        Assert.assertEquals(expected.list1, actual.list1);
        Assert.assertEquals(expected.map1, actual.map1);
        Assert.assertArrayEquals(expected.bytes1, actual.bytes1);
        Assert.assertEquals(expected.record, actual.record);

        Record2 expectedRecord2 = expected.container1.getRecord2list().get(0);
        Record2 actualRecord2 = actual.container1.getRecord2list().get(0);

        Assert.assertEquals(expectedRecord2.bigint2, actualRecord2.bigint2);
        Assert.assertEquals(expectedRecord2.enum2, actualRecord2.enum2);
        Assert.assertEquals(expectedRecord2.map2, actualRecord2.map2);
    }

    private TestSerializerSample createSample(long id) {

        TestSerializerSample sample = new TestSerializerSample();

        sample.bigint1 = id;
        sample.boolean1 = false;
        sample.double1 = 2.099328;
        sample.enum1 = Enum1Values.GREEN;
        sample.int1 = 2000;
        sample.string1 = "testSerialize";
        sample.bytes1 = "testBytes".getBytes();
        sample.list1 = Arrays.asList("a", "b", "c");
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("1a", 1);
        map.put("2b", 2);
        map.put("3c", 3);
        sample.map1 = map;
        sample.record = new Record(1, true, "testRecord");
        Record2 record2 = new Record2();
        record2.bigint2 = 2048L;
        record2.enum2 = Enum2Values.PLANE;
        Map<String, Record> recordMap = new HashMap<String, Record>();
        recordMap.put("m1", new Record(1, true, "testRecord"));
        recordMap.put("m2", new Record(2, true, "testRecord"));
        record2.map2 = recordMap;
        sample.container1 = new Record2Container(Arrays.asList(record2));

        return sample;
    }
}
