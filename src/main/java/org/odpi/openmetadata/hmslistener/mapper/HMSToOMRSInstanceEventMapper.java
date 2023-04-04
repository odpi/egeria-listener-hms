/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.hmslistener.mapper;


import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.hive.metastore.events.AlterTableEvent;
import org.apache.hadoop.hive.metastore.events.CreateTableEvent;
import org.apache.hadoop.hive.metastore.events.DropTableEvent;
import org.apache.hive.metastore.listener.HMSListener;
import org.odpi.openmetadata.hmslistener.SupportedTypes;
import org.odpi.openmetadata.hmslistener.helpers.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.*;


public class HMSToOMRSInstanceEventMapper {

    private static final Logger logger = LoggerFactory.getLogger(HMSListener.class);
    private String qualifiedNamePrefix = null;

    String qualifiedNameAboveTable = null;

    private OMRSInstanceEventBuilder omrsInstanceEventBuilder = null;

    private OMRSRepositoryHelper repositoryHelper = null;


    public HMSToOMRSInstanceEventMapper(String       originatorMetadataCollectionId,
                                        String       originatorServerName,
                                        String       originatorServerType,
                                        String       originatorOrganizationName,
                                        String       qualifiedNamePrefix) {
        OMRSEventOriginator eventOriginator= new OMRSEventOriginator();
        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);
        omrsInstanceEventBuilder = new OMRSInstanceEventBuilder(eventOriginator);
        repositoryHelper = new OMRSRepositoryHelper(originatorMetadataCollectionId, originatorServerName);
        this.qualifiedNamePrefix = qualifiedNamePrefix;
    }

    public List<OMRSInstanceEvent> getEventsForCreateTable(CreateTableEvent createTableEvent) {
        String methodName = "createTable";
        if(logger.isDebugEnabled()) {
            logger.debug("==> " + methodName);
        }
        List<OMRSInstanceEvent> instanceEvents = new ArrayList<>();
        EntityDetail newTableEntity = new EntityDetail();
        newTableEntity.setType(SupportedTypes.RELATIONAL_TABLE_INSTANCETYPE);
        Table hmsTable = createTableEvent.getTable();
        String qualifiedNameAboveTable = null;
        String tableName = hmsTable.getTableName();



         qualifiedNameAboveTable = getQualifiedNameAboveTable(hmsTable);

        String tableQualifiedName  = qualifiedNameAboveTable + SupportedTypes.SEPARATOR_CHAR + tableName;
        String tableGUID = null;
        try {
            tableGUID = Base64.getUrlEncoder().encodeToString(tableQualifiedName.getBytes("UTF-8"));
            newTableEntity.setGUID(tableGUID);
        } catch (UnsupportedEncodingException e) {
            // TODO deal with error properly
            throw new RuntimeException(e);
        }
        Date createTime = new Date(hmsTable.getCreateTime());
        newTableEntity.setCreateTime(createTime);
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

        newTableEntity.setVersion(System.currentTimeMillis());
        newTableEntity.setProperties(instanceProperties);
        newTableEntity.setMetadataCollectionId(repositoryHelper.getMetadataCollectionId());
        newTableEntity.setMetadataCollectionName(repositoryHelper.getMetadataCollectionName());
        newTableEntity.setStatus(InstanceStatus.ACTIVE);
        //TypeEmbeddedAttribute
        newTableEntity = repositoryHelper.createTypeEmbeddedClassification(methodName, SupportedTypes.RELATIONAL_TABLE_TYPE, newTableEntity, null) ;
        String tableType = hmsTable.getTableType();

        if (tableType != null && tableType.equals("VIRTUAL_VIEW")) {
            //Indicate that this hmsTable is a view using the classification
            //tableClassifications.add(mapperHelper.createCalculatedValueClassification("refreshRepository", tableEntity, connectorTable.getHmsViewOriginalText()));
            newTableEntity = repositoryHelper.createCalculatedValueClassification(methodName, newTableEntity) ;

        }

        instanceEvents.add(omrsInstanceEventBuilder.buildNewEntityEvent(newTableEntity));
        // add relationship
        String relationalDBTypeGuid = null;
        try {
            relationalDBTypeGuid = Base64.getUrlEncoder().encodeToString(qualifiedNameAboveTable.getBytes("UTF-8"));
            Relationship newRelationship = repositoryHelper.createReferenceRelationship(SupportedTypes.ATTRIBUTE_FOR_SCHEMA,
                    relationalDBTypeGuid,
                    SupportedTypes.RELATIONAL_DB_SCHEMA_TYPE,
                    tableGUID,
                    SupportedTypes.TABLE);
            newRelationship.setCreateTime(createTime);
            instanceEvents.add(omrsInstanceEventBuilder.buildNewRelationshipEvent(newRelationship));

        } catch (UnsupportedEncodingException e) {
            // TODO deal with error properly
            throw new RuntimeException(e);
        }


        //TODO add columns
        if (hmsTable.getSd() != null) {
            Iterator<FieldSchema> colsIterator = hmsTable.getSd().getColsIterator();
            while (colsIterator.hasNext()) {
                FieldSchema fieldSchema = colsIterator.next();
                EntityDetail newColumnEntity = new EntityDetail();
                String columnName = fieldSchema.getName();
                newColumnEntity.setVersion(System.currentTimeMillis());
                newColumnEntity.setType(SupportedTypes.RELATIONAL_COLUMN_INSTANCETYPE);
    
                String columnQualifiedName = tableQualifiedName+"."+ columnName;
                String columnGUID = null;
                try {
                   columnGUID = Base64.getUrlEncoder().encodeToString( columnQualifiedName.getBytes("UTF-8"));
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
                newColumnEntity = repositoryHelper.createTypeEmbeddedClassification(methodName, SupportedTypes.RELATIONAL_COLUMN_TYPE, newColumnEntity, fieldSchema.getType()) ;
                newColumnEntity.setCreateTime(createTime);
                instanceEvents.add(omrsInstanceEventBuilder.buildNewEntityEvent(newColumnEntity));
                Relationship newRelationship = repositoryHelper.createReferenceRelationship(SupportedTypes.NESTED_SCHEMA_ATTRIBUTE,
                        tableGUID,
                        SupportedTypes.TABLE,
                        columnGUID,
                        SupportedTypes.COLUMN);
                newRelationship.setMetadataCollectionId(repositoryHelper.getMetadataCollectionId());
                newRelationship.setMetadataCollectionName(repositoryHelper.getMetadataCollectionName());
                newRelationship.setStatus(InstanceStatus.ACTIVE);
                newRelationship.setCreateTime(createTime);
                instanceEvents.add(omrsInstanceEventBuilder.buildNewRelationshipEvent(newRelationship));
            }
        }

        if(logger.isDebugEnabled()) {
            logger.debug("<== " + methodName);
        }
        return instanceEvents;
    }

    private String getQualifiedNameAboveTable(Table hmsTable ) {
        return qualifiedNamePrefix + "::" +
                hmsTable.getCatName() +
                SupportedTypes.SEPARATOR_CHAR +
                hmsTable.getDbName() +
                SupportedTypes.SEPARATOR_CHAR +
                SupportedTypes.DEFAULT_DEPLOYED_SCHEMA_TOKEN_NAME +
                SupportedTypes.SEPARATOR_CHAR +
                SupportedTypes.DEFAULT_RELATIONAL_DB_SCHEMA_TYPE ;
    }


    public List<OMRSInstanceEvent> getEventsForDropTable(DropTableEvent dropTableEvent) {
        List<OMRSInstanceEvent> instanceEvents = new ArrayList<>();
        EntityDetail entity = new EntityDetail();
        // get qualifiedName of table and calculate the guid from it
        Table tabletoDelete = dropTableEvent.getTable();

        String qualifiedName = getQualifiedNameAboveTable(tabletoDelete) + SupportedTypes.SEPARATOR_CHAR + tabletoDelete.getTableName();
        try {
            String guid = Base64.getUrlEncoder().encodeToString(qualifiedName.getBytes("UTF-8"));
            entity.setGUID(guid);
        } catch (UnsupportedEncodingException e) {
            // TODO deal with error properly
            throw new RuntimeException(e);
        }

        entity.setType(SupportedTypes.RELATIONAL_TABLE_INSTANCETYPE);
        entity.setMetadataCollectionId(repositoryHelper.getMetadataCollectionId());
        entity.setCreateTime(new Date(tabletoDelete.getCreateTime()));
        entity.setVersion(new Date().getTime());
        instanceEvents.add(omrsInstanceEventBuilder.buildDeletedEntityEvent(entity));
        return instanceEvents;
    }
    public List<OMRSInstanceEvent> alterTable(AlterTableEvent alterTableEvent) {
        List<OMRSInstanceEvent> instanceEvents = new ArrayList<>();


        return instanceEvents;
    }

}
