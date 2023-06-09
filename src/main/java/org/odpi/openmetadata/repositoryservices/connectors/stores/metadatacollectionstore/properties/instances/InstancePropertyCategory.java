package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyCategory defines the list of value types for the properties for open metadata.
 * It is used in the org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue class to distinguish its subclasses.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum InstancePropertyCategory implements Serializable
{
    UNKNOWN     (0, "<Unknown>", "Uninitialized org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue object."),
    PRIMITIVE   (1, "Primitive", "A primitive type."),
    ENUM        (2, "Enum",      "A pre-defined list of valid values."),
    STRUCT      (3, "Struct",    "A collection of related properties."),
    MAP         (4, "Map",       "A set of name->value pairs where all names are unique in the map."),
    ARRAY       (5, "Array",     "An ordered list of values, each with the same type.");

    private static final long serialVersionUID = 1L;

    private int            typeCode;
    private String         typeName;
    private String         typeDescription;


    /**
     * Default Constructor
     *
     * @param typeCode ordinal for this enum
     * @param typeName symbolic name for this enum
     * @param typeDescription short description for this enum
     */
    InstancePropertyCategory(int     typeCode, String   typeName, String   typeDescription)
    {
        /*
         * Save the values supplied
         */
        this.typeCode = typeCode;
        this.typeName = typeName;
        this.typeDescription = typeDescription;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int type code
     */
    public int getOrdinal()
    {
        return typeCode;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    public String getName()
    {
        return typeName;
    }


    /**
     * Return the default description for the type for this enum instance.
     *
     * @return String default description
     */
    public String getDescription()
    {
        return typeDescription;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyCategory{" +
                "typeCode=" + typeCode +
                ", typeName='" + typeName + '\'' +
                ", typeDescription='" + typeDescription + '\'' +
                '}';
    }
}
