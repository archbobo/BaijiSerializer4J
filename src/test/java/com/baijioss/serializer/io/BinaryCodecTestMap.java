package com.baijioss.serializer.io;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BinaryCodecTestMap extends BinaryCodecTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{new Object[]{1, new String[]{}},
                new Object[]{6, new String[]{"a", "b"}},
                new Object[]{9, new String[]{"a", "b", "c", ""}}});
    }

    private final int _size;
    private final String[] _entries;

    public BinaryCodecTestMap(int size, String[] entries) {
        _size = size;
        _entries = entries;
    }

    @Test
    public void testMap() throws IOException {
        ItemDecoder decoder = new ItemDecoder() {
            @Override
            public Object decode(Decoder decoder) throws IOException {
                int length = (int) decoder.readArrayStart() * 2;
                String[] entries = new String[length];
                if (length != 0) {
                    for (int i = 0; i < length; i += 2) {
                        entries[i] = decoder.readString();
                        entries[i + 1] = decoder.readString();
                    }
                    Assert.assertEquals(0, decoder.readArrayNext());
                }
                return entries;
            }
        };
        ItemEncoder encoder = new ItemEncoder() {
            @Override
            public void encode(Encoder encoder, Object obj) throws IOException {
                String[] entries = (String[]) obj;
                encoder.writeArrayStart();
                encoder.setItemCount(entries.length / 2);
                for (int i = 0; i < entries.length; i += 2) {
                    encoder.startItem();
                    encoder.writeString(entries[i]);
                    encoder.writeString(entries[i + 1]);
                }
                encoder.writeArrayEnd();
            }
        };
        testRead(_entries, decoder, encoder, _size);
    }
}
