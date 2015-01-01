package com.baijioss.serializer.schema;

import org.codehaus.jackson.JsonGenerator;

import java.io.IOException;

public class SchemaName {

    private final String name;
    private final String space;
    private final String encSpace;
    private final String fullname;

    public SchemaName(String name, String space, String encSpace) {
        if (name == null) {                         // anonymous
            this.name = this.space = null;
            this.encSpace = encSpace;
            this.fullname = null;
            return;
        }
        int lastDot = name.lastIndexOf('.');
        if (lastDot < 0) {                          // unqualified name
            this.name = name;
            this.space = space;
            this.encSpace = encSpace;
        } else {                                    // qualified name
            this.space = name.substring(0, lastDot);
            this.name = name.substring(lastDot + 1, name.length());
            this.encSpace = encSpace;
        }
        String namespace = getNamespace();
        fullname = namespace != null && namespace.length() != 0 ? namespace + "." + name : name;
    }

    public String getName() {
        return name;
    }

    public String getSpace() {
        return space;
    }

    public String getEncSpace() {
        return encSpace;
    }

    public String getNamespace() {
        return space != null && space.length() != 0 ? space : encSpace;
    }

    public String getFullName() {
        return fullname;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SchemaName)) return false;
        SchemaName that = (SchemaName) o;
        return fullname == null ? that.fullname == null : fullname.equals(that.fullname);
    }

    public int hashCode() {
        return fullname == null ? 0 : fullname.hashCode();
    }

    public String toString() {
        return fullname;
    }

    void writeJson(JsonGenerator gen, SchemaNames names) throws IOException {
        JsonHelper.writeIfNotNullOrEmpty(gen, "name", name);
        JsonHelper.writeIfNotNullOrEmpty(gen, "namespace", getNamespace());
    }
}
