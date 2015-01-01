package com.baijioss.serializer.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Tests the BinaryEncoder and BinaryDecoder. This is a pretty general set of test cases and hence
 * can be used for any encoder and its corresponding decoder.
 */
public abstract class BinaryCodecTestBase {

    protected static interface ItemDecoder {
        Object decode(Decoder decoder) throws IOException;
    }

    protected static interface ItemEncoder {
        void encode(Encoder encoder, Object obj) throws IOException;
    }

    /**
     * Writes a baiji type T with value t into a stream using the encode method e
     * and reads it back using the decode method d and verifies that
     * the value read back is the same as the one written in.
     *
     * @param t    Value for the Baiji type to test.
     * @param r    The decode method
     * @param w    The encode method
     * @param size Expected size of serialized data
     * @param <T>  Baiji type to test
     */
    protected <T> void testRead(T t, ItemDecoder r, ItemEncoder w, int size) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Encoder e = new BinaryEncoder(os);
        w.encode(e, t);

        byte[] data = os.toByteArray();

        assertEquals(size, data.length);

        ByteArrayInputStream is = new ByteArrayInputStream(data);
        Decoder d = new BinaryDecoder(is);
        Object actual = r.decode(d);
        if (t instanceof byte[]) {
            assertArrayEquals((byte[]) t, (byte[]) actual);
        } else if (t instanceof int[]) {
            assertArrayEquals((int[]) t, (int[]) actual);
        } else if (t instanceof String[]) {
            assertArrayEquals((String[]) t, (String[]) actual);
        } else {
            assertEquals(t, actual);
        }
        assertEquals(0, is.available());
    }
}
