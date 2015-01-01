package com.baijioss.serializer.generic;

import com.baijioss.serializer.io.Decoder;
import com.baijioss.serializer.schema.Schema;

import java.io.IOException;

public interface DatumReader<D> {

    Schema getSchema();

    /**
     * Read a datum.
     * Traverse the schema, depth-first, reading all leaf values
     * in the schema into a datum that is returned.  If the provided datum is
     * non-null it may be reused and returned.
     */
    D read(D reuse, Decoder in) throws IOException;
}

