/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.apache.hive.metastore.listener.beans;

public class Originator {

    private String metadataCollectionId;
    private String serverName;
    private String serverType;

    public Originator(String metadataCollectionId, String serverName, String serverType) {
        this.metadataCollectionId = metadataCollectionId;
        this.serverName = serverName;
        this.serverType = serverType;
    }

    public String getMetadataCollectionId() {
        return metadataCollectionId;
    }

    public String getServerName() {
        return serverName;
    }

    public String getServerType() {
        return serverType;
    }

    public static class Builder {

        private String metadataCollectionId;
        private String serverName;
        private String serverType;

        public Builder setMetadataCollectionId(String metadataCollectionId) {
            this.metadataCollectionId = metadataCollectionId;
            return this;
        }

        public Builder setServerName(String serverName) {
            this.serverName = serverName;
            return this;
        }

        public Builder setServerType(String serverType) {
            this.serverType = serverType;
            return this;
        }

        public Originator createOriginator() {
            return new Originator(metadataCollectionId, serverName, serverType);
        }
    }
}
