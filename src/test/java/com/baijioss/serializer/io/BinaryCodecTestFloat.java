package com.baijioss.serializer.io;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BinaryCodecTestFloat extends BinaryCodecTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{new Object[]{0.0f}, new Object[]{Float.MAX_VALUE},
                new Object[]{Float.MIN_NORMAL}, new Object[]{Float.MIN_VALUE}});
    }

    private final float _value;

    public BinaryCodecTestFloat(float value) {
        _value = value;
    }

    @Test
    public void testFloat() throws IOException {
        ItemDecoder decoder = new ItemDecoder() {
            @Override
            public Object decode(Decoder decoder) throws IOException {
                return decoder.readFloat();
            }
        };
        ItemEncoder encoder = new ItemEncoder() {
            @Override
            public void encode(Encoder encoder, Object obj) throws IOException {
                encoder.writeFloat((Float) obj);
            }
        };
        testRead(_value, decoder, encoder, 4);
    }
}
