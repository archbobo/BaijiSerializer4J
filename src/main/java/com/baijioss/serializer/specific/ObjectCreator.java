package com.baijioss.serializer.specific;

import com.baijioss.serializer.exception.BaijiRuntimeException;
import com.baijioss.serializer.schema.NamedSchema;
import com.baijioss.serializer.schema.Schema;
import com.baijioss.serializer.schema.SchemaType;
import com.baijioss.serializer.schema.UnionSchema;
import com.baijioss.serializer.util.ClassUtils;

import java.util.ArrayList;
import java.util.HashMap;

public final class ObjectCreator {

    public static final ObjectCreator INSTANCE = new ObjectCreator();

    private ObjectCreator() {
    }

    /**
     * Find the class with the given name
     *
     * @param name       the object type to locate
     * @param throwError whether or not to throw an error if the type wasn't found
     * @return the object class, or #null if not found.
     */
    private Class<?> findClass(String name, boolean throwError) {
        // Modify provided type to ensure it can be discovered.
        // This is mainly for Generics
       /* name = name.replace("List", "java.util.ArrayList");
        name = name.replace("Map", "java.util.HashMap");
        name = name.replaceAll("<.+>", "");*/

        Class<?> clazz = null;
        try {
            clazz = ClassUtils.forName(name);
        } catch (ClassNotFoundException e) {
        }

        if (clazz == null && throwError) {
            throw new BaijiRuntimeException("Unable to find class " + name);
        }

        return clazz;
    }

    /**
     * Gets the class for the specified schema
     *
     * @param schema
     * @return
     */
    public Class<?> getClass(Schema schema) {
        switch (schema.getType()) {
            case NULL:
                return null;
            case BOOLEAN:
                return Boolean.class;
            case INT:
                return Integer.class;
            case LONG:
                return Long.class;
            case FLOAT:
                return Float.class;
            case DOUBLE:
                return Double.class;
            case BYTES:
                return byte[].class;
            case STRING:
                return String.class;
            case ARRAY:
                return ArrayList.class;
            case MAP:
                return HashMap.class;
            case ENUM:
            case RECORD: {
                // Should all be named types
                if (schema instanceof NamedSchema) {
                    NamedSchema named = (NamedSchema) schema;
                    return findClass(named.getFullName(), true);
                }
                break;
            }
            case UNION: {
                if (!(schema instanceof UnionSchema)) {
                    break;
                }
                UnionSchema unSchema = (UnionSchema) schema;
                if (unSchema.size() == 2) {
                    Schema s1 = unSchema.get(0);
                    Schema s2 = unSchema.get(1);

                    // Nullable ?
                    Class<?> itemType = null;
                    if (s1.getType() == SchemaType.NULL) {
                        itemType = getClass(s2);
                    } else if (s2.getType() == SchemaType.NULL) {
                        itemType = getClass(s1);
                    }

                    if (itemType != null) {
                        return itemType;
                    }
                }
                return Object.class;
            }
        }

        // Fallback
        return findClass(schema.getName(), true);
    }
}
