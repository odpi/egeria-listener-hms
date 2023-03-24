/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.hmslistener;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;

import java.util.Arrays;
import java.util.List;

public class SupportedTypes {

    // TODO config?
    // this should be a natural to the technology separator character to be used to separate elements in a name.
    public static final String SEPARATOR_CHAR = ".";

    public static final String DEFAULT_DEPLOYED_SCHEMA_TOKEN_NAME = "default-deployed-schema";
    public static final String DEFAULT_RELATIONAL_DB_SCHEMA_TYPE = "default-relational-DB-Schema-Type";


    public static final String CONNECTION = "Connection";
    public static final String CONNECTOR_TYPE = "ConnectorType";
    public static final String ENDPOINT = "Endpoint";

    public static final String SCHEMA_TYPE_NAME = "schemaTypeName"; // property name in the type embedded classification for entity type
    public static final String DATA_TYPE = "dataType" ; // property name in the type embedded classification for data type
    // hard coded values
    public static final String CONNECTION_VALUE = "connection";

    public static final String CONNECTOR_TYPE_VALUE = "connectorType";

    public static final String ENDPOINT_VALUE = "Endpoint";

    public static final String CONNECTION_ENDPOINT = "ConnectionEndpoint";
    public static final String CONNECTION_CONNECTOR_TYPE = "ConnectionConnectorType";
    public static final String CONNECTION_TO_ASSET = "ConnectionToAsset";
    public static final String DATABASE = "Database";

    public static final String RELATIONAL_DB_SCHEMA_TYPE = "RelationalDBSchemaType";

    public static final String DEPLOYED_DATABASE_SCHEMA = "DeployedDatabaseSchema";
    // relationship
    public static final String DATA_CONTENT_FOR_DATASET = "DataContentForDataSet";
    // relationship
    public static final String ASSET_SCHEMA_TYPE = "AssetSchemaType";
    //relationship
    public static final String ATTRIBUTE_FOR_SCHEMA = "AttributeForSchema";
     // entities and types
    public static final String TABLE = "RelationalTable";
    public static final String COLUMN = "RelationalColumn";

    public static final String RELATIONAL_TABLE_TYPE = "RelationalTableType";

    // relationships and types
    public static final String SCHEMA_ATTRIBUTE_TYPE = "SchemaAttributeType";

    public static final String RELATIONAL_COLUMN_TYPE = "RelationalColumnType";

    public static final String NESTED_SCHEMA_ATTRIBUTE = "NestedSchemaAttribute";
    public static final InstanceType TABLE_TYPE = new InstanceType(
            TypeDefCategory.ENTITY_DEF,
            "1321bcc0-dc6a-48ed-9ca6-0c6f934b0b98",
             TABLE,
    1L
    );
    public static final InstanceType COLUMN_TYPE = new InstanceType(
            TypeDefCategory.ENTITY_DEF,
            "aa8d5470-6dbc-4648-9e2f-045e5df9d2f9",
            COLUMN,
            1L
    );
    // relationships and types
    public static final InstanceType ATTRIBUTE_FOR_SCHEMA_TYPE = new InstanceType(
            TypeDefCategory.RELATIONSHIP_DEF,
            "86b176a2-015c-44a6-8106-54d5d69ba661",
            ATTRIBUTE_FOR_SCHEMA,
            1L
    );

    public static final InstanceType NESTED_SCHEMA_ATTRIBUTE_TYPE = new InstanceType(
            TypeDefCategory.RELATIONSHIP_DEF,
            "0ffb9d87-7074-45da-a9b0-ae0859611133",
            NESTED_SCHEMA_ATTRIBUTE,
            1L
    );

    // classifications and types
    public static final String TYPE_EMBEDDED_ATTRIBUTE = "TypeEmbeddedAttribute";

    public static final String CALCULATED_VALUE = "CalculatedValue";

    public static final InstanceType TYPE_EMBEDDED_ATTRIBUTE_TYPE = new InstanceType(
            TypeDefCategory.RELATIONSHIP_DEF,
            "e2bb76bb-774a-43ff-9045-3a05f663d5d9",
            TYPE_EMBEDDED_ATTRIBUTE,
            1L
    );

    public static final InstanceType CALCULATED_VALUE_TYPE = new InstanceType(
            TypeDefCategory.RELATIONSHIP_DEF,
            "4814bec8-482d-463d-8376-160b0358e139",
            CALCULATED_VALUE,
            1L
    );





    public static final List<String> supportedTypeNames = Arrays.asList(new String[]{
            // entity types
            "Asset", // super type of Database
            "Referenceable", // super type of the others
            "OpenMetadataRoot", // super type of referenceable
            "SchemaAttribute",
            "SchemaElement",
            "ComplexSchemaType",
            "SchemaType",

            CONNECTION,
            CONNECTOR_TYPE,
            ENDPOINT,
            RELATIONAL_TABLE_TYPE,
            RELATIONAL_COLUMN_TYPE,
            DATABASE,
            DEPLOYED_DATABASE_SCHEMA,
            RELATIONAL_DB_SCHEMA_TYPE,
            TABLE,
            COLUMN,
            // relationship types
            CONNECTION_ENDPOINT,
            CONNECTION_CONNECTOR_TYPE,
            CONNECTION_TO_ASSET,
            ASSET_SCHEMA_TYPE,
            ATTRIBUTE_FOR_SCHEMA,
            NESTED_SCHEMA_ATTRIBUTE,
            DATA_CONTENT_FOR_DATASET,
            SCHEMA_ATTRIBUTE_TYPE,
            // classification types
            TYPE_EMBEDDED_ATTRIBUTE,
            CALCULATED_VALUE
    });


    public static final String SOURCE_NAME = "Egeria HMS listener";
}
