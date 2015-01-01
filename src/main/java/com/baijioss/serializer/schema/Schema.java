package com.baijioss.serializer.schema;

import com.baijioss.serializer.exception.BaijiRuntimeException;
import com.baijioss.serializer.exception.BaijiTypeException;
import com.baijioss.serializer.util.ObjectUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;

import java.io.IOException;
import java.io.StringWriter;

/**
 * An abstract data type.
 * <p>A schema may be one of:
 * <ul>
 * <li>A <i>record</i>, mapping field names to field value data;
 * <li>An <i>enum</i>, containing one of a small set of symbols;
 * <li>An <i>array</i> of values, all of the same schema;
 * <li>A <i>map</i>, containing string/value pairs, of a declared schema;
 * <li>A <i>union</i> of other schemas;
 * <li>A unicode <i>string</i>;
 * <li>A sequence of <i>bytes</i>;
 * <li>A 32-bit signed <i>int</i>;
 * <li>A 64-bit signed <i>long</i>;
 * <li>A 32-bit IEEE single-<i>float</i>; or
 * <li>A 64-bit IEEE <i>double</i>-float; or
 * <li>A <i>boolean</i>; or
 * <li><i>null</i>.
 * </ul>
 * <p/>
 * methods. The schema objects are <i>logically</i> immutable.
 */
public abstract class Schema {
    static final JsonFactory FACTORY = new JsonFactory();
    static final ObjectMapper MAPPER = new ObjectMapper(FACTORY);

    static {
        FACTORY.enable(JsonParser.Feature.ALLOW_COMMENTS);
        FACTORY.setCodec(MAPPER);
    }

    private final SchemaType _type;
    private final PropertyMap _props;

    Schema(SchemaType type, PropertyMap props) {
        this._type = type;
        this._props = props;
    }


    /**
     * Return the type of this schema.
     */
    public SchemaType getType() {
        return _type;
    }

    /**
     * Return additional JSON attributes apart from those defined in the Baiji spec
     */
    public PropertyMap getProps() {
        return _props;
    }

    /**
     * Returns the schema's custom property value given the property name
     *
     * @param key
     * @return
     */
    public String getProp(String key) {
        if (null == _props) {
            return null;
        }
        return _props.get(key);
    }

    /**
     * The name of this schema. If this is a named schema such as an enum,
     * it returns the fully qualified name for the schema.
     * For other schemas, it returns the type of the schema.
     *
     * @return
     */
    public abstract String getName();

    /**
     * Parses a JSON string to create a new schema object
     *
     * @param json JSON string
     * @return
     */
    public static Schema parse(String json) {
        if (json == null || json.isEmpty()) {
            throw new IllegalArgumentException("json cannot be null or empty.");
        }
        return parse(json.trim(), new SchemaNames(), null); // standalone schema, so no enclosing namespace
    }

    /**
     * Parses a JSON string to create a new schema object
     *
     * @param json     JSON string
     * @param names    list of named schemas already read
     * @param encSpace enclosing namespace of the schema
     * @return
     */
    static Schema parse(String json, SchemaNames names, String encSpace) {
        Schema schema = PrimitiveSchema.newInstance(json);
        if (schema != null) {
            return schema;
        }

        try {
            JsonNode node = MAPPER.readTree(json);
            return parseJson(node, names, encSpace);
        } catch (Throwable t) {
            throw new SchemaParseException("Could not parse. " + t.getMessage() + "\n" + json);
        }
    }

    /**
     * Static method to return new instance of schema object
     *
     * @param node     JSON object
     * @param names    list of named schemas already read
     * @param encSpace enclosing namespace of the schema
     * @return
     */
    static Schema parseJson(JsonNode node, SchemaNames names, String encSpace) {
        if (node == null) {
            throw new IllegalArgumentException("node cannot be null.");
        }

        if (node.isTextual()) {
            String value = node.getTextValue();
            PrimitiveSchema ps = PrimitiveSchema.newInstance(value);
            if (ps != null) {
                return ps;
            }

            NamedSchema schema = names.getSchema(value, null, encSpace);
            if (schema != null) {
                return schema;
            }
            throw new SchemaParseException("Undefined name: " + value);
        } else if (node.isArray()) {
            return UnionSchema.newInstance((ArrayNode) node, null, names, encSpace);
        } else if (node.isObject()) {

            JsonNode typeNode = node.get("type");
            if (typeNode == null) {
                throw new SchemaParseException("Property type is required");
            }

            PropertyMap props = JsonHelper.getProperties(node);

            if (typeNode.isTextual()) {
                String type = typeNode.getTextValue();

                if ("array".equals(type)) {
                    return ArraySchema.newInstance(node, props, names, encSpace);
                } else if ("map".equals(type)) {
                    return MapSchema.newInstance(node, props, names, encSpace);
                }

                PrimitiveSchema ps = PrimitiveSchema.newInstance(type);
                if (ps != null) {
                    return ps;
                }

                return NamedSchema.newInstance(node, props, names, encSpace);
            } else if (typeNode.isArray()) {
                return UnionSchema.newInstance((ArrayNode) typeNode, props, names, encSpace);
            }
        }

        throw new BaijiTypeException("Invalid JSON for schema: " + node);
    }

    /**
     * Render this as <a href="http://json.org/">JSON</a>.
     */
    @Override
    public String toString() {
        return toString(false);
    }

    /**
     * Render this as <a href="http://json.org/">JSON</a>.
     *
     * @param pretty if true, pretty-print JSON.
     */
    public String toString(boolean pretty) {
        try {
            StringWriter writer = new StringWriter();
            JsonGenerator gen = FACTORY.createJsonGenerator(writer);
            if (pretty) gen.useDefaultPrettyPrinter();

            if (this instanceof PrimitiveSchema || this instanceof UnionSchema) {
                gen.writeStartObject();
                gen.writeFieldName("type");
            }

            writeJson(gen, new SchemaNames(), null);

            if (this instanceof PrimitiveSchema || this instanceof UnionSchema) {
                gen.writeEndObject();
            }

            gen.flush();
            return writer.toString();
        } catch (IOException e) {
            throw new BaijiRuntimeException(e);
        }
    }

    /**
     * Writes schema object in JSON format
     *
     * @param gen      JSON generator
     * @param names    list of named schemas already written
     * @param encSpace enclosing namespace of the schema
     */
    protected void writeJson(JsonGenerator gen, SchemaNames names, String encSpace) throws IOException {
        writeStartObject(gen);
        writeJsonFields(gen, names, encSpace);
        if (null != _props) {
            _props.writeJson(gen);
        }
        gen.writeEndObject();
    }

    /**
     * Writes opening { and 'type' property
     *
     * @param gen
     */
    private void writeStartObject(JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("type");
        gen.writeString(_type.toString().toLowerCase());
    }

    /**
     * Default implementation for writing schema properties in JSON format
     *
     * @param gen      JSON generator
     * @param names    list of named schemas already written
     * @param encSpace enclosing namespace of the schema
     */
    protected void writeJsonFields(JsonGenerator gen, SchemaNames names, String encSpace) throws IOException {
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Schema)) {
            return false;
        }
        Schema that = (Schema) o;
        if (this._type != that._type) {
            return false;
        }
        return _props != null ? _props.equals(that._props) : that._props == null;
    }

    public int hashCode() {
        return _type.hashCode() + ObjectUtils.hashCode(_props);
    }
}
