/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.apache.hive.metastore.listener.beans;


public class TypeDefSuperType {

    private String guid;
    private Long headerVersion;
    private String name;
    private String status;

    public TypeDefSuperType(String guid, Long headerVersion, String name, String status) {
        this.guid = guid;
        this.headerVersion = headerVersion;
        this.name = name;
        this.status = status;
    }

    public String getGuid() {
        return guid;
    }

    public Long getHeaderVersion() {
        return headerVersion;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }


    public static class Builder {

        private String guid;
        private Long headerVersion;
        private String name;
        private String status;

        public Builder setGuid(String guid) {
            this.guid = guid;
            return this;
        }

        public Builder setHeaderVersion(Long headerVersion) {
            this.headerVersion = headerVersion;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public TypeDefSuperType createTypeDefSuperType() {
            return new TypeDefSuperType(guid, headerVersion, name, status);
        }
    }
}
