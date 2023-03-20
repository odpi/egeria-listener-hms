/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.apache.hive.metastore.listener.beans;

public class EgeriaAddTableEvent {

    private Classification Classification;
    private Entity Entity;
    private Object EntityProxy;
    private String ErrorCode;
    private Object ErrorMessage;
    private String EventCategory;
    private String EventDirection;
    private EventOriginator EventOriginator;
    private Long EventTimestamp;
    private Object HomeMetadataCollectionId;
    private Object InstanceBatch;
    private String InstanceEventType;
    private String InstanceGUID;
    private OmrseventV1 OmrseventV1;
    private Object OriginalClassification;
    private Object OriginalEntity;
    private Object OriginalHomeMetadataCollectionId;
    private Object OriginalInstanceGUID;
    private Object OriginalRelationship;
    private Object OriginalTypeDefSummary;
    private Object OtherAttributeTypeDef;
    private Object OtherInstanceGUID;
    private Object OtherMetadataCollectionId;
    private Object OtherOrigin;
    private Object OtherTypeDef;
    private Object OtherTypeDefSummary;
    private Object Relationship;
    private Object TargetAttributeTypeDef;
    private Object TargetInstanceGUID;
    private Object TargetMetadataCollectionId;
    private Object TargetRemoteConnection;
    private Object TargetTypeDefSummary;
    private String TypeDefGUID;
    private String TypeDefName;

    public Classification getClassification() {
        return Classification;
    }

    public Entity getEntity() {
        return Entity;
    }

    public Object getEntityProxy() {
        return EntityProxy;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public Object getErrorMessage() {
        return ErrorMessage;
    }

    public String getEventCategory() {
        return EventCategory;
    }

    public String getEventDirection() {
        return EventDirection;
    }

    public EventOriginator getEventOriginator() {
        return EventOriginator;
    }

    public Long getEventTimestamp() {
        return EventTimestamp;
    }

    public Object getHomeMetadataCollectionId() {
        return HomeMetadataCollectionId;
    }

    public Object getInstanceBatch() {
        return InstanceBatch;
    }

    public String getInstanceEventType() {
        return InstanceEventType;
    }

    public String getInstanceGUID() {
        return InstanceGUID;
    }

    public OmrseventV1 getOmrseventV1() {
        return OmrseventV1;
    }

    public Object getOriginalClassification() {
        return OriginalClassification;
    }

    public Object getOriginalEntity() {
        return OriginalEntity;
    }

    public Object getOriginalHomeMetadataCollectionId() {
        return OriginalHomeMetadataCollectionId;
    }

    public Object getOriginalInstanceGUID() {
        return OriginalInstanceGUID;
    }

    public Object getOriginalRelationship() {
        return OriginalRelationship;
    }

    public Object getOriginalTypeDefSummary() {
        return OriginalTypeDefSummary;
    }

    public Object getOtherAttributeTypeDef() {
        return OtherAttributeTypeDef;
    }

    public Object getOtherInstanceGUID() {
        return OtherInstanceGUID;
    }

    public Object getOtherMetadataCollectionId() {
        return OtherMetadataCollectionId;
    }

    public Object getOtherOrigin() {
        return OtherOrigin;
    }

    public Object getOtherTypeDef() {
        return OtherTypeDef;
    }

    public Object getOtherTypeDefSummary() {
        return OtherTypeDefSummary;
    }

    public Object getRelationship() {
        return Relationship;
    }

    public Object getTargetAttributeTypeDef() {
        return TargetAttributeTypeDef;
    }

    public Object getTargetInstanceGUID() {
        return TargetInstanceGUID;
    }

    public Object getTargetMetadataCollectionId() {
        return TargetMetadataCollectionId;
    }

    public Object getTargetRemoteConnection() {
        return TargetRemoteConnection;
    }

    public Object getTargetTypeDefSummary() {
        return TargetTypeDefSummary;
    }

    public String getTypeDefGUID() {
        return TypeDefGUID;
    }

    public String getTypeDefName() {
        return TypeDefName;
    }

    public static class Builder {

        private Classification Classification;
        private Entity Entity;
        private Object EntityProxy;
        private String ErrorCode;
        private Object ErrorMessage;
        private String EventCategory;
        private String EventDirection;
        private EventOriginator EventOriginator;
        private Long EventTimestamp;
        private Object HomeMetadataCollectionId;
        private Object InstanceBatch;
        private String InstanceEventType;
        private String InstanceGUID;
        private OmrseventV1 OmrseventV1;
        private Object OriginalClassification;
        private Object OriginalEntity;
        private Object OriginalHomeMetadataCollectionId;
        private Object OriginalInstanceGUID;
        private Object OriginalRelationship;
        private Object OriginalTypeDefSummary;
        private Object OtherAttributeTypeDef;
        private Object OtherInstanceGUID;
        private Object OtherMetadataCollectionId;
        private Object OtherOrigin;
        private Object OtherTypeDef;
        private Object OtherTypeDefSummary;
        private Object Relationship;
        private Object TargetAttributeTypeDef;
        private Object TargetInstanceGUID;
        private Object TargetMetadataCollectionId;
        private Object TargetRemoteConnection;
        private Object TargetTypeDefSummary;
        private String TypeDefGUID;
        private String TypeDefName;

        public Builder withClassification(Classification classification) {
            Classification = classification;
            return this;
        }

        public Builder withEntity(Entity entity) {
            Entity = entity;
            return this;
        }

        public Builder withEntityProxy(Object entityProxy) {
            EntityProxy = entityProxy;
            return this;
        }

        public Builder withErrorCode(String errorCode) {
            ErrorCode = errorCode;
            return this;
        }

        public Builder withErrorMessage(Object errorMessage) {
            ErrorMessage = errorMessage;
            return this;
        }

        public Builder withEventCategory(String eventCategory) {
            EventCategory = eventCategory;
            return this;
        }

        public Builder withEventDirection(String eventDirection) {
            EventDirection = eventDirection;
            return this;
        }

        public Builder withEventOriginator(EventOriginator eventOriginator) {
            EventOriginator = eventOriginator;
            return this;
        }

        public Builder withEventTimestamp(Long eventTimestamp) {
            EventTimestamp = eventTimestamp;
            return this;
        }

        public Builder withHomeMetadataCollectionId(Object homeMetadataCollectionId) {
            HomeMetadataCollectionId = homeMetadataCollectionId;
            return this;
        }

        public Builder withInstanceBatch(Object instanceBatch) {
            InstanceBatch = instanceBatch;
            return this;
        }

        public Builder withInstanceEventType(String instanceEventType) {
            InstanceEventType = instanceEventType;
            return this;
        }

        public Builder withInstanceGUID(String instanceGUID) {
            InstanceGUID = instanceGUID;
            return this;
        }

        public Builder withOmrseventV1(OmrseventV1 omrseventV1) {
            OmrseventV1 = omrseventV1;
            return this;
        }

        public Builder withOriginalClassification(Object originalClassification) {
            OriginalClassification = originalClassification;
            return this;
        }

        public Builder withOriginalEntity(Object originalEntity) {
            OriginalEntity = originalEntity;
            return this;
        }

        public Builder withOriginalHomeMetadataCollectionId(Object originalHomeMetadataCollectionId) {
            OriginalHomeMetadataCollectionId = originalHomeMetadataCollectionId;
            return this;
        }

        public Builder withOriginalInstanceGUID(Object originalInstanceGUID) {
            OriginalInstanceGUID = originalInstanceGUID;
            return this;
        }

        public Builder withOriginalRelationship(Object originalRelationship) {
            OriginalRelationship = originalRelationship;
            return this;
        }

        public Builder withOriginalTypeDefSummary(Object originalTypeDefSummary) {
            OriginalTypeDefSummary = originalTypeDefSummary;
            return this;
        }

        public Builder withOtherAttributeTypeDef(Object otherAttributeTypeDef) {
            OtherAttributeTypeDef = otherAttributeTypeDef;
            return this;
        }

        public Builder withOtherInstanceGUID(Object otherInstanceGUID) {
            OtherInstanceGUID = otherInstanceGUID;
            return this;
        }

        public Builder withOtherMetadataCollectionId(Object otherMetadataCollectionId) {
            OtherMetadataCollectionId = otherMetadataCollectionId;
            return this;
        }

        public Builder withOtherOrigin(Object otherOrigin) {
            OtherOrigin = otherOrigin;
            return this;
        }

        public Builder withOtherTypeDef(Object otherTypeDef) {
            OtherTypeDef = otherTypeDef;
            return this;
        }

        public Builder withOtherTypeDefSummary(Object otherTypeDefSummary) {
            OtherTypeDefSummary = otherTypeDefSummary;
            return this;
        }

        public Builder withRelationship(Object relationship) {
            Relationship = relationship;
            return this;
        }

        public Builder withTargetAttributeTypeDef(Object targetAttributeTypeDef) {
            TargetAttributeTypeDef = targetAttributeTypeDef;
            return this;
        }

        public Builder withTargetInstanceGUID(Object targetInstanceGUID) {
            TargetInstanceGUID = targetInstanceGUID;
            return this;
        }

        public Builder withTargetMetadataCollectionId(Object targetMetadataCollectionId) {
            TargetMetadataCollectionId = targetMetadataCollectionId;
            return this;
        }

        public Builder withTargetRemoteConnection(Object targetRemoteConnection) {
            TargetRemoteConnection = targetRemoteConnection;
            return this;
        }

        public Builder withTargetTypeDefSummary(Object targetTypeDefSummary) {
            TargetTypeDefSummary = targetTypeDefSummary;
            return this;
        }

        public Builder withTypeDefGUID(String typeDefGUID) {
            TypeDefGUID = typeDefGUID;
            return this;
        }

        public Builder withTypeDefName(String typeDefName) {
            TypeDefName = typeDefName;
            return this;
        }

        public EgeriaAddTableEvent build() {
            EgeriaAddTableEvent egeriaAddTableEvent = new EgeriaAddTableEvent();
            egeriaAddTableEvent.Classification = Classification;
            egeriaAddTableEvent.Entity = Entity;
            egeriaAddTableEvent.EntityProxy = EntityProxy;
            egeriaAddTableEvent.ErrorCode = ErrorCode;
            egeriaAddTableEvent.ErrorMessage = ErrorMessage;
            egeriaAddTableEvent.EventCategory = EventCategory;
            egeriaAddTableEvent.EventDirection = EventDirection;
            egeriaAddTableEvent.EventOriginator = EventOriginator;
            egeriaAddTableEvent.EventTimestamp = EventTimestamp;
            egeriaAddTableEvent.HomeMetadataCollectionId = HomeMetadataCollectionId;
            egeriaAddTableEvent.InstanceBatch = InstanceBatch;
            egeriaAddTableEvent.InstanceEventType = InstanceEventType;
            egeriaAddTableEvent.InstanceGUID = InstanceGUID;
            egeriaAddTableEvent.OmrseventV1 = OmrseventV1;
            egeriaAddTableEvent.OriginalClassification = OriginalClassification;
            egeriaAddTableEvent.OriginalEntity = OriginalEntity;
            egeriaAddTableEvent.OriginalHomeMetadataCollectionId = OriginalHomeMetadataCollectionId;
            egeriaAddTableEvent.OriginalInstanceGUID = OriginalInstanceGUID;
            egeriaAddTableEvent.OriginalRelationship = OriginalRelationship;
            egeriaAddTableEvent.OriginalTypeDefSummary = OriginalTypeDefSummary;
            egeriaAddTableEvent.OtherAttributeTypeDef = OtherAttributeTypeDef;
            egeriaAddTableEvent.OtherInstanceGUID = OtherInstanceGUID;
            egeriaAddTableEvent.OtherMetadataCollectionId = OtherMetadataCollectionId;
            egeriaAddTableEvent.OtherOrigin = OtherOrigin;
            egeriaAddTableEvent.OtherTypeDef = OtherTypeDef;
            egeriaAddTableEvent.OtherTypeDefSummary = OtherTypeDefSummary;
            egeriaAddTableEvent.Relationship = Relationship;
            egeriaAddTableEvent.TargetAttributeTypeDef = TargetAttributeTypeDef;
            egeriaAddTableEvent.TargetInstanceGUID = TargetInstanceGUID;
            egeriaAddTableEvent.TargetMetadataCollectionId = TargetMetadataCollectionId;
            egeriaAddTableEvent.TargetRemoteConnection = TargetRemoteConnection;
            egeriaAddTableEvent.TargetTypeDefSummary = TargetTypeDefSummary;
            egeriaAddTableEvent.TypeDefGUID = TypeDefGUID;
            egeriaAddTableEvent.TypeDefName = TypeDefName;
            return egeriaAddTableEvent;
        }

    }

}
