package com.baijioss.serializer;

import com.baijioss.serializer.specific.TestSerializerSampleList;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class JsonSerializerPerfTestThree {

    JsonSerializer serializer;

    @Before
    public void setUp() throws Exception {
        serializer = new JsonSerializer();
    }

    @Test
    public void testDeserialize_5Threads() throws Exception {
        testDeserialize(5, 10);
    }

    @Test
    public void testDeserialize_20Threads() throws Exception {
        testDeserialize(20, 10);
    }

    @Test
    public void testDeserialize_128Threads() throws Exception {
        testDeserialize(128, 10);
    }

    private void testDeserialize(int threadNumber, final int loop) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Future<List<Long>>> futures = new ArrayList<Future<List<Long>>>();

        for (int i = 0; i < threadNumber; i++) {
            futures.add(executorService.submit(new Callable<List<Long>>() {
                @Override
                public List<Long> call() throws Exception {
                    List<Long> results = new ArrayList<Long>();
                    for (int i = 0; i < loop; i++) {
                        try {
                        	InputStream is = JsonSerializerPerfTestThree.class.getResourceAsStream("/t50records.json");
                            long start = System.currentTimeMillis();
                            TestSerializerSampleList sample = serializer.deserialize(TestSerializerSampleList.class, is);
                            long end = System.currentTimeMillis();
                            results.add(end - start);
                        } catch (IOException e) {

                        }
                    }

                    return results;
                }
            }));
        }

        List<List<Long>> results = new ArrayList<List<Long>>();
        for (Future<List<Long>> future : futures) {
            results.add(future.get());
        }

        // shutdown the thread pool
        executorService.shutdown();

        long[] result = readResults(results);
        System.out.println("parse 50 records (" + loop + " loops, " + threadNumber + " threads) : " + result[0] / 50 + " (min) ms/op");
        System.out.println("parse 50 records (" + loop + " loops, " + threadNumber + " threads) : " + result[1] / 50 + " (max) ms/op");
        System.out.println("parse 50 records (" + loop + " loops, " + threadNumber + " threads) : " + result[2] / 50 + " (avg) ms/op");
    }

    private long[] readResults(List<List<Long>> results) {
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        long sum = 0L;
        long avg = 0L;

        for (int i = 0; i < results.size(); i++) {
            List<Long> result = results.get(i);
            for (int j = 0; j < result.size(); j++) {
                if (min > result.get(j))
                    min = result.get(j);
                if (max < result.get(j))
                    max = result.get(j);

                sum += result.get(j);
            }
        }

        avg = sum / results.size();
        return new long[]{min, max, avg};
    }
}
