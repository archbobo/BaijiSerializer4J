package com.baijioss.serializer.io;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

@RunWith(Parameterized.class)
public class BinaryCodecTestBytes extends BinaryCodecTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{new Object[]{0, 1},
                new Object[]{5, 1},
                new Object[]{63, 1},
                new Object[]{64, 2},
                new Object[]{8191, 2},
                new Object[]{8192, 3}});
    }

    private final int _dataLength;
    private final int _overhead;

    public BinaryCodecTestBytes(int dataLength, int overhead) {
        _dataLength = dataLength;
        _overhead = overhead;
    }

    @Test
    public void testBytes() throws IOException {
        final byte[] data = generateRandomBytes(_dataLength);
        ItemDecoder decoder = new ItemDecoder() {
            @Override
            public Object decode(Decoder decoder) throws IOException {
                return decoder.readBytes();
            }
        };
        ItemEncoder encoder = new ItemEncoder() {
            @Override
            public void encode(Encoder encoder, Object obj) throws IOException {
                encoder.writeBytes((byte[]) obj);
            }
        };
        testRead(data, decoder, encoder, _dataLength + _overhead);
    }

    private static byte[] generateRandomBytes(int length) {
        byte[] data = new byte[length];
        Random rand = new Random();
        rand.nextBytes(data);
        return data;
    }
}
