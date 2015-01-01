package com.baijioss.serializer.io;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BinaryCodecTestArray extends BinaryCodecTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{new Object[]{1, new int[]{}},
                new Object[]{3, new int[]{0}},
                new Object[]{4, new int[]{64}},
                new Object[]{6, new int[]{64, 128}}});
    }

    private final int _size;
    private final int[] _entries;

    public BinaryCodecTestArray(int size, int[] entries) {
        _size = size;
        _entries = entries;
    }

    @Test
    public void testArray() throws IOException {
        ItemDecoder decoder = new ItemDecoder() {
            @Override
            public Object decode(Decoder decoder) throws IOException {
                int length = (int) decoder.readArrayStart();
                int[] entries = new int[length];
                if (length != 0) {
                    for (int i = 0; i < length; i++) {
                        entries[i] = decoder.readInt();
                    }
                    Assert.assertEquals(0, decoder.readArrayNext());
                }
                return entries;
            }
        };
        ItemEncoder encoder = new ItemEncoder() {
            @Override
            public void encode(Encoder encoder, Object obj) throws IOException {
                int[] entries = (int[]) obj;
                encoder.writeArrayStart();
                encoder.setItemCount(entries.length);
                for (int i : entries) {
                    encoder.startItem();
                    encoder.writeInt(i);
                }
                encoder.writeArrayEnd();
            }
        };
        testRead(_entries, decoder, encoder, _size);
    }
}
