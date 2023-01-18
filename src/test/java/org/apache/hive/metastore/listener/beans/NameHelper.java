package org.apache.hive.metastore.listener.beans;

 public class NameHelper {

    public static Name getName() {

        Name name = new Name.Builder()
                .setTypeName("string")
                .setHeaderVersion(0L)
                .setPrimitiveValue("RelationalTableType")
                .setTypeGUID("b34a64b9-554a-42b1-8f8a-7d5c2339f9c4")
                .setInstancePropertyCategory("PRIMITIVE")
                .setPrimitiveDefCategory("OM_PRIMITIVE_TYPE_STRING")
                .createName();

        return name;

    }
}
