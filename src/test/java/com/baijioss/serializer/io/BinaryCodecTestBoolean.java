package com.baijioss.serializer.io;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BinaryCodecTestBoolean extends BinaryCodecTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{new Object[]{Boolean.TRUE}, new Object[]{Boolean.FALSE}});
    }

    private final boolean _value;

    public BinaryCodecTestBoolean(boolean value) {
        _value = value;
    }

    @Test
    public void testBoolean() throws IOException {
        ItemDecoder decoder = new ItemDecoder() {
            @Override
            public Object decode(Decoder decoder) throws IOException {
                return decoder.readBoolean();
            }
        };
        ItemEncoder encoder = new ItemEncoder() {
            @Override
            public void encode(Encoder encoder, Object obj) throws IOException {
                encoder.writeBoolean((Boolean) obj);
            }
        };
        testRead(_value, decoder, encoder, 1);
    }
}
