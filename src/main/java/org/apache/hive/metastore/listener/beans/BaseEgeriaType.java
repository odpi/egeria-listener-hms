/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.apache.hive.metastore.listener.beans;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Classification.class, name = "Classification"),
        @JsonSubTypes.Type(value = Type.class, name = "InstanceType"),
        @JsonSubTypes.Type(value = Entity.class, name = "Entity"),
        @JsonSubTypes.Type(value = Name.class, name = "PrimitivePropertyValue")
})

public class BaseEgeriaType
{

}
