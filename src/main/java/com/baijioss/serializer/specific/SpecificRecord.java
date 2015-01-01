package com.baijioss.serializer.specific;

import com.baijioss.serializer.schema.Schema;

/**
 * Implemented by generated record classes. Permits efficient access to
 * fields.
 */
public interface SpecificRecord {

    public Schema getSchema();

    /**
     * Gets the value of a field given its position.
     *
     * @param fieldPos
     * @return
     */
    Object get(int fieldPos);

    /**
     * Sets the value of a field given its position.
     *
     * @param fieldPos
     * @param fieldValue
     */
    void put(int fieldPos, Object fieldValue);

    /**
     * Gets the value of a field given its name.
     *
     * @param fieldName
     * @return
     */
    Object get(String fieldName);

    /**
     * Sets the value of a field given its name.
     *
     * @param fieldName
     * @param fieldValue
     */
    void put(String fieldName, Object fieldValue);
}

