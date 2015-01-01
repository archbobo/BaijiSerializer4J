package com.baijioss.serializer.schema;

import com.baijioss.serializer.util.ObjectUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Field {

    public enum SortOrder {
        ASCENDING,
        DESCENDING,
        IGNORE
    }

    private final String _name;

    private final List<String> _aliases;

    private final int _pos;

    private final String _doc;

    private final JsonNode _defaultValue;

    private final SortOrder _ordering;

    private final Schema _schema;

    private final PropertyMap _props;

    /**
     * Constructor for the field class
     *
     * @param schema       schema for the field type
     * @param name         name of the field
     * @param aliases      list of aliases for the name of the field
     * @param pos          position of the field
     * @param doc          documentation for the field
     * @param defaultValue field's default value if it exists
     * @param sortOrder    sort order of the field
     * @param props
     */
    public Field(Schema schema, String name, List<String> aliases, int pos, String doc,
          JsonNode defaultValue, SortOrder sortOrder, PropertyMap props) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name cannot be null.");
        }
        if (schema == null) {
            throw new IllegalArgumentException("schema cannot be null.");
        }
        _schema = schema;
        _name = name;
        _aliases = aliases;
        _pos = pos;
        _doc = doc;
        _defaultValue = defaultValue;
        _ordering = sortOrder;
        _props = props;
    }

    public String getName() {
        return _name;
    }

    public List<String> getAliases() {
        return _aliases;
    }

    public int getPos() {
        return _pos;
    }

    public String getDoc() {
        return _doc;
    }

    public JsonNode getDefaultValue() {
        return _defaultValue;
    }

    public SortOrder getOrdering() {
        return _ordering;
    }

    public Schema getSchema() {
        return _schema;
    }

    /**
     * Writes the Field class in JSON format
     *
     * @param node
     * @param names
     * @param encSpace
     */
    protected void writeJson(JsonGenerator node, SchemaNames names, String encSpace)
            throws IOException {
        node.writeStartObject();
        JsonHelper.writeIfNotNullOrEmpty(node, "name", _name);
        JsonHelper.writeIfNotNullOrEmpty(node, "doc", _doc);

        if (_defaultValue != null) {
            node.writeFieldName("default");
            node.writeTree(_defaultValue);
        }
        if (_schema != null) {
            node.writeFieldName("type");
            _schema.writeJson(node, names, encSpace);
        }

        if (_props != null) {
            _props.writeJson(node);
        }

        if (_aliases != null) {
            node.writeFieldName("aliases");
            node.writeStartArray();
            for (String name : _aliases) {
                node.writeString(name);
            }
            node.writeEndArray();
        }

        node.writeEndObject();
    }

    /**
     * Parses the 'aliases' property from the given JSON token
     *
     * @param node JSON object to read
     * @return List of string that represents the list of alias. If no 'aliases' specified, then it returns null.
     */
    static List<String> getAliases(JsonNode node) {
        JsonNode aliasesNode = node.get("aliases");
        if (aliasesNode == null) {
            return null;
        }

        if (!aliasesNode.isArray()) {
            throw new SchemaParseException("Aliases must be of format JSON array of strings");
        }

        List<String> aliases = new ArrayList<String>();
        for (JsonNode aliasNode : aliasesNode) {
            if (!aliasNode.isTextual()) {
                throw new SchemaParseException("Aliases must be of format JSON array of strings");
            }

            aliases.add(aliasNode.getTextValue());
        }
        return aliases;
    }

    /**
     * Returns the field's custom property value given the property name
     *
     * @param key
     * @return
     */
    public String getProp(String key) {
        if (_props == null) {
            return null;
        }
        return _props.get(key);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Field)) {
            return false;
        }
        Field that = (Field) obj;
        return ObjectUtils.equals(that._name, _name) && that._pos == _pos &&
                ObjectUtils.equals(that._doc, _doc)
                && ObjectUtils.equals(that._ordering, _ordering) &&
                defaultValueEquals(that._defaultValue)
                && that._schema.equals(_schema) && ObjectUtils.equals(that._props, _props);
    }

    private boolean defaultValueEquals(JsonNode thatDefaultValue) {
        if (_defaultValue == null) {
            return thatDefaultValue == null;
        }
        if (Double.isNaN(_defaultValue.getDoubleValue())) {
            return Double.isNaN(thatDefaultValue.getDoubleValue());
        }
        return _defaultValue.equals(thatDefaultValue);
    }

    @Override
    public int hashCode() {
        return 17 * _name.hashCode() + _pos + 19 * ObjectUtils.hashCode(_doc) +
                23 * ObjectUtils.hashCode(_ordering) + 29 * ObjectUtils.hashCode(_defaultValue) +
                31 * _schema.hashCode() + 37 * ObjectUtils.hashCode(_props);
    }
}
