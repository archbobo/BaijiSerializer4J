package com.baijioss.serializer;

import com.baijioss.serializer.specific.SpecificRecord;
import com.baijioss.serializer.specific.TestSerializerSampleList;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonSerializerPerfTest {

    JsonSerializer serializer;

    @Before
    public void setUp() throws Exception {
        serializer = new JsonSerializer();
    }

    @Test
    public void testDeserialize_10tiny() throws Exception {
        testDeserialize("/t10records.json", 10, 5, "with tiny data");
    }

    @Test
    public void testDeserialize_50tiny() throws Exception {
        testDeserialize("/t50records.json", 50, 5, "with tiny data");
    }

    @Test
    public void testDeserialize_300tiny() throws Exception {
        testDeserialize("/t300records.json", 300, 5, "with tiny data");
    }

    @Test
    public void testDeserialize_10big() throws Exception {
        testDeserialize("/b10records.json", 10, 5, "with big data");
    }

    @Test
    public void testDeserialize_50big() throws Exception {
        testDeserialize("/b50records.json", 50, 5, "with big data");
    }

    @Test
    public void testDeserialize_300big() throws Exception {
        testDeserialize("/b300records.json", 300, 5, "with big data");
    }

    protected void testDeserialize(String fileName, int size, int loop, String dataType) {
        List<Long> results = new ArrayList<Long>();
        for (int i = 0; i < loop; i++) {
            try {
            	InputStream in = JsonSerializerPerfTest.class.getResourceAsStream(fileName);
                long start = System.currentTimeMillis();
                TestSerializerSampleList sample = deserialize(TestSerializerSampleList.class, in);
                long end = System.currentTimeMillis();
                results.add(end - start);
            } catch (IOException e) {/**/}
        }

        long[] pef = readResults(results);
        System.out.println("parse " + size + " records (" + loop + " times)" + dataType + " : " + pef[0] / size + " (min) ms/op\n");
        System.out.println("parse " + size + " records (" + loop + " times)" + dataType + " : " + pef[1] / size + " (max) ms/op\n");
        System.out.println("parse " + size + " records (" + loop + " times)" + dataType + " : " + pef[2] / size + " (avg) ms/op\n");
    }

    private long[] readResults(List<Long> results) {
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        long sum = 0L;
        long avg = 0L;

        for (int i = 0; i < results.size(); i++) {
            long l = results.get(i);
            if (min > l)
                min = l;
            if (max < l)
                max = l;

            sum += l;
        }

        avg = sum / results.size();
        return new long[]{min, max, avg};
    }

    private <T extends SpecificRecord> T deserialize(Class<T> clazz, InputStream is) throws IOException {
        return serializer.deserialize(clazz, is);
    }
}
