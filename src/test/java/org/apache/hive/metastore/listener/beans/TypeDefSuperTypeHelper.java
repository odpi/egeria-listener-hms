package org.apache.hive.metastore.listener.beans;

public class TypeDefSuperTypeHelper {

    public static TypeDefSuperType getTypeDefSuperType() {

        TypeDefSuperType type = new TypeDefSuperType.Builder()
                .setGuid("1a5e159b-913a-43b1-95fe-04433b25fca9")
                .setHeaderVersion(1L)
                .setName("SchemaAttribute")
                .setStatus("ACTIVE_TYPEDEF")
                .createTypeDefSuperType();

        return type;
    }
}
