package com.baijioss.serializer.io;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BinaryCodecTestInt extends BinaryCodecTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{new Object[]{0, 1},
                new Object[]{1, 1},
                new Object[]{63, 1},
                new Object[]{64, 2},
                new Object[]{8191, 2},
                new Object[]{8192, 3},
                new Object[]{1048575, 3},
                new Object[]{1048576, 4},
                new Object[]{134217727, 4},
                new Object[]{134217728, 5},
                new Object[]{17179869183L, 5},
                new Object[]{17179869184L, 6},
                new Object[]{2199023255551L, 6},
                new Object[]{2199023255552L, 7},
                new Object[]{281474976710655L, 7},
                new Object[]{281474976710656L, 8},
                new Object[]{36028797018963967L, 8},
                new Object[]{36028797018963968L, 9},
                new Object[]{4611686018427387903L, 9},
                new Object[]{4611686018427387904L, 10},
                new Object[]{9223372036854775807L, 10},
                new Object[]{-1, 1},
                new Object[]{-64, 1},
                new Object[]{-65, 2},
                new Object[]{-8192, 2},
                new Object[]{-8193, 3},
                new Object[]{-1048576, 3},
                new Object[]{-1048577, 4},
                new Object[]{-134217728, 4},
                new Object[]{-134217729, 5},
                new Object[]{-17179869184L, 5},
                new Object[]{-17179869185L, 6},
                new Object[]{-2199023255552L, 6},
                new Object[]{-2199023255553L, 7},
                new Object[]{-281474976710656L, 7},
                new Object[]{-281474976710657L, 8},
                new Object[]{-36028797018963968L, 8},
                new Object[]{-36028797018963969L, 9},
                new Object[]{-4611686018427387904L, 9},
                new Object[]{-4611686018427387905L, 10},
                new Object[]{-9223372036854775808L, 10}});
    }

    private final long _value;
    private final int _size;

    public BinaryCodecTestInt(long value, int size) {
        _value = value;
        _size = size;
    }

    @Test
    public void testInt() throws IOException {
        ItemDecoder decoder = new ItemDecoder() {
            @Override
            public Object decode(Decoder decoder) throws IOException {
                return decoder.readLong();
            }
        };
        ItemEncoder encoder = new ItemEncoder() {
            @Override
            public void encode(Encoder encoder, Object obj) throws IOException {
                encoder.writeLong((Long) obj);
            }
        };
        testRead(_value, decoder, encoder, _size);
    }
}
