package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.RepositoryElementHeader;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceElementHeader provides a common base for all instance information from the metadata collection.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClassificationEntityExtension.class, name = "org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationEntityExtension"),
        @JsonSubTypes.Type(value = InstanceAuditHeader.class, name = "org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceAuditHeader"),
        @JsonSubTypes.Type(value = InstanceGraph.class, name = "org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph"),
        @JsonSubTypes.Type(value = InstanceType.class, name = "org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType"),
        @JsonSubTypes.Type(value = InstancePropertyValue.class, name = "org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue"),
        @JsonSubTypes.Type(value = InstanceProperties.class, name = "org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties")
})
public abstract class InstanceElementHeader extends RepositoryElementHeader
{
    private static final long serialVersionUID = 1L;

    /**
     * Default Constructor sets the instance to nulls
     */
    public InstanceElementHeader()
    {
        super();

        /*
         * Nothing to do.
         */
    }


    /**
     * Copy/clone constructor set values from the template
     *
     * @param template org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceElementHeader to copy
     */
    public InstanceElementHeader(InstanceElementHeader   template)
    {
        super (template);

        /*
         * Nothing to do.
         */
    }
}
