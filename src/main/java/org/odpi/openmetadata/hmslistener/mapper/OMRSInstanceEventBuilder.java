/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.hmslistener.mapper;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;

/**
 * These methods are duplicates from org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventBuilder
 * in core Egeria - apart from they return the OMRSInstanceEvent rather than issuing the event.
 */

public class OMRSInstanceEventBuilder {

    private OMRSEventOriginator eventOriginator= null;

    public  OMRSInstanceEventBuilder( OMRSEventOriginator eventOriginator) {
        this.eventOriginator = eventOriginator;
    }

    public OMRSInstanceEvent buildNewEntityEvent(EntityDetail entity)
    {
        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.NEW_ENTITY_EVENT,
                entity);
        instanceEvent.setEventOriginator(eventOriginator);
        instanceEvent.setHomeMetadataCollectionId(entity.getMetadataCollectionId());

        return instanceEvent;
    }
    public OMRSInstanceEvent buildUpdatedEntityEvent(
            EntityDetail oldEntity,
            EntityDetail newEntity)
    {
        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.UPDATED_ENTITY_EVENT,
                oldEntity,
                newEntity);
        instanceEvent.setEventOriginator(eventOriginator);

        return instanceEvent;
    }
    public  OMRSInstanceEvent buildDeletedEntityEvent(
            EntityDetail entity)
    {
        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.DELETED_ENTITY_EVENT,
                entity);

        instanceEvent.setEventOriginator(eventOriginator);
        instanceEvent.setHomeMetadataCollectionId(entity.getMetadataCollectionId());


        return instanceEvent;
    }
    public OMRSInstanceEvent  buildNewRelationshipEvent(
            Relationship relationship)
    {
        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.NEW_RELATIONSHIP_EVENT,
                relationship);

        instanceEvent.setEventOriginator(eventOriginator);
        instanceEvent.setHomeMetadataCollectionId(relationship.getMetadataCollectionId());

        return instanceEvent;
    }

//    public  OMRSInstanceEvent buildUpdatedRelationshipEvent(
//            Relationship oldRelationship,
//            Relationship newRelationship)
//    {
//        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.UPDATED_RELATIONSHIP_EVENT,
//                oldRelationship,
//                newRelationship);
//
//        instanceEvent.setEventOriginator(eventOriginator);
//        return instanceEvent;
//    }

//    public OMRSInstanceEvent buildDeletedRelationshipEvent(
//            Relationship relationship)
//    {
//        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.DELETED_RELATIONSHIP_EVENT,
//                relationship);
//
//        instanceEvent.setEventOriginator(eventOriginator);
//        return instanceEvent;
//    }






}
