package com.baijioss.serializer.schema;

import com.baijioss.serializer.exception.BaijiRuntimeException;
import com.baijioss.serializer.util.ObjectUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import java.io.IOException;
import java.util.*;

public class EnumSchema extends NamedSchema implements Iterable<String> {

    private final List<String> _symbols;

    /**
     * Map of enum symbols and it's corresponding ordinal number
     * The first element of value is the explicit value which can be null, the second one is the actual value.
     */
    private final Map<String, Integer[]> _symbolMap;

    /**
     * Constructor for enum schema
     *
     * @param name    name of enum
     * @param doc
     * @param aliases list of aliases for the name
     * @param symbols list of enum symbols, Map of enum symbols and it's corresponding ordinal number
     *                The first element of value is the explicit value which can be null, the second one is the actual value.
     * @param props
     */
    public EnumSchema(SchemaName name, String doc, List<SchemaName> aliases, Map.Entry<String, Integer>[] symbols,
                      PropertyMap props) {
        super(SchemaType.ENUM, name, doc, aliases, props, new SchemaNames());
        if (null == name.getName()) {
            throw new SchemaParseException("name cannot be null for enum schema.");
        }

        _symbols = new ArrayList<String>();
        Map<String, Integer[]> symbolMap = new HashMap<String, Integer[]>();
        int lastValue = -1;
        for (Map.Entry<String, Integer> symbol : symbols) {
            Integer[] values = new Integer[2];
            if (symbol.getValue() != null) {
                values[0] = values[1] = lastValue = symbol.getValue();
            } else {
                values[1] = ++lastValue;
            }
            _symbols.add(symbol.getKey());
            symbolMap.put(symbol.getKey(), values);
        }
        _symbolMap = symbolMap;
    }

    /**
     * Constructor for enum schema
     *
     * @param name      name of enum
     * @param doc
     * @param aliases   list of aliases for the name
     * @param symbols   list of enum symbols
     * @param symbolMap map of enum symbols and value
     *                  The first element of value is the explicit value which can be null, the second one is the actual value.
     * @param props
     * @param names     list of named schema already read
     */
    private EnumSchema(SchemaName name, String doc, List<SchemaName> aliases, List<String> symbols,
                       Map<String, Integer[]> symbolMap, PropertyMap props, SchemaNames names) {
        super(SchemaType.ENUM, name, doc, aliases, props, names);
        if (null == name.getName()) {
            throw new SchemaParseException("name cannot be null for enum schema.");
        }
        _symbols = symbols;
        _symbolMap = symbolMap;
    }

    /**
     * Static function to return new instance of EnumSchema
     *
     * @param token    JSON object for enum schema
     * @param props
     * @param names    list of named schema already parsed in
     * @param encSpace enclosing namespace for the enum schema
     * @return
     */
    static EnumSchema newInstance(JsonNode token, PropertyMap props, SchemaNames names, String encSpace) {
        SchemaName name = getName(token, encSpace);
        List<SchemaName> aliases = getAliases(token, name.getSpace(), name.getEncSpace());
        String doc = JsonHelper.getOptionalString(token, "doc");

        JsonNode jsymbols = token.get("symbols");
        if (jsymbols == null) {
            throw new SchemaParseException("Enum has no symbols: " + name);
        }
        if (!jsymbols.isArray()) {
            throw new SchemaParseException("symbols field in enum must be an array.");
        }

        List<String> symbols = new ArrayList<String>();
        Map<String, Integer[]> symbolMap = new HashMap<String, Integer[]>();
        int lastValue = -1;
        for (JsonNode jsymbol : jsymbols) {
            Integer explicitValue = null;
            Integer actualValue;
            String symbol;
            if (jsymbol.isTextual()) {
                symbol = jsymbol.getTextValue();
                actualValue = ++lastValue;
            } else if (jsymbol.isObject()) {
                ObjectNode symbolObj = (ObjectNode) jsymbol;
                JsonNode symbolNode = symbolObj.get("name");
                if (symbolNode == null) {
                    throw new SchemaParseException("Missing symbol name: " + jsymbol);
                }
                if (!symbolNode.isTextual()) {
                    throw new SchemaParseException("Symbol name must be a string: " + jsymbol);
                }
                symbol = symbolNode.getTextValue();
                JsonNode valueNode = symbolObj.get("value");
                if (valueNode != null) {
                    if (!valueNode.isInt()) {
                        throw new SchemaParseException("Only integer value is allowed for an enum symbol: " + jsymbol);
                    }
                    explicitValue = valueNode.getIntValue();
                }
                lastValue = actualValue = explicitValue != null ? explicitValue : lastValue + 1;
            } else {
                throw new SchemaParseException("Invalid symbol object: " + jsymbol);
            }

            if (symbolMap.containsKey(symbol)) {
                throw new SchemaParseException("Duplicate symbol: " + symbol);
            }

            symbolMap.put(symbol, new Integer[]{explicitValue, actualValue});
            symbols.add(symbol);
        }
        return new EnumSchema(name, doc, aliases, symbols, symbolMap, props, names);
    }

    /**
     * Get count of enum symbols
     *
     * @return
     */
    public int size() {
        return _symbols.size();
    }

    public List<String> getSymbols() {
        return _symbols;
    }

    /**
     * Returns the position of the given symbol within this enum.
     * Throws BaijiException if the symbol is not found in this enum.
     *
     * @param symbol name of the symbol to find
     * @return position of the given symbol in this enum schema
     */
    public int ordinal(String symbol) {
        Integer[] value = _symbolMap.get(symbol);
        if (value != null) {
            return value[1].intValue();
        }
        throw new BaijiRuntimeException("No such symbol: " + symbol);
    }

    /**
     * Returns the enum symbol of the given index to the list
     *
     * @param value symbol value
     * @return The symbol name corresponding to the given value.
     * If there are multiple symbols associated with the given value, the first one in the list will be returned.
     */
    public String get(int value) {
        for (Map.Entry<String, Integer[]> entry : _symbolMap.entrySet()) {
            if (entry.getValue()[1] == value) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Checks if given symbol is in the list of enum symbols
     *
     * @param symbol symbol to check
     * @return true if symbol exist, false otherwise
     */
    public boolean contains(String symbol) {
        return _symbolMap.containsKey(symbol);
    }

    /**
     * Default implementation for writing schema properties in JSON format
     *
     * @param gen      JSON generator
     * @param names    list of named schemas already written
     * @param encSpace enclosing namespace of the schema
     */
    protected void writeJsonFields(JsonGenerator gen, SchemaNames names, String encSpace) throws IOException {
        super.writeJsonFields(gen, names, encSpace);
        gen.writeFieldName("symbols");
        gen.writeStartArray();
        for (String s : _symbols) {
            gen.writeString(s);
        }
        gen.writeEndArray();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof EnumSchema)) {
            return false;
        }
        EnumSchema that = (EnumSchema) obj;
        if (getSchemaName().equals(that.getSchemaName()) && _symbols.size() == that._symbols.size()) {
            for (int i = 0; i < _symbols.size(); ++i) {
                if (!_symbols.get(i).equals(that._symbols.get(i))) {
                    return false;
                }
            }
            return ObjectUtils.equals(that.getProps(), getProps());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int value = (int) (getSchemaName().hashCode() + ObjectUtils.hashCode(getProps()));
        for (String symbol : _symbols) {
            value += 23 * symbol.hashCode();
        }
        return value;
    }

    @Override
    public Iterator<String> iterator() {
        return _symbols.iterator();
    }
}
