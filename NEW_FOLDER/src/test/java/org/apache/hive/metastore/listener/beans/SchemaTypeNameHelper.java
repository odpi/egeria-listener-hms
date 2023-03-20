package org.apache.hive.metastore.listener.beans;

public class SchemaTypeNameHelper {


    public static SchemaTypeName getSchemaTypeName() {

        SchemaTypeName name = new SchemaTypeName.Builder()
                .setName( NameHelper.getName() )
                .createSchemaTypeName();

        return name;
    }
}
