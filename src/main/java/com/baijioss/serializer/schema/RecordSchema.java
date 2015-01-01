package com.baijioss.serializer.schema;

import com.baijioss.serializer.util.ObjectUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;

import java.io.IOException;
import java.util.*;

public class RecordSchema extends NamedSchema implements Iterable<Field> {

    private final List<Field> _fields;

    private final Map<String, Field> _fieldLookup;

    private final Map<String, Field> _fieldAliasLookup;

    private final boolean _request;

    private static final ThreadLocal<Set> SEEN = new ThreadLocal<Set>() {
        protected Set initialValue() {
            return new HashSet();
        }
    };

    /**
     * Constructor for the record schema
     *
     * @param name    name of the record schema
     * @param doc
     * @param aliases list of aliases for the record name
     * @param props
     * @param fields  list of fields for the record
     * @param request true if this is an anonymous record with 'request' instead of 'fields'
     */
    public RecordSchema(SchemaName name, String doc, List<SchemaName> aliases, PropertyMap props,
                        List<Field> fields, boolean request) {
        super(SchemaType.RECORD, name, doc, aliases, props, new SchemaNames());
        if (!request && name.getName() == null) {
            throw new SchemaParseException("name cannot be null for record schema.");
        }
        _fields = fields;
        _request = request;
        Map<String, Field> fieldMap = new HashMap<String, Field>();
        Map<String, Field> fieldAliasMap = new HashMap<String, Field>();
        for (Field field : fields) {
            addToFieldMap(fieldMap, field.getName(), field);
            addToFieldMap(fieldAliasMap, field.getName(), field);

            if (field.getAliases() != null)
            // add aliases to field lookup map so reader function will find it when writer field name appears only as an alias on the reader field
            {
                for (String alias : field.getAliases()) {
                    addToFieldMap(fieldAliasMap, alias, field);
                }
            }
        }
        _fieldLookup = fieldMap;
        _fieldAliasLookup = fieldAliasMap;
    }

    /**
     * Constructor for the record schema
     *
     * @param name          name of the record schema
     * @param doc
     * @param aliases       list of aliases for the record name
     * @param props
     * @param fields        list of fields for the record
     * @param request       true if this is an anonymous record with 'request' instead of 'fields'
     * @param fieldMap      map of field names and field objects
     * @param fieldAliasMap map of field aliases and field objects
     * @param names         list of named schema already read
     */
    private RecordSchema(SchemaName name, String doc, List<SchemaName> aliases, PropertyMap props,
                         List<Field> fields, boolean request, Map<String, Field> fieldMap,
                         Map<String, Field> fieldAliasMap, SchemaNames names) {
        super(SchemaType.RECORD, name, doc, aliases, props, names);
        if (!request && name.getName() == null) {
            throw new SchemaParseException("name cannot be null for record schema.");
        }
        _fields = fields;
        _request = request;
        _fieldLookup = fieldMap;
        _fieldAliasLookup = fieldAliasMap;
    }

    /**
     * Static function to return new instance of the record schema
     *
     * @param jtok     JSON object for the record schema
     * @param props
     * @param names    list of named schema already read
     * @param encspace enclosing namespace of the records schema
     * @return
     */
    static RecordSchema newInstance(JsonNode jtok, PropertyMap props, SchemaNames names,
                                    String encspace) {
        boolean request = false;
        JsonNode fieldsNode = jtok.get("fields"); // normal record
        if (fieldsNode == null) {
            fieldsNode = jtok.get("request"); // anonymous record from messages
            if (fieldsNode != null) {
                request = true;
            }
        }
        if (fieldsNode == null) {
            throw new SchemaParseException("'fields' cannot be null for record");
        }
        if (!fieldsNode.isArray()) {
            throw new SchemaParseException("'fields' not an array for record");
        }

        SchemaName name = getName(jtok, encspace);
        List<SchemaName> aliases = getAliases(jtok, name.getSpace(), name.getEncSpace());
        String doc = JsonHelper.getOptionalString(jtok, "doc");
        List<Field> fields = new ArrayList<Field>();
        Map<String, Field> fieldMap = new HashMap<String, Field>();
        Map<String, Field> fieldAliasMap = new HashMap<String, Field>();
        RecordSchema result = new RecordSchema(name, doc, aliases, props, fields,
                request, fieldMap, fieldAliasMap, names);

        int fieldPos = 0;
        for (JsonNode fieldNode : fieldsNode) {
            String fieldName = JsonHelper.getRequiredString(fieldNode, "name");
            Field field = createField(fieldNode, fieldPos++, names, name.getNamespace());
            // add record namespace for field look up
            fields.add(field);
            addToFieldMap(fieldMap, fieldName, field);
            addToFieldMap(fieldAliasMap, fieldName, field);

            if (field.getAliases() != null) {
                // add aliases to field lookup map so reader function will find it when writer field name appears only as an alias on the reader field
                for (String alias : field.getAliases()) {
                    addToFieldMap(fieldAliasMap, alias, field);
                }
            }
        }
        return result;
    }

    private static void addToFieldMap(Map<String, Field> map, String name, Field field) {
        if (map.containsKey(name)) {
            throw new SchemaParseException("field or alias " + name + " is a duplicate name");
        }
        map.put(name, field);
    }

    /**
     * Creates a new field for the record
     *
     * @param jfield   JSON object for the field
     * @param pos      position number of the field
     * @param names    list of named schemas already read
     * @param encSpace enclosing namespace of the records schema
     * @return
     */
    private static Field createField(JsonNode jfield, int pos, SchemaNames names, String encSpace) {
        String name = JsonHelper.getRequiredString(jfield, "name");
        String doc = JsonHelper.getOptionalString(jfield, "doc");

        String jorder = JsonHelper.getOptionalString(jfield, "order");
        Field.SortOrder sortOrder = Field.SortOrder.IGNORE;
        if (null != jorder) {
            sortOrder = Field.SortOrder.valueOf(jorder.toUpperCase());
        }

        List<String> aliases = Field.getAliases(jfield);
        PropertyMap props = JsonHelper.getProperties(jfield);
        JsonNode defaultValue = jfield.get("default");

        JsonNode jtype = jfield.get("type");
        if (jtype == null) {
            throw new SchemaParseException("'type' was not found for field: " + name);
        }
        Schema schema = parseJson(jtype, names, encSpace);
        return new Field(schema, name, aliases, pos, doc, defaultValue, sortOrder, props);
    }

    public List<Field> getFields() {
        return _fields;
    }

    public int size() {
        return _fields.size();
    }

    public void addField(Field field) {
        if (_fieldLookup.containsKey(field.getName())) {
            throw new IllegalArgumentException("Duplicate field: " + field.getName());
        }
        _fields.add(field);
        addToFieldMap(_fieldLookup, field.getName(), field);
        addToFieldMap(_fieldAliasLookup, field.getName(), field);
    }

    /**
     * Returns the field with the given name.
     *
     * @param name field name
     * @return Field object
     */
    public Field getField(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name cannot be null");
        }
        return _fieldLookup.get(name);
    }

    /**
     * Returns true if and only if the record contains a field by the given name.
     *
     * @param fieldName The name of the field
     * @return true if the field exists, false otherwise
     */
    public boolean contains(String fieldName) {
        return _fieldLookup.containsKey(fieldName);
    }


    public Field getFieldAlias(String fieldName) {
        return _fieldAliasLookup.get(fieldName);
    }

    /**
     * Writes the records schema in JSON format
     *
     * @param writer   JSON writer
     * @param names    list of named schemas already written
     * @param encSpace enclosing namespace of the record schema
     */
    @Override
    protected void writeJsonFields(JsonGenerator writer, SchemaNames names, String encSpace)
            throws IOException {
        super.writeJsonFields(writer, names, encSpace);

        // we allow reading for empty fields, so writing of records with empty fields are allowed as well
        if (_request) {
            writer.writeFieldName("request");
        } else {
            writer.writeFieldName("fields");
        }
        writer.writeStartArray();

        if (_fields != null && !_fields.isEmpty()) {
            for (Field field : this) {
                field.writeJson(writer, names, getNamespace()); // use the namespace of the record for the fields
            }
        }
        writer.writeEndArray();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RecordSchema)) {
            return false;
        }

        RecordSchema that = (RecordSchema) obj;
        Set seen = SEEN.get();
        RecordSchemaPair pair = new RecordSchemaPair(this, that);
        if (seen.contains(pair)) {
            return true;
        }
        seen.add(pair);

        try {
            if (getSchemaName().equals(that.getSchemaName()) && size() == that.size()) {
                for (int i = 0; i < _fields.size(); i++) {
                    if (!_fields.get(i).equals(that._fields.get(i))) {
                        return false;
                    }
                }
                return ObjectUtils.equals(that.getProps(), getProps());
            }
            return false;
        } finally {
            seen.remove(pair);
        }
    }

    @Override
    public int hashCode() {
        Set seen = SEEN.get();
        RecordSchemaPair pair = new RecordSchemaPair(this, this);
        if (seen.contains(pair)) {
            return 0;
        }
        seen.add(pair);

        try {
            long result = getSchemaName().hashCode();
            for (Field field : this) {
                result += 29L * field.hashCode();
            }
            result += ObjectUtils.hashCode(getProps());
            return (int) result;
        } finally {
            seen.remove(pair);
        }
    }

    @Override
    public Iterator<Field> iterator() {
        return _fields.iterator();
    }

    private static class RecordSchemaPair {
        public final RecordSchema _first;
        public final RecordSchema _second;

        public RecordSchemaPair(RecordSchema first, RecordSchema second) {
            _first = first;
            _second = second;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof RecordSchemaPair)) {
                return false;
            }
            RecordSchemaPair that = (RecordSchemaPair) obj;
            return that._first == _first && that._second == _second;
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(_first) + System.identityHashCode(_second);
        }
    }
}
