package com.baijioss.serializer.schema;

public abstract class UnnamedSchema extends Schema {

    protected UnnamedSchema(SchemaType type, PropertyMap props) {
        super(type, props);
    }

    @Override
    public String getName() {
        return getType().getName().toLowerCase();
    }
}
