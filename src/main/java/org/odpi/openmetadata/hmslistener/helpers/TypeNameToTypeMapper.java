/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.hmslistener.helpers;

import org.odpi.openmetadata.hmslistener.SupportedTypes;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypeNameToTypeMapper {
    private static final Logger logger = LoggerFactory.getLogger(TypeNameToTypeMapper.class);

    public InstanceType getTypeFromName(String name){
        InstanceType type = null;
        if (name.equals(SupportedTypes.TABLE)) {
            type = SupportedTypes.RELATIONAL_TABLE_INSTANCETYPE;
        } else  if (name.equals(SupportedTypes.COLUMN)) {
            type = SupportedTypes.RELATIONAL_COLUMN_INSTANCETYPE;
        } else  if (name.equals(SupportedTypes.ATTRIBUTE_FOR_SCHEMA)) {
            type = SupportedTypes.ATTRIBUTE_FOR_SCHEMA_TYPE;
        } else  if (name.equals(SupportedTypes.NESTED_SCHEMA_ATTRIBUTE)) {
            type = SupportedTypes.NESTED_SCHEMA_ATTRIBUTE_TYPE;
        } else  if (name.equals(SupportedTypes.TYPE_EMBEDDED_ATTRIBUTE)) {
            type = SupportedTypes.TYPE_EMBEDDED_ATTRIBUTE_TYPE;
        }

        return type;
    }
}
