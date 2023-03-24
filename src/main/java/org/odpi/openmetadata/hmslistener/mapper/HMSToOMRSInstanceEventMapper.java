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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;


public class HMSToOMRSInstanceEventMapper {

    private static final Logger logger = LoggerFactory.getLogger(HMSListener.class);

    private OMRSInstanceEventBuilder omrsInstanceEventBuilder = null;
    private String        qualifiedNameAboveTable =null;

    private OMRSRepositoryHelper repositoryHelper = null;
    public HMSToOMRSInstanceEventMapper(String       originatorMetadataCollectionId,
                                        String       originatorServerName,
                                        String       originatorServerType,
                                        String       originatorOrganizationName,
                                        String       qualifiedNameAboveTable) {
        OMRSEventOriginator eventOriginator= new OMRSEventOriginator();
        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);
        omrsInstanceEventBuilder = new OMRSInstanceEventBuilder(eventOriginator);
        this.qualifiedNameAboveTable = qualifiedNameAboveTable;
        repositoryHelper = new OMRSRepositoryHelper(originatorMetadataCollectionId, originatorServerName);

    }

    public List<OMRSInstanceEvent> getEventsForCreateTable(CreateTableEvent createTableEvent) {
        String methodName = "createTable";
        if(logger.isDebugEnabled()) {
            logger.debug("==> " + methodName);
        }
        List<OMRSInstanceEvent> instanceEvents = new ArrayList<>();
        EntityDetail newTableEntity = new EntityDetail();
        newTableEntity.setType(SupportedTypes.TABLE_TYPE);
        Table hmsTable = createTableEvent.getTable();
        String tableName = hmsTable.getTableName();
        String tableQualifiedName  = qualifiedNameAboveTable + SupportedTypes.SEPARATOR_CHAR + tableName;
        String tableGUID = null;
        try {
            tableGUID = Base64.getUrlEncoder().encodeToString(tableQualifiedName.getBytes("UTF-8"));
            newTableEntity.setGUID(tableGUID);
        } catch (UnsupportedEncodingException e) {
            // TODO deal with error properly
            throw new RuntimeException(e);
        }
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
        //TypeEmbeddedAttribute
        repositoryHelper.createTypeEmbeddedClassification(methodName, SupportedTypes.RELATIONAL_TABLE_TYPE, newTableEntity, null) ;
        String tableType = hmsTable.getTableType();

        if (tableType != null && tableType.equals("VIRTUAL_VIEW")) {
            //Indicate that this hmsTable is a view using the classification
            //tableClassifications.add(mapperHelper.createCalculatedValueClassification("refreshRepository", tableEntity, connectorTable.getHmsViewOriginalText()));
            repositoryHelper.createCalculatedValueClassification(methodName, newTableEntity) ;

        }
        instanceEvents.add(omrsInstanceEventBuilder.buildNewEntityEvent(newTableEntity));
        // TODO add relationship
           String relationalDBTypeGuid = null;
        try {
            relationalDBTypeGuid = Base64.getUrlEncoder().encodeToString(qualifiedNameAboveTable.getBytes("UTF-8"));
            Relationship newRelationship = repositoryHelper.createReferenceRelationship(SupportedTypes.ATTRIBUTE_FOR_SCHEMA,
                    relationalDBTypeGuid,
                    SupportedTypes.RELATIONAL_DB_SCHEMA_TYPE,
                    tableGUID,
                    SupportedTypes.TABLE);
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
                newColumnEntity.setType(SupportedTypes.COLUMN_TYPE);
    
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
                
                //TypeEmbeddedAttribute
                repositoryHelper.createTypeEmbeddedClassification(methodName, SupportedTypes.RELATIONAL_COLUMN_TYPE, newColumnEntity, fieldSchema.getType()) ;

                instanceEvents.add(omrsInstanceEventBuilder.buildNewEntityEvent(newTableEntity));
                Relationship newRelationship = repositoryHelper.createReferenceRelationship(SupportedTypes.ATTRIBUTE_FOR_SCHEMA,
                        tableGUID,
                        SupportedTypes.TABLE,
                        columnGUID,
                        SupportedTypes.COLUMN);
                instanceEvents.add(omrsInstanceEventBuilder.buildNewRelationshipEvent(newRelationship));
            }
        }

        if(logger.isDebugEnabled()) {
            logger.debug("<== " + methodName);
        }
        return instanceEvents;
    }




    public List<OMRSInstanceEvent> getEventsForDropTable(DropTableEvent dropTableEvent) {
        List<OMRSInstanceEvent> instanceEvents = new ArrayList<>();
        EntityDetail entity = new EntityDetail();
        // get qualifiedName of table and calculate the guid from it
        String qualifiedName  = qualifiedNameAboveTable + SupportedTypes.SEPARATOR_CHAR + dropTableEvent.getTable().getTableName();
        try {
            String guid = Base64.getUrlEncoder().encodeToString(qualifiedName.getBytes("UTF-8"));
            entity.setGUID(guid);
        } catch (UnsupportedEncodingException e) {
            // TODO deal with error properly
            throw new RuntimeException(e);
        }

        entity.setType(SupportedTypes.TABLE_TYPE);
        omrsInstanceEventBuilder.buildDeletedEntityEvent(entity);

        return instanceEvents;
    }
    public List<OMRSInstanceEvent> alterTable(AlterTableEvent alterTableEvent) {
        List<OMRSInstanceEvent> instanceEvents = new ArrayList<>();


        return instanceEvents;
    }

}
