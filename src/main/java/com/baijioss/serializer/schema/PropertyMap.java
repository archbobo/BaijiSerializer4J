package com.baijioss.serializer.schema;

import com.baijioss.serializer.exception.BaijiRuntimeException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import java.io.IOException;
import java.util.*;

/**
 * Base class for objects that have Json-valued properties.
 */
public class PropertyMap extends HashMap<String, String> {

    private static final Set<String> reserved;

    static {
        reserved = new HashSet<String>();
        reserved.add("type");
        reserved.add("name");
        reserved.add("namespace");
        reserved.add("fields");
        reserved.add("items");
        reserved.add("size");
        reserved.add("symbols");
        reserved.add("values");
        reserved.add("aliases");
        reserved.add("order");
        reserved.add("doc");
        reserved.add("default");
    }

    public void parse(JsonNode node) {
        if (!(node instanceof ObjectNode)) {
            return;
        }

        ObjectNode objNode = (ObjectNode) node;
        Iterator<Map.Entry<String, JsonNode>> fields = objNode.getFields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            if (reserved.contains(entry.getKey())) {
                continue;
            }
            String key = entry.getKey();
            JsonNode value = entry.getValue();
            if (!containsKey(key)) {
                put(key, value.isTextual() ? value.getTextValue() : value.asText());
            }
        }
    }

    @Override
    public synchronized String put(String key, String value) {
        if (reserved.contains(key))
        {
            throw new BaijiRuntimeException("Can't set reserved property: " + key);
        }

        String oldValue = get(key);
        if (oldValue == null)
        {
           return super.put(key, value);
        }
        else if (!oldValue.equals(value))
        {
            throw new BaijiRuntimeException("Property cannot be overwritten: " + key);
        }
        return value;
    }

    void writeJson(JsonGenerator gen) throws IOException {
        for (Map.Entry<String, String> e : entrySet())
            gen.writeObjectField(e.getKey(), e.getValue());
    }
}
