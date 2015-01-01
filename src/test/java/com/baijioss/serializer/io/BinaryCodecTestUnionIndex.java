package com.baijioss.serializer.io;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BinaryCodecTestUnionIndex extends BinaryCodecTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{new Object[]{0, 1},
                new Object[]{1, 1},
                new Object[]{64, 2}});
    }

    private final int _value;
    private final int _size;

    public BinaryCodecTestUnionIndex(int value, int size) {
        _value = value;
        _size = size;
    }

    @Test
    public void testUnionIndex() throws IOException {
        ItemDecoder decoder = new ItemDecoder() {
            @Override
            public Object decode(Decoder decoder) throws IOException {
                return decoder.readUnionIndex();
            }
        };
        ItemEncoder encoder = new ItemEncoder() {
            @Override
            public void encode(Encoder encoder, Object obj) throws IOException {
                encoder.writeUnionIndex((Integer) obj);
            }
        };
        testRead(_value, decoder, encoder, _size);
    }
}
