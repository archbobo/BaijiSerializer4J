package com.baijioss.serializer.io;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BinaryCodecTestString extends BinaryCodecTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{new Object[]{"", 1},
                new Object[]{"hello", 1},
                new Object[]{"1234567890123456789012345678901234567890123456789012345678901234", 2},
                new Object[]{"你好世界", 1}});
    }

    private final String _data;
    private final int _overhead;

    public BinaryCodecTestString(String data, int overhead) {
        _data = data;
        _overhead = overhead;
    }

    @Test
    public void testString() throws IOException {
        ItemDecoder decoder = new ItemDecoder() {
            @Override
            public Object decode(Decoder decoder) throws IOException {
                return decoder.readString();
            }
        };
        ItemEncoder encoder = new ItemEncoder() {
            @Override
            public void encode(Encoder encoder, Object obj) throws IOException {
                encoder.writeString((String) obj);
            }
        };
        testRead(_data, decoder, encoder, _data.getBytes("utf-8").length + _overhead);
    }
}
