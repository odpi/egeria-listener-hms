/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.hmslistener.mapper;

import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.hive.metastore.events.AlterTableEvent;
import org.apache.hadoop.hive.metastore.events.CreateTableEvent;
import org.apache.hadoop.hive.metastore.events.DropTableEvent;
import org.apache.hive.metastore.listener.HMSListener;
import org.odpi.openmetadata.hmslistener.SupportedTypes;
import org.odpi.openmetadata.hmslistener.helpers.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.*;


public class HMSToOMRSInstanceEventMapper {

    private static final Logger logger = LoggerFactory.getLogger(HMSListener.class);
    private String qualifiedNamePrefix = null;

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

        Table hmsTable = createTableEvent.getTable();

        List<OMRSInstanceEvent> instanceEvents = omrsInstanceEventBuilder.buildNewTableEvents(hmsTable, getQualifiedNameAboveTable(hmsTable));

        if(logger.isDebugEnabled()) {
            logger.debug("<== " + methodName);
        }
        return instanceEvents;
    }


    public List<OMRSInstanceEvent> getEventsForDropTable(DropTableEvent dropTableEvent) {
        List<OMRSInstanceEvent> instanceEvents = new ArrayList<>();

        // get qualifiedName of table and calculate the guid from it
        Table tableToDelete = dropTableEvent.getTable();
        String tableQualifiedName = getQualifiedNameAboveTable(tableToDelete) + SupportedTypes.SEPARATOR_CHAR + tableToDelete.getTableName();

        EntityDetail entity = omrsInstanceEventBuilder.getTableEntityToDelete(tableToDelete, tableQualifiedName);
        instanceEvents.add(omrsInstanceEventBuilder.buildDeletedEntityEvent(entity));
        return instanceEvents;
    }

    /**
     * Get OMRS instance events for an altertable HMS event
     * @param alterTableEvent hms event
     * @return list of omr instance events
     */
    public List<OMRSInstanceEvent> getEventsForAlterTable(AlterTableEvent alterTableEvent) {

        List<OMRSInstanceEvent> instanceEvents = new ArrayList<>();

        // get qualifiedName of table and calculate the guid from it
        Table oldTable = alterTableEvent.getOldTable();
        Table newTable = alterTableEvent.getNewTable();
        String tableQualifiedName = getQualifiedNameAboveTable(newTable) + SupportedTypes.SEPARATOR_CHAR + newTable.getTableName();


        // String qualifiedName = getQualifiedNameAboveTable(newTable) + SupportedTypes.SEPARATOR_CHAR + newTable.getTableName();

        // check if we think this is the same table from old to new
        // if not we need to delete and create

        if (canUpdateTable(oldTable, newTable )) {
            instanceEvents = omrsInstanceEventBuilder.buildAlterTableEvents(oldTable, newTable, tableQualifiedName);
        } else {
            // delete the old table
            EntityDetail tableEntityToDelete = omrsInstanceEventBuilder.getTableEntityToDelete(oldTable, tableQualifiedName);
            instanceEvents.add(omrsInstanceEventBuilder.buildDeletedEntityEvent(tableEntityToDelete));
            // create the new one
            instanceEvents.addAll(omrsInstanceEventBuilder.buildNewTableEvents(newTable, tableQualifiedName));
        }
        return instanceEvents;
    }
    private boolean canUpdateTable(Table oldTable, Table newTable ) {
        boolean canUpdateTable = true;
        if (!oldTable.getTableName().equals(newTable.getTableName())) {
            canUpdateTable =false;
        } else if (!oldTable.getCatName().equals(newTable.getCatName())) {
            canUpdateTable =false;
        } else if (!oldTable.getDbName().equals(newTable.getDbName())) {
            canUpdateTable =false;
        }
        return canUpdateTable;
    }

    public List<OMRSInstanceEvent> alterTable(AlterTableEvent alterTableEvent) {
        List<OMRSInstanceEvent> instanceEvents = new ArrayList<>();


            return instanceEvents;
    }

//    private EntityDetail getTableEntityToCreate( Table hmsTable) {
//        String
//        EntityDetail newTableEntity = new EntityDetail();
//        newTableEntity.setType(SupportedTypes.RELATIONAL_TABLE_INSTANCETYPE);
//
//        String qualifiedNameAboveTable = null;
//        String tableName = hmsTable.getTableName();
//
//
//
//        qualifiedNameAboveTable = getQualifiedNameAboveTable(hmsTable);
//
//        String tableQualifiedName  = qualifiedNameAboveTable + SupportedTypes.SEPARATOR_CHAR + tableName;
//        String tableGUID = null;
//        try {
//            tableGUID = Base64.getUrlEncoder().encodeToString(tableQualifiedName.getBytes("UTF-8"));
//            newTableEntity.setGUID(tableGUID);
//        } catch (UnsupportedEncodingException e) {
//            // TODO deal with error properly
//            throw new RuntimeException(e);
//        }
//        Date createTime = new Date(hmsTable.getCreateTime());
//        newTableEntity.setCreateTime(createTime);
//        InstanceProperties instanceProperties = repositoryHelper.addStringPropertyToInstance("Egeria HMS listener",
//                null,
//                "qualifiedName",
//                tableQualifiedName,
//                methodName);
//        instanceProperties = repositoryHelper.addStringPropertyToInstance(SupportedTypes.SOURCE_NAME,
//                instanceProperties,
//                "name",
//                tableName,
//                methodName);
//
//        newTableEntity.setVersion(System.currentTimeMillis());
//        newTableEntity.setProperties(instanceProperties);
//        newTableEntity.setMetadataCollectionId(repositoryHelper.getMetadataCollectionId());
//        newTableEntity.setMetadataCollectionName(repositoryHelper.getMetadataCollectionName());
//        newTableEntity.setStatus(InstanceStatus.ACTIVE);
//        //TypeEmbeddedAttribute
//        newTableEntity = repositoryHelper.createTypeEmbeddedClassification(methodName, SupportedTypes.RELATIONAL_TABLE_TYPE, newTableEntity, null) ;
//        String tableType = hmsTable.getTableType();
//
//        if (tableType != null && tableType.equals("VIRTUAL_VIEW")) {
//            //Indicate that this hmsTable is a view using the classification
//            //tableClassifications.add(mapperHelper.createCalculatedValueClassification("refreshRepository", tableEntity, connectorTable.getHmsViewOriginalText()));
//            newTableEntity = repositoryHelper.createCalculatedValueClassification(methodName, newTableEntity) ;
//
//        }
//    }

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

}
