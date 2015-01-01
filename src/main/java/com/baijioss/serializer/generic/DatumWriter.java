package com.baijioss.serializer.generic;

import com.baijioss.serializer.io.Encoder;
import com.baijioss.serializer.schema.Schema;

import java.io.IOException;

public interface DatumWriter<D> {

    Schema getSchema();

    /**
     * Write a datum.  Traverse the schema, depth first, writing each leaf value
     * in the schema from the datum to the output.
     */
    void write(D datum, Encoder out) throws IOException;
}

