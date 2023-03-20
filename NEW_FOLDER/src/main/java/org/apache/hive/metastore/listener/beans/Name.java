
package org.apache.hive.metastore.listener.beans;

public class Name extends BaseEgeriaType {

    private Long headerVersion;
    private String instancePropertyCategory;
    private String primitiveDefCategory;
    private String primitiveValue;
    private String typeGUID;
    private String typeName;

    public Name(Long headerVersion, String instancePropertyCategory, String primitiveDefCategory, String primitiveValue, String typeGUID, String typeName) {
        this.headerVersion = headerVersion;
        this.instancePropertyCategory = instancePropertyCategory;
        this.primitiveDefCategory = primitiveDefCategory;
        this.primitiveValue = primitiveValue;
        this.typeGUID = typeGUID;
        this.typeName = typeName;
    }

    public Long getHeaderVersion() {
        return headerVersion;
    }

    public String getInstancePropertyCategory() {
        return instancePropertyCategory;
    }

    public String getPrimitiveDefCategory() {
        return primitiveDefCategory;
    }

    public String getPrimitiveValue() {
        return primitiveValue;
    }

    public String getTypeGUID() {
        return typeGUID;
    }

    public String getTypeName() {
        return typeName;
    }

    public static class Builder {

        private Long headerVersion;
        private String instancePropertyCategory;
        private String primitiveDefCategory;
        private String primitiveValue;
        private String typeGUID;
        private String typeName;

        public Builder setHeaderVersion(Long headerVersion) {
            this.headerVersion = headerVersion;
            return this;
        }

        public Builder setInstancePropertyCategory(String instancePropertyCategory) {
            this.instancePropertyCategory = instancePropertyCategory;
            return this;
        }

        public Builder setPrimitiveDefCategory(String primitiveDefCategory) {
            this.primitiveDefCategory = primitiveDefCategory;
            return this;
        }

        public Builder setPrimitiveValue(String primitiveValue) {
            this.primitiveValue = primitiveValue;
            return this;
        }

        public Builder setTypeGUID(String typeGUID) {
            this.typeGUID = typeGUID;
            return this;
        }

        public Builder setTypeName(String typeName) {
            this.typeName = typeName;
            return this;
        }

        public Name createName() {
            return new Name(headerVersion, instancePropertyCategory, primitiveDefCategory, primitiveValue, typeGUID, typeName);
        }
    }
}
