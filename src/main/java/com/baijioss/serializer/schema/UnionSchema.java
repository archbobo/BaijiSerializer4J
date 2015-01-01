package com.baijioss.serializer.schema;


import com.baijioss.serializer.util.ObjectUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnionSchema extends UnnamedSchema {
    private final List<Schema> _schemas;

    public UnionSchema(List<Schema> schemas, PropertyMap props) {
        super(SchemaType.UNION, props);
        if (schemas == null) {
            throw new IllegalArgumentException("schemas");
        }
        _schemas = schemas;
    }

    /**
     * Static function to return instance of the union schema
     *
     * @param array    JSON object for the union schema
     * @param props
     * @param names    list of named schemas already read
     * @param encSpace enclosing namespace of the schema
     * @return
     */
    static UnionSchema newInstance(ArrayNode array, PropertyMap props, SchemaNames names, String encSpace) {
        List<Schema> schemas = new ArrayList<Schema>();
        Map<String, String> uniqueSchemas = new HashMap<String, String>();

        for (JsonNode node : array) {
            Schema unionType = parseJson(node, names, encSpace);
            if (null == unionType) {
                throw new SchemaParseException("Invalid JSON in union" + node);
            }

            String name = unionType.getName();
            if (uniqueSchemas.containsKey(name)) {
                throw new SchemaParseException("Duplicate type in union: " + name);
            }

            uniqueSchemas.put(name, name);
            schemas.add(unionType);
        }

        return new UnionSchema(schemas, props);
    }


    /**
     * @return List of schemas in the union
     */
    public List<Schema> getSchemas() {
        return _schemas;
    }

    /**
     * @return Count of schemas in the union
     */
    public int size() {
        return _schemas.size();
    }

    /**
     * Returns the schema at the given branch.
     *
     * @param index Index to the branch, starting with 0.
     * @return The branch corresponding to the given index.
     */
    public Schema get(int index) {
        return _schemas.get(index);

    }

    /**
     * Writes union schema in JSON format
     *
     * @param writer
     * @param names    list of named schemas already written
     * @param encSpace enclosing namespace of the schema
     */
    protected void writeJson(JsonGenerator writer, SchemaNames names,
                             String encSpace)
            throws IOException {
        writer.writeStartArray();
        for (Schema schema : _schemas) {
            schema.writeJson(writer, names, encSpace);
        }
        writer.writeEndArray();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UnionSchema)) {
            return false;
        }
        UnionSchema that = (UnionSchema) obj;
        if (!that._schemas.equals(_schemas)) {
            return false;
        }
        return ObjectUtils.equals(that.getProps(), getProps());
    }


    @Override
    public int hashCode() {
        int result = 53;
        for (Schema schema : _schemas) {
            result += 89 * schema.hashCode();
        }
        result += ObjectUtils.hashCode(getProps());
        return result;
    }
}
