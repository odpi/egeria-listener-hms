
package org.apache.hive.metastore.listener.beans;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Classification extends BaseEgeriaType {

    private Long createTime;
    private String createdBy;
    private Long headerVersion = 1L;
    private String instanceProvenanceType;
    private String name;
    private Properties properties;
    private String status;
    private Type type;
    private Long version;


    public Long getCreateTime() {
        return createTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Long getHeaderVersion() {
        return headerVersion;
    }

    public String getInstanceProvenanceType() {
        return instanceProvenanceType;
    }

    public String getName() {
        return name;
    }

    public Properties getProperties() {
        return properties;
    }


    public String getStatus() {
        return status;
    }


    public Type getType() {
        return type;
    }

    public Long getVersion() {
        return version;
    }

    public Classification(Long createTime, String createdBy, Long headerVersion, String instanceProvenanceType, String name, Properties properties, String status, Type type, Long version) {
        this.createTime = createTime;
        this.createdBy = createdBy;
        this.headerVersion = headerVersion;
        this.instanceProvenanceType = instanceProvenanceType;
        this.name = name;
        this.properties = properties;
        this.status = status;
        this.type = type;
        this.version = version;
    }

    public static class Builder {

        private Long createTime;
        private String createdBy;
        private Long headerVersion;
        private String instanceProvenanceType;
        private String name;
        private Properties properties;
        private String status;
        private Type type;
        private Long version;

        public Builder setCreateTime(Long createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder setHeaderVersion(Long headerVersion) {
            this.headerVersion = headerVersion;
            return this;
        }

        public Builder setInstanceProvenanceType(String instanceProvenanceType) {
            this.instanceProvenanceType = instanceProvenanceType;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setProperties(Properties properties) {
            this.properties = properties;
            return this;
        }

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder setType(Type type) {
            this.type = type;
            return this;
        }

        public Builder setVersion(Long version) {
            this.version = version;
            return this;
        }

        public Classification createClassification() {
            return new Classification(createTime, createdBy, headerVersion, instanceProvenanceType, name, properties, status, type, version);
        }
    }

}