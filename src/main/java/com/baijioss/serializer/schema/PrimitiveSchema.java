package com.baijioss.serializer.schema;

import com.baijioss.serializer.util.ObjectUtils;
import org.codehaus.jackson.JsonGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class PrimitiveSchema extends UnnamedSchema {

    private static final Map<String, SchemaType> _typeMap = new HashMap<String, SchemaType>();

    static {
        _typeMap.put("null", SchemaType.NULL);
        _typeMap.put("boolean", SchemaType.BOOLEAN);
        _typeMap.put("int", SchemaType.INT);
        _typeMap.put("long", SchemaType.LONG);
        _typeMap.put("float", SchemaType.FLOAT);
        _typeMap.put("double", SchemaType.DOUBLE);
        _typeMap.put("bytes", SchemaType.BYTES);
        _typeMap.put("string", SchemaType.STRING);
    }

    public PrimitiveSchema(SchemaType type, PropertyMap props) {
        super(type, props);
    }

    /**
     * Create a schema for a primitive type.
     */
    public static PrimitiveSchema newInstance(String type) {
        return newInstance(type, null);
    }

    /**
     * Create a schema for a primitive type.
     */
    public static PrimitiveSchema newInstance(String type, PropertyMap props) {
        final String quote = "\"";
        if (type.startsWith(quote) && type.endsWith(quote)) {
            type = type.substring(1, type.length() - 2);
        }
        SchemaType schemaType = _typeMap.get(type);
        return schemaType != null ? new PrimitiveSchema(schemaType, props) : null;
    }

    /**
     * Writes schema object in JSON format
     *
     * @param gen      JSON generator
     * @param names    list of named schemas already written
     * @param encSpace enclosing namespace of the schema
     */
    @Override
    protected void writeJson(JsonGenerator gen, SchemaNames names, String encSpace) throws IOException {
        gen.writeString(getName());
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PrimitiveSchema)) {
            return false;
        }
        PrimitiveSchema that = (PrimitiveSchema) obj;
        return getType() == that.getType() && ObjectUtils.equals(that.getProps(), getProps());
    }

    @Override
    public int hashCode() {
        return 13 * getType().hashCode() + ObjectUtils.hashCode(getProps());
    }
}
