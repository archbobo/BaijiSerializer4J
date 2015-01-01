package com.baijioss.serializer.schema;

import com.baijioss.serializer.exception.BaijiRuntimeException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class NamedSchema extends Schema {

    private final SchemaName _schemaName;
    private final String _doc;
    private final List<SchemaName> _aliases;

    /**
     * Constructor for named schema class.
     *
     * @param type
     * @param schemaName
     * @param doc
     * @param aliases
     * @param props
     * @param names      list of named schemas already read
     */
    protected NamedSchema(SchemaType type, SchemaName schemaName, String doc, List<SchemaName> aliases,
                          PropertyMap props, SchemaNames names) {
        super(type, props);
        _schemaName = schemaName;
        _doc = doc;
        _aliases = aliases;
        if (schemaName.getName() != null && !names.add(schemaName, this)) {
            throw new BaijiRuntimeException("Duplicated schema name " + schemaName.getFullName());
        }
    }

    public SchemaName getSchemaName() {
        return _schemaName;
    }

    @Override
    public String getName() {
        return _schemaName.getName();
    }

    public String getNamespace() {
        return _schemaName.getNamespace();
    }

    public String getFullName() {
        return _schemaName.getFullName();
    }

    public String getDoc() {
        return _doc;
    }

    static NamedSchema newInstance(JsonNode node, PropertyMap props, SchemaNames names,
                                   String encSpace) {
        String type = JsonHelper.getRequiredString(node, "type");
        if ("enum".equals(type)) {
            return EnumSchema.newInstance(node, props, names, encSpace);
        } else if ("record".equals(type)) {
            return RecordSchema.newInstance(node, props, names, encSpace);
        } else {
            return names.getSchema(type, null, encSpace);
        }
    }

    /**
     * Parses the name and namespace from the given JSON schema object then creates
     * SchemaName object including the given enclosing namespace
     *
     * @param node     JSON object to read
     * @param encspace JSON object to read
     * @return
     */
    protected static SchemaName getName(JsonNode node, String encspace) {
        String n = JsonHelper.getOptionalString(node, "name");
        // Changed this to optional string for anonymous records in messages
        String ns = JsonHelper.getOptionalString(node, "namespace");
        return new SchemaName(n, ns, encspace);
    }

    /**
     * Writes named schema in JSON format
     *
     * @param gen      JSON generator
     * @param names    list of named schemas already written
     * @param encSpace enclosing namespace of the schema
     */
    @Override
    protected void writeJson(JsonGenerator gen, SchemaNames names, String encSpace)
            throws IOException {
        if (!names.add(this)) {
            // schema is already in the list, write name only
            SchemaName schemaName = getSchemaName();
            String name;
            if (schemaName.getNamespace() != encSpace) {
                name = schemaName.getNamespace() + "." + schemaName.getName();
                // we need to add the qualifying namespace of the target schema if it's not the same as current namespace
            } else {
                name = schemaName.getName();
            }
            gen.writeString(name);
        } else {
            // schema is not in the list, write full schema definition
            super.writeJson(gen, names, encSpace);
        }
    }

    /**
     * Writes named schema in JSON format
     *
     * @param gen      JSON generator
     * @param names    list of named schemas already written
     * @param encSpace enclosing namespace of the schema
     */
    @Override
    protected void writeJsonFields(JsonGenerator gen, SchemaNames names, String encSpace) throws IOException {
        _schemaName.writeJson(gen, names);

        if (_doc != null && !_doc.isEmpty()) {
            gen.writeFieldName("doc");
            gen.writeString(_doc);
        }

        if (_aliases != null) {
            gen.writeFieldName("aliases");
            gen.writeStartArray();
            for (SchemaName name : _aliases) {
                String fullname = name.getSpace() != null ? name.getSpace() + "." + name.getName() : name.getName();
                gen.writeString(fullname);
            }
            gen.writeEndArray();
        }
    }

    /**
     * Parses the 'aliases' property from the given JSON token
     *
     * @param node     JSON object to read
     * @param space    namespace of the name this alias is for
     * @param encSpace enclosing namespace of the name this alias is for
     * @return List of SchemaName that represents the list of alias. If no 'aliases' specified, then it returns null.
     */
    protected static List<SchemaName> getAliases(JsonNode node, String space, String encSpace) {
        JsonNode aliasesNode = node.get("aliases");
        if (aliasesNode == null) {
            return null;
        }

        if (!aliasesNode.isArray()) {
            throw new SchemaParseException("Aliases must be of format JSON array of strings");
        }

        List<SchemaName> aliases = new ArrayList<SchemaName>();
        for (JsonNode aliasNode : aliasesNode) {
            if (!aliasNode.isTextual()) {
                throw new SchemaParseException("Aliases must be of format JSON array of strings");
            }

            aliases.add(new SchemaName(aliasNode.getTextValue(), space, encSpace));
        }
        return aliases;
    }

    protected boolean inAliases(SchemaName name) {
        if (_aliases == null) {
            return false;
        }
        for (SchemaName alias : _aliases) {
            if (name.equals(alias)) {
                return true;
            }
        }
        return false;
    }
}
