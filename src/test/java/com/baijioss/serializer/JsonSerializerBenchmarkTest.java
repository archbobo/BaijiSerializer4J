package com.baijioss.serializer;

import com.baijioss.serializer.generic.GenericBenchmarkRecord;
import com.baijioss.serializer.specific.*;
import com.google.common.collect.Lists;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class JsonSerializerBenchmarkTest {

    private BenchmarkSerializer serializer;
    private int loop;
    private boolean run;

    private ConcurrentMap<String, List<ExecutionResult>> records = new ConcurrentHashMap<String, List<ExecutionResult>>();

    public static void main(String[] args) throws Exception {
        JsonSerializerBenchmarkTest test = new JsonSerializerBenchmarkTest();
        test.setUp();
        test.testBenchmark();
        test.tearDown();
    }

    /*@Before*/
    public void setUp() throws Exception {
        loop = 50;
        run = false;
        warmUp();
    }

    /*@Test*/
    public void testBenchmark() throws Exception {
        loop = 1000;
        run = true;
        testJsonSerializerBenchmark();
        testBinaryBenchmark();
        testJacksonBenchmark();
        print(records);
    }

    public void testJsonSerializerBenchmark() throws Exception {
        serializer = new JsonSerializerBenchmark();
        intBenchmark();
        booleanBenchmark();
        longBenchmark();
        doubleBenchmark();
        stringBenchmark();
        bytesBenchmark();
        enumBenchmark();
        arrayBenchmark();
        mapBenchmark();
        recordBenchmark();
        if (run) {
            benchmarkFiveThreads();
            benchmarkTenThreads();
            benchmarkTwentyThreads();
        }
    }

    public void testJacksonBenchmark() throws Exception {
        serializer = new JacksonBenchmark();
        intBenchmark();
        booleanBenchmark();
        longBenchmark();
        doubleBenchmark();
        stringBenchmark();
        bytesBenchmark();
        enumBenchmark();
        arrayBenchmark();
        mapBenchmark();
        recordBenchmark();
        if (run) {
            benchmarkFiveThreads();
            benchmarkTenThreads();
            benchmarkTwentyThreads();
        }
    }

    public void testBinaryBenchmark() throws Exception {
        serializer = new BinaryBenchmark();
        intBenchmark();
        booleanBenchmark();
        longBenchmark();
        doubleBenchmark();
        stringBenchmark();
        bytesBenchmark();
        enumBenchmark();
        arrayBenchmark();
        mapBenchmark();
        recordBenchmark();
        if (run) {
            benchmarkFiveThreads();
            benchmarkTenThreads();
            benchmarkTwentyThreads();
        }
    }

    @After
    public void tearDown() throws Exception {

    }

    private void warmUp() throws Exception {
        // Pre-run until the JVM Stable
        testJsonSerializerBenchmark();
        testBinaryBenchmark();
        testJacksonBenchmark();
        System.out.println("warm up done");
    }

    private void intBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark(42, "\"int\"");
        appendResults("write int", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("parse int", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void booleanBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark(true, "\"boolean\"");
        appendResults("write boolean", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("parse boolean", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void longBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark(1024 * 1024 * 16L, "\"long\"");
        appendResults("write long", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("parse long", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void doubleBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark(24.00000001, "\"double\"");
        appendResults("write double", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("parse double", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void stringBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark("testString", "\"string\"");
        appendResults("write string", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("parse string", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void bytesBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark("testBytes".getBytes(), "\"bytes\"");
        appendResults("write bytes", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("parse bytes", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void enumBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark(Enum1Values.RED, "{\"type\":\"enum\",\"name\":\"Enum1Values\",\"namespace\":\"com.baijioss.serializer.specific\",\"doc\":null,\"symbols\":[\"BLUE\",\"RED\",\"GREEN\"]}");
        appendResults("write enum", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("parse enum", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void arrayBenchmark() {
        serializer.clearCache();
        double[] results = singleFieldBenchmark(Lists.newArrayList(1, 2, 3, 4, 5), "{\"type\":\"array\",\"items\":\"int\"}");
        appendResults("write array", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("parse array", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));

    }

    private void mapBenchmark() {
        serializer.clearCache();
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("1a", 1);
        map.put("2b", 2);
        map.put("3c", 3);
        double[] results = singleFieldBenchmark(map, "{\"type\":\"map\",\"values\":\"int\"}");
        appendResults("write map", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("parse map", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void recordBenchmark() {
        serializer.clearCache();
        ModelFilling2 record = new ModelFilling2(1024 * 1024 * 16L, "testRecord", Lists.newArrayList("a", "b", "c"), Enum2Values.BIKE);
        double[] results = singleFieldBenchmark(record, record.getSchema().toString());
        appendResults("write record", new ExecutionResult(serializer.getName(), results[0], (int)results[2]));
        appendResults("parse record", new ExecutionResult(serializer.getName(), results[1], (int)results[2]));
    }

    private void benchmarkFiveThreads() throws ExecutionException, InterruptedException {
        benchmarkMultiThread(5);
    }

    private void benchmarkTenThreads() throws ExecutionException, InterruptedException {
        benchmarkMultiThread(10);
    }

    private void benchmarkTwentyThreads() throws ExecutionException, InterruptedException {
        benchmarkMultiThread(20);
    }

    private void benchmarkMultiThread(int threadNum) throws ExecutionException, InterruptedException {
        serializer.clearCache();

        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Future<List<Double>>> futures = new ArrayList<Future<List<Double>>>();

        for (int i = 0; i < threadNum; i++) {
            futures.add(executorService.submit(new Callable<List<Double>>() {
                @Override
                public List<Double> call() throws Exception {
                    ModelFilling2 record = new ModelFilling2(1024 * 1024 * 16L, "testRecord", Lists.newArrayList("a", "b", "c"), Enum2Values.BIKE);
                    double[] results = singleFieldBenchmark(record, record.getSchema().toString());
                    List<Double> result = new ArrayList<Double>();
                    result.add(results[0]);
                    result.add(results[1]);
                    result.add(results[2]);
                    return result;
                }
            }));
        }

        ArrayList<List<Double>> results = new ArrayList<List<Double>> ();
        for (Future<List<Double>> future : futures) {
            results.add(future.get());
        }

        executorService.shutdown();

        double serialize = 0;
        double deserialize = 0;
        int bytesSize = 0;

        for (int i = 0; i < results.size(); i++) {
            List<Double> perThread = results.get(i);
            serialize += perThread.get(0);
            deserialize += perThread.get(1);
            bytesSize = perThread.get(2).intValue();
        }

        serialize = serialize/results.size();
        deserialize = deserialize/results.size();

        appendResults(threadNum + " threads serialize records", new ExecutionResult(serializer.getName(), serialize, bytesSize));
        appendResults(threadNum + " threads deserialize records", new ExecutionResult(serializer.getName(), deserialize, bytesSize));
    }

    private double[] singleFieldBenchmark(Object fieldValue, String fieldType) {
        GenericBenchmarkRecord.recordType = fieldType;
        GenericBenchmarkRecord benchmarkRecord = new GenericBenchmarkRecord();
        benchmarkRecord.put(0, fieldValue);

        List<Long> serializeTimes = new ArrayList<Long>();
        List<Long> deserializeTimes = new ArrayList<Long>();

        int bytesSize = 0;

        for (int i = 0; i < loop; i++) {
            try {
            	OutputStream os = new ByteArrayOutputStream();
                long startTime = System.nanoTime();
                serializer.serialize(benchmarkRecord, os);
                long endTime = System.nanoTime();
                serializeTimes.add((endTime - startTime)/1000);

                byte[] bytes = ((ByteArrayOutputStream) os).toByteArray();
                bytesSize = bytes.length;
                InputStream is = new ByteArrayInputStream(bytes);
                long startTimeTwo = System.nanoTime();
                serializer.deserialize(GenericBenchmarkRecord.class, is);
                long endTimeTwo = System.nanoTime();
                deserializeTimes.add((endTimeTwo - startTimeTwo)/1000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new double[]{aggregateResults(serializeTimes), aggregateResults(deserializeTimes), bytesSize};
    }

    private double aggregateResults(List<Long> results) {
        double sum = 0;
        for (Long result : results) {
            sum += result;
        }

        return sum/results.size();
    }

    private void appendResults(String type, ExecutionResult result) {
        if (!run)
            return;

        if (records.containsKey(type)) {
            records.get(type).add(result);
        } else {
            List<ExecutionResult> resultList = new ArrayList<ExecutionResult>();
            resultList.add(result);
            records.put(type, resultList);
        }
    }

    private void print(ConcurrentMap<String, List<ExecutionResult>> result) {
        if (!run)
            return;

        String str = "(" + loop + " loops" + ")";
        for (String key : result.keySet()) {
            System.out.println(key + str);
            List<ExecutionResult> re = result.get(key);
            for (ExecutionResult r : re) {
                System.out.println("\t\t\t" + r.serializer + " " + ": " + r.time + "(avg) μs/op, " + r.bytesSize + " bytes");
            }
        }
    }

    class JsonSerializerBenchmark implements BenchmarkSerializer {

        private JsonSerializer jsonSerializer = new JsonSerializer();

        @Override
        public String getName() {
            return "Self JsonSerializer";
        }

        @Override
        public <T extends SpecificRecord> void serialize(T obj, OutputStream os) throws IOException {
            jsonSerializer.serialize(obj, os);
        }

        @Override
        public <T extends SpecificRecord> T deserialize(Class<T> clazz, InputStream is) throws IOException {
            return jsonSerializer.deserialize(clazz, is);
        }

        @Override
        public void clearCache() {
            //jsonSerializer.clearCache();
        }
    }


    class JacksonBenchmark implements BenchmarkSerializer {

        private ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public String getName() {
            return "Jackson DataBind";
        }

        @Override
        public <T extends SpecificRecord> void serialize(T obj, OutputStream os) throws IOException {
            objectMapper.writeValue(os, obj);
        }

        @Override
        public <T extends SpecificRecord> T deserialize(Class<T> clazz, InputStream is) throws IOException {
            return objectMapper.readValue(is, clazz);
        }

        @Override
        public void clearCache() {
        }
    }

    class BinaryBenchmark implements BenchmarkSerializer {

        private BinarySerializer binarySerializer = new BinarySerializer();

        @Override
        public String getName() {
            return "Self Binary Serializer";
        }

        @Override
        public <T extends SpecificRecord> void serialize(T obj, OutputStream os) throws IOException {
            binarySerializer.serialize(obj, os);
        }

        @Override
        public <T extends SpecificRecord> T deserialize(Class<T> clazz, InputStream is) throws IOException {
            return binarySerializer.deserialize(clazz, is);
        }

        @Override
        public void clearCache() {
            binarySerializer.clearCache();
        }
    }

    interface BenchmarkSerializer {

        String getName();

        <T extends SpecificRecord> void serialize(T obj, OutputStream os) throws IOException;

        <T extends SpecificRecord> T deserialize(Class<T> clazz, InputStream is) throws IOException;

        void clearCache();
    }

}

class ExecutionResult {
    public String serializer;
    public double time;
    public int bytesSize;

    public ExecutionResult(String serializer, double time, int bytesSize) {
        this.serializer = serializer;
        this.time = time;
        this.bytesSize = bytesSize;
    }
}
