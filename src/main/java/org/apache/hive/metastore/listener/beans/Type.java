/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.apache.hive.metastore.listener.beans;

import java.util.List;

public class Type extends BaseEgeriaType {

    private final Long headerVersion;
    private final String typeDefCategory;
    private final String typeDefDescription;
    private final String typeDefGUID;
    private final String typeDefName;
    private final List<TypeDefSuperType> typeDefSuperTypes;
    private final Long typeDefVersion;
    private final List<String> validInstanceProperties;
    private final List<String> validStatusList;

    public Type(Long headerVersion, String typeDefCategory, String typeDefDescription, String typeDefGUID, String typeDefName, List<TypeDefSuperType> typeDefSuperTypes, Long typeDefVersion, List<String> validInstanceProperties, List<String> validStatusList) {
        this.headerVersion = headerVersion;
        this.typeDefCategory = typeDefCategory;
        this.typeDefDescription = typeDefDescription;
        this.typeDefGUID = typeDefGUID;
        this.typeDefName = typeDefName;
        this.typeDefSuperTypes = typeDefSuperTypes;
        this.typeDefVersion = typeDefVersion;
        this.validInstanceProperties = validInstanceProperties;
        this.validStatusList = validStatusList;
    }

    public Long getHeaderVersion() {
        return headerVersion;
    }

    public String getTypeDefCategory() {
        return typeDefCategory;
    }

    public String getTypeDefDescription() {
        return typeDefDescription;
    }

    public String getTypeDefGUID() {
        return typeDefGUID;
    }

    public String getTypeDefName() {
        return typeDefName;
    }

    public List<TypeDefSuperType> getTypeDefSuperTypes() {
        return typeDefSuperTypes;
    }

    public Long getTypeDefVersion() {
        return typeDefVersion;
    }

    public List<String> getValidInstanceProperties() {
        return validInstanceProperties;
    }

    public List<String> getValidStatusList() {
        return validStatusList;
    }

    public class Builder {

        private Long headerVersion;
        private String typeDefCategory;
        private String typeDefDescription;
        private String typeDefGUID;
        private String typeDefName;
        private List<TypeDefSuperType> typeDefSuperTypes;
        private Long typeDefVersion;
        private List<String> validInstanceProperties;
        private List<String> validStatusList;

        public Builder setHeaderVersion(Long headerVersion) {
            this.headerVersion = headerVersion;
            return this;
        }

        public Builder setTypeDefCategory(String typeDefCategory) {
            this.typeDefCategory = typeDefCategory;
            return this;
        }

        public Builder setTypeDefDescription(String typeDefDescription) {
            this.typeDefDescription = typeDefDescription;
            return this;
        }

        public Builder setTypeDefGUID(String typeDefGUID) {
            this.typeDefGUID = typeDefGUID;
            return this;
        }

        public Builder setTypeDefName(String typeDefName) {
            this.typeDefName = typeDefName;
            return this;
        }

        public Builder setTypeDefSuperTypes(List<TypeDefSuperType> typeDefSuperTypes) {
            this.typeDefSuperTypes = typeDefSuperTypes;
            return this;
        }

        public Builder setTypeDefVersion(Long typeDefVersion) {
            this.typeDefVersion = typeDefVersion;
            return this;
        }

        public Builder setValidInstanceProperties(List<String> validInstanceProperties) {
            this.validInstanceProperties = validInstanceProperties;
            return this;
        }

        public Builder setValidStatusList(List<String> validStatusList) {
            this.validStatusList = validStatusList;
            return this;
        }

        public Type createType() {
            return new Type(headerVersion, typeDefCategory, typeDefDescription, typeDefGUID, typeDefName, typeDefSuperTypes, typeDefVersion, validInstanceProperties, validStatusList);
        }
    }

}

