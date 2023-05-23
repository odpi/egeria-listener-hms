/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.hmslistener.mapper;


import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.Table;
import org.odpi.openmetadata.hmslistener.SupportedTypes;
import org.odpi.openmetadata.hmslistener.helpers.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * These methods are duplicates from org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventBuilder
 * in core Egeria - apart from they return the OMRSInstanceEvent rather than issuing the event.
 */
@SuppressWarnings("JavaUtilDate")
public class OMRSInstanceEventBuilder {

    private OMRSEventOriginator eventOriginator = null;

    private OMRSRepositoryHelper repositoryHelper = null;

    public OMRSInstanceEventBuilder(OMRSEventOriginator eventOriginator) {
        this.eventOriginator = eventOriginator;

        repositoryHelper = new OMRSRepositoryHelper(eventOriginator.getMetadataCollectionId(), eventOriginator.getServerName());
    }

    /**
     * build new Instance events for a new table
     * @param hmsTable hmsTable to map into Egeria events
     * @param qualifiedNameAboveTable the qualifiedName above the table
     * @return list of Egeria events that represent the creation of the table
     */
    public List<OMRSInstanceEvent> buildNewTableEvents(Table hmsTable, String qualifiedNameAboveTable) {

        List<OMRSInstanceEvent> instanceEvents = new ArrayList<>();

        String tableQualifiedName = qualifiedNameAboveTable +  SupportedTypes.SEPARATOR_CHAR + hmsTable.getTableName();

        EntityDetail newTableEntity = mapHMSTableToEntity(hmsTable, tableQualifiedName);
        Date createTime = newTableEntity.getCreateTime();
        String tableGUID = newTableEntity.getGUID();
        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.NEW_ENTITY_EVENT,
                newTableEntity);
        instanceEvent.setEventOriginator(eventOriginator);
        instanceEvent.setHomeMetadataCollectionId(newTableEntity.getMetadataCollectionId());
        instanceEvents.add(instanceEvent);
        // add relationship
        String relationalDBTypeGuid = null;
        try {
            relationalDBTypeGuid = Base64.getUrlEncoder().encodeToString(qualifiedNameAboveTable.getBytes("UTF-8"));
            Relationship newRelationship = repositoryHelper.createReferenceRelationship(
                    SupportedTypes.ATTRIBUTE_FOR_SCHEMA,
                    relationalDBTypeGuid,
                    SupportedTypes.RELATIONAL_DB_SCHEMA_TYPE,
                    tableGUID,
                    SupportedTypes.TABLE);
            newRelationship.setCreateTime(createTime);
            instanceEvents.add(buildNewRelationshipEvent(newRelationship));

        } catch (UnsupportedEncodingException e) {
            // TODO deal with error properly
            throw new RuntimeException(e);
        }

        if (hmsTable.getSd() != null) {
            Iterator<FieldSchema> colsIterator = hmsTable.getSd().getColsIterator();
            while (colsIterator.hasNext()) {
                FieldSchema fieldSchema = colsIterator.next();
                EntityDetail newColumnEntity = mapFieldSchemaToEntity(fieldSchema, tableQualifiedName, createTime);

                OMRSInstanceEvent colInstanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.NEW_ENTITY_EVENT,
                        newColumnEntity);
                colInstanceEvent.setEventOriginator(eventOriginator);
                colInstanceEvent.setHomeMetadataCollectionId(newTableEntity.getMetadataCollectionId());
                instanceEvents.add(colInstanceEvent);
                Relationship newRelationship = mapEndGUIDToRelationship(tableGUID, newColumnEntity.getGUID(), createTime);

                newRelationship.setMetadataCollectionId(repositoryHelper.getMetadataCollectionId());
                newRelationship.setMetadataCollectionName(repositoryHelper.getMetadataCollectionName());
                newRelationship.setStatus(InstanceStatus.ACTIVE);
                newRelationship.setCreateTime(createTime);
                instanceEvents.add(buildNewRelationshipEvent(newRelationship));
            }
        }
        return instanceEvents;

    }

    /**
     * map the hms table to an EntityDetail
     * @param hmsTable hms table
     * @param tableQualifiedName table qualified name
     * @return EntityDetail for the RelationalTable
     */
    private EntityDetail mapHMSTableToEntity(Table hmsTable, String tableQualifiedName) {
        String methodName = "mapHMSTableToEntity";
        EntityDetail tableEntity = new EntityDetail();
        tableEntity.setType(SupportedTypes.RELATIONAL_TABLE_INSTANCETYPE);

        String tableName = hmsTable.getTableName();
        String tableGUID = null;
        try {
            tableGUID = Base64.getUrlEncoder().encodeToString(tableQualifiedName.getBytes("UTF-8"));
            tableEntity.setGUID(tableGUID);
        } catch (UnsupportedEncodingException e) {
            // TODO deal with error properly
            throw new RuntimeException(e);
        }
        Date createTime = new Date(hmsTable.getCreateTime()*1000L);
        tableEntity.setCreateTime(createTime);
        InstanceProperties instanceProperties = repositoryHelper.addStringPropertyToInstance("Egeria HMS listener",
                null,
                "qualifiedName",
                tableQualifiedName,
                methodName);
        instanceProperties = repositoryHelper.addStringPropertyToInstance(SupportedTypes.SOURCE_NAME,
                instanceProperties,
                "name",
                tableName,
                methodName);

        tableEntity.setVersion(System.currentTimeMillis());
        tableEntity.setProperties(instanceProperties);
        tableEntity.setMetadataCollectionId(repositoryHelper.getMetadataCollectionId());
        tableEntity.setMetadataCollectionName(repositoryHelper.getMetadataCollectionName());
        tableEntity.setStatus(InstanceStatus.ACTIVE);
        //TypeEmbeddedAttribute
        tableEntity = repositoryHelper.createTypeEmbeddedClassification(methodName, SupportedTypes.RELATIONAL_TABLE_TYPE, tableEntity, null);
        String tableType = hmsTable.getTableType();

        if (tableType != null && tableType.equals("VIRTUAL_VIEW")) {
            //Indicate that this hmsTable is a view using the classification
            //tableClassifications.add(mapperHelper.createCalculatedValueClassification("refreshRepository", tableEntity, connectorTable.getHmsViewOriginalText()));
            tableEntity = repositoryHelper.createCalculatedValueClassification(methodName, tableEntity);

        }
        return tableEntity;
    }

    private Relationship mapEndGUIDToRelationship(String tableGUID, String columnGUID, Date createTime) {
        Relationship newRelationship = repositoryHelper.createReferenceRelationship(SupportedTypes.NESTED_SCHEMA_ATTRIBUTE,
                tableGUID,
                SupportedTypes.TABLE,
                columnGUID,
                SupportedTypes.COLUMN);
        newRelationship.setMetadataCollectionId(repositoryHelper.getMetadataCollectionId());
        newRelationship.setMetadataCollectionName(repositoryHelper.getMetadataCollectionName());
        newRelationship.setStatus(InstanceStatus.ACTIVE);
        newRelationship.setCreateTime(createTime);
        return newRelationship;
    }

    private EntityDetail mapFieldSchemaToEntity(FieldSchema fieldSchema, String tableQualifiedName, Date createTime) {
        String methodName = "getNewColumnEntity";
        EntityDetail newColumnEntity = new EntityDetail();
        String columnName = fieldSchema.getName();
        newColumnEntity.setVersion(System.currentTimeMillis());
        newColumnEntity.setType(SupportedTypes.RELATIONAL_COLUMN_INSTANCETYPE);

        String columnQualifiedName = tableQualifiedName + SupportedTypes.SEPARATOR_CHAR + columnName;
        String columnGUID = null;
        try {
            columnGUID = Base64.getUrlEncoder().encodeToString(columnQualifiedName.getBytes("UTF-8"));
            newColumnEntity.setGUID(columnGUID);
        } catch (UnsupportedEncodingException e) {
            // TODO deal with error properly
            throw new RuntimeException(e);
        }
        InstanceProperties colProperties = repositoryHelper.addStringPropertyToInstance("Egeria HMS listener",
                null,
                "qualifiedName",
                columnQualifiedName,
                methodName);
        colProperties = repositoryHelper.addStringPropertyToInstance(SupportedTypes.SOURCE_NAME,
                colProperties,
                "name",
                columnName,
                methodName);
        newColumnEntity.setProperties(colProperties);
        newColumnEntity.setMetadataCollectionId(repositoryHelper.getMetadataCollectionId());
        newColumnEntity.setMetadataCollectionName(repositoryHelper.getMetadataCollectionName());
        newColumnEntity.setStatus(InstanceStatus.ACTIVE);
        //TypeEmbeddedAttribute
        newColumnEntity = repositoryHelper.createTypeEmbeddedClassification(methodName, SupportedTypes.RELATIONAL_COLUMN_TYPE, newColumnEntity, fieldSchema.getType());
        newColumnEntity.setCreateTime(createTime);
        return newColumnEntity;
    }

    public OMRSInstanceEvent buildDeletedEntityEvent(
            EntityDetail entity) {
        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.DELETED_ENTITY_EVENT,
                entity);

        instanceEvent.setEventOriginator(eventOriginator);
        instanceEvent.setHomeMetadataCollectionId(entity.getMetadataCollectionId());


        return instanceEvent;
    }

    public OMRSInstanceEvent buildNewRelationshipEvent(
            Relationship relationship) {
        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.NEW_RELATIONSHIP_EVENT,
                relationship);

        instanceEvent.setEventOriginator(eventOriginator);
        instanceEvent.setHomeMetadataCollectionId(relationship.getMetadataCollectionId());

        return instanceEvent;
    }

    /**
     * An open metadata repository is passing information about a collection of entities and relationships
     * with the other repositories in the cohort.
     *
     * @param oldTable           old table prior to update
     * @param newTable           new table after update
     * @param tableQualifiedName qualified name for table
     * @return list of omrs instance events
     */
    public  List<OMRSInstanceEvent> buildAlterTableEvents(Table oldTable, Table newTable, String tableQualifiedName) {
        List<OMRSInstanceEvent> instanceEvents = new ArrayList<>();

        EntityDetail tableEntity = mapHMSTableToEntity(newTable , tableQualifiedName);

        Map<String, FieldSchema> oldTableColumnMap = new HashMap<>();
        Map<String, FieldSchema> newTableColumnMap = new HashMap<>();
        Set<String> batchEntityNamesSet = new HashSet<>();
        // check the columns
        Iterator<FieldSchema> oldTableColumnIterator = oldTable.getSd().getColsIterator();
        while (oldTableColumnIterator.hasNext()) {
            FieldSchema fieldSchema = oldTableColumnIterator.next();
            oldTableColumnMap.put(fieldSchema.getName(), fieldSchema);
        }

        Iterator<FieldSchema> newTableColumnIterator = newTable.getSd().getColsIterator();
        while (newTableColumnIterator.hasNext()) {
            FieldSchema newFieldSchema = newTableColumnIterator.next();
            String colName = newFieldSchema.getName();
            newTableColumnMap.put(colName, newFieldSchema);
            FieldSchema oldFieldSchema = oldTableColumnMap.get(colName);
            if (oldFieldSchema == null) {
                batchEntityNamesSet.add(colName);
            } else {
                // update if there is a change in type
                // TODO do comments if required
                if (!oldFieldSchema.getType().equals(newFieldSchema.getType())) {
                    batchEntityNamesSet.add(colName);
                }
            }
        }
        List<EntityDetail> entities = new ArrayList<>();
        List<Relationship> relationships = new ArrayList<>();
        Date createTime = new Date(newTable.getCreateTime()*1000L);
        InstanceGraph instanceGraph = null;
        for (String columnName : batchEntityNamesSet) {

            EntityDetail columnEntity = mapFieldSchemaToEntity(newTableColumnMap.get(columnName), tableQualifiedName, createTime);
            Relationship relationship = mapEndGUIDToRelationship(tableEntity.getGUID(), columnEntity.getGUID(), createTime);

            relationship.setMetadataCollectionId(repositoryHelper.getMetadataCollectionId());
            relationship.setMetadataCollectionName(repositoryHelper.getMetadataCollectionName());
            relationship.setStatus(InstanceStatus.ACTIVE);
            relationship.setCreateTime(createTime);
            entities.add(columnEntity);
            relationships.add(relationship);
            instanceGraph = new InstanceGraph(entities, relationships);
        }
        OMRSInstanceEvent batchInstanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.BATCH_INSTANCES_EVENT, instanceGraph);
        batchInstanceEvent.setEventOriginator(eventOriginator);
        instanceEvents.add(batchInstanceEvent);

        // now look for deleted columns
        // reset the iterator
        Iterator<String> oldColumnIterator =  oldTableColumnMap.keySet().iterator();
        while (oldColumnIterator.hasNext()) {
            String oldColName = oldColumnIterator.next();
            if (!newTableColumnMap.containsKey(oldColName) ) {
                // delete this column as it exists in the old but not in the new table
                EntityDetail entity =  null;

                OMRSInstanceEvent deleteColInstanceEvent = buildDeletedEntityEvent(entity);
                instanceEvents.add(deleteColInstanceEvent);
            }
        }


        return instanceEvents;
    }


    public EntityDetail getTableEntityToDelete(Table tableToDelete, String tableQualifiedName) {
        EntityDetail entity = new EntityDetail();
        try {
            String guid = Base64.getUrlEncoder().encodeToString(tableQualifiedName.getBytes("UTF-8"));
            entity.setGUID(guid);
        } catch (UnsupportedEncodingException e) {
            // TODO deal with error properly
            throw new RuntimeException(e);
        }

        entity.setType(SupportedTypes.RELATIONAL_TABLE_INSTANCETYPE);
        entity.setMetadataCollectionId(repositoryHelper.getMetadataCollectionId());
        entity.setCreateTime(new Date(tableToDelete.getCreateTime()*1000L));
        entity.setVersion(new Date().getTime());
        return entity;
    }
    public EntityDetail getColumnEntityToDelete(Table table, String columnName, String tableQualifiedName) {
        EntityDetail entity = new EntityDetail();
        try {
            String guid = Base64.getUrlEncoder().encodeToString(tableQualifiedName.getBytes("UTF-8"));
            entity.setGUID(guid);
        } catch (UnsupportedEncodingException e) {
            // TODO deal with error properly
            throw new RuntimeException(e);
        }

        entity.setType(SupportedTypes.RELATIONAL_COLUMN_INSTANCETYPE);
        entity.setMetadataCollectionId(repositoryHelper.getMetadataCollectionId());
        // Assert  the create time of the table is the same as the create time of the column for Egeria
        // so the create time will match and the deletion will occur
        entity.setCreateTime(getCreateTime(table));
        entity.setVersion(new Date().getTime());
        return entity;
    }

    /**
     * get create time as a Date from hms table
     * @param table hms table
     * @return date
     */
    private static Date getCreateTime(Table table) {
        return new Date(table.getCreateTime() * 1000L);
    }
}
