/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.hmslistener.helpers;

import org.odpi.openmetadata.hmslistener.SupportedTypes;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class OMRSRepositoryHelper {
    private static final Logger logger = LoggerFactory.getLogger(OMRSRepositoryHelper.class);

    private TypeNameToTypeMapper typeNameToTypeMapper = new TypeNameToTypeMapper();
    String metadataCollectionId = null;
    String metadataCollectionName = null;

    public OMRSRepositoryHelper(String metadataCollectionId, String metadataCollectionName ) {
        this.metadataCollectionId = metadataCollectionId;
        this.metadataCollectionName = metadataCollectionName;

    }

    public InstanceProperties addStringPropertyToInstance(String sourceName,
                                                          InstanceProperties properties,
                                                          String propertyName,
                                                          String propertyValue,
                                                          String methodName) {
        InstanceProperties resultingProperties;

        if (propertyValue != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Adding property " + propertyName + " for " + methodName);
            }
            if (properties == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("First property");
                }

                resultingProperties = new InstanceProperties();
            } else {
                resultingProperties = properties;
            }


            PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();

            primitivePropertyValue.setHeaderVersion(InstancePropertyValue.CURRENT_INSTANCE_PROPERTY_VALUE_HEADER_VERSION);
            primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
            primitivePropertyValue.setPrimitiveValue(propertyValue);
            primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
            primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());

            resultingProperties.setProperty(propertyName, primitivePropertyValue);

            return resultingProperties;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Null property");
            }
            return properties;
        }
    }
    /**
     *
     * @param apiName API name for diagnostics
     * @param type the schema type name.
     * @param entity entity to apply the classification to
     * @param dataType column type if a column
     * @return the embedded type classification

     */
    public Classification createTypeEmbeddedClassification(String apiName, String type, EntityDetail entity, String dataType)  {
        String methodName = "createTypeEmbeddedClassification";
        Classification classification = new Classification();
        classification.setName(SupportedTypes.TYPE_EMBEDDED_ATTRIBUTE);
        classification.setType(typeNameToTypeMapper.getTypeFromName(SupportedTypes.TYPE_EMBEDDED_ATTRIBUTE));
        InstanceProperties instanceProperties = new InstanceProperties();
        addStringPropertyToInstance(methodName, instanceProperties, "schemaTypeName", type, methodName);
        if (dataType != null ) {
            addStringPropertyToInstance(methodName, instanceProperties, "dataType", dataType, methodName);
        }
        classification.setProperties(instanceProperties);
        addClassificationToEntity(methodName, entity, classification, methodName);
        return classification;

    }
    public Classification createCalculatedValueClassification(String apiName, EntityDetail entity) {

        String methodName = "createCalculatedValueClassification";
        Classification classification = new Classification();
        classification.setName(SupportedTypes.CALCULATED_VALUE);
        // TODO is there any properties  we need to take around the view
//        InstanceProperties instanceProperties = new InstanceProperties();
//        classification.setProperties(instanceProperties);
        classification.setVersion(System.currentTimeMillis());
        // TODO origin
        addClassificationToEntity(methodName, entity, classification, methodName);
        return classification;

    }

    public EntityDetail addClassificationToEntity(String sourceName,
                                                  EntityDetail entity,
                                                  Classification newClassification,
                                                  String methodName) {
       // final String thisMethodName = "addClassificationToEntity";

        if (newClassification != null) {
            if (entity != null) {
                EntityDetail updatedEntity = new EntityDetail(entity);

                updatedEntity.setClassifications(this.addClassificationToList(sourceName,
                        entity.getClassifications(),
                        newClassification,
                        methodName));
                return updatedEntity;
            } else {
                throw new RuntimeException("addClassificationToEntity passed a null entity");
            }
        } else {
            throw new RuntimeException("addClassificationToEntity passed a null classification");
        }
    }

    public List<Classification> addClassificationToList(String sourceName,
                                                        List<Classification> classificationList,
                                                        Classification newClassification,
                                                        String methodName) {
        if (newClassification != null) {
            /*
             * Duplicate classifications are not allowed so a hash map is used to remove duplicates.
             */
            Map<String, Classification> entityClassificationsMap = new HashMap<>();

            if (classificationList != null) {
                for (Classification existingClassification : classificationList) {
                    if (existingClassification != null) {
                        entityClassificationsMap.put(existingClassification.getName(), existingClassification);
                    }
                }
            }

            Classification existingClassification = entityClassificationsMap.get(newClassification.getName());

            /*
             * Ignore older versions of the classification
             */
            if ((existingClassification == null) ||
                    (existingClassification.getVersion() < newClassification.getVersion())) {
                entityClassificationsMap.put(newClassification.getName(), newClassification);
            }

            if (entityClassificationsMap.isEmpty()) {
                return null;
            } else {
                return new ArrayList<>(entityClassificationsMap.values());
            }

        } else {
             throw new RuntimeException("addClassificationToList passed a null classification");
        }
    }
    public Relationship createReferenceRelationship(String relationshipTypeName, String end1GUID, String end1TypeName, String end2GUID, String end2TypeName)  {
        String methodName = "createReferenceRelationship";

        Relationship relationship = null;

            relationship = getSkeletonRelationship(methodName,
                    metadataCollectionId,
                    metadataCollectionName,
                    InstanceProvenanceType.LOCAL_COHORT,
                    "userName",   //TODO
                    relationshipTypeName);
            relationship.setMetadataCollectionName(metadataCollectionName);


        String connectionToAssetCanonicalName = end1GUID + SupportedTypes.SEPARATOR_CHAR + relationshipTypeName + SupportedTypes.SEPARATOR_CHAR + end2GUID;
        String relationshipGUID = null;
        try {
            relationshipGUID = Base64.getUrlEncoder().encodeToString(connectionToAssetCanonicalName.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
           new RuntimeException(e);
        }

        relationship.setGUID(relationshipGUID);
        //end 1
        EntityProxy entityProxy1 = getEntityProxySkeleton(end1GUID, end1TypeName);
        relationship.setEntityOneProxy(entityProxy1);

        //end 2
        EntityProxy entityProxy2 = getEntityProxySkeleton(end2GUID, end2TypeName);
        relationship.setEntityTwoProxy(entityProxy2);

        return relationship;

    }
    public Relationship getSkeletonRelationship(String                 sourceName,
                                                String                 metadataCollectionId,
                                                InstanceProvenanceType provenanceType,
                                                String                 userName,
                                                String                 typeName)
    {
        return this.getSkeletonRelationship(sourceName, metadataCollectionId, null, provenanceType, userName, typeName);
    }


    /**
     * Return a relationship with the header and type information filled out.  The caller only needs to add properties
     * to complete the setup of the relationship.
     *
     * @param sourceName             source of the request (used for logging)
     * @param metadataCollectionId   unique identifier for the home metadata collection
     * @param metadataCollectionName unique name for the home metadata collection
     * @param provenanceType         origin type of the relationship
     * @param userName               name of the creator
     * @param typeName               name of the relationship's type
     * @return partially filled out relationship needs properties
     */
    public Relationship getSkeletonRelationship(String                 sourceName,
                                                String                 metadataCollectionId,
                                                String                 metadataCollectionName,
                                                InstanceProvenanceType provenanceType,
                                                String                 userName,
                                                String                 typeName)
    {
        final String methodName = "getSkeletonRelationship";

        Relationship relationship = new Relationship();
        String       guid         = UUID.randomUUID().toString();

        relationship.setHeaderVersion(InstanceAuditHeader.CURRENT_AUDIT_HEADER_VERSION);
        relationship.setInstanceProvenanceType(provenanceType);
        relationship.setMetadataCollectionId(metadataCollectionId);
        relationship.setMetadataCollectionName(metadataCollectionName);
        relationship.setCreateTime(new Date());
        relationship.setGUID(guid);
        relationship.setVersion(System.currentTimeMillis());
        relationship.setType(typeNameToTypeMapper.getTypeFromName(typeName));

//        relationship.setStatus(repositoryContentManager.getInitialStatus(sourceName, typeName, methodName));
//        relationship.setCreatedBy(userName);
//        relationship.setInstanceURL(repositoryContentManager.getRelationshipURL(sourceName, guid));

        return relationship;
    }
    /**
     * Create an entity proxy with the supplied parameters
     * @param guid GUID
     * @param typeName type name
     * @return entity proxy
     */
    private EntityProxy getEntityProxySkeleton(String guid, String typeName)  {
        EntityProxy proxy = new EntityProxy();
        proxy.setType(typeNameToTypeMapper.getTypeFromName(typeName));
        proxy.setGUID(guid);
        proxy.setMetadataCollectionId(metadataCollectionId);
        proxy.setMetadataCollectionName(metadataCollectionName);
        return proxy;
    }

}
