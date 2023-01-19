
package org.apache.hive.metastore.listener.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SchemaTypeName {

    Name schemaTypeName;

    public SchemaTypeName(Name schemaTypeName) {
        this.schemaTypeName = schemaTypeName;
    }


    public Name getSchemaTypeName() {
        return schemaTypeName;
    }

    public static class Builder {

        private Name schemaTypeName;

        public Builder setName(Name schemaTypeName) {
            this.schemaTypeName = schemaTypeName;
            return this;
        }

        public SchemaTypeName createSchemaTypeName() {
            return new SchemaTypeName(schemaTypeName);
        }
    }
}