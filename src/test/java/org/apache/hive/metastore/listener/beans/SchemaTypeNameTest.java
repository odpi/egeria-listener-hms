package org.apache.hive.metastore.listener.beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SchemaTypeNameTest {

    @Test
    void getName() {
        SchemaTypeName schemaTypeName = SchemaTypeNameHelper.getSchemaTypeName();

        ObjectMapper mapper = new ObjectMapper();

        try {
            assertEquals(mapper.writeValueAsString(schemaTypeName.getSchemaTypeName()),
                    mapper.writeValueAsString(NameHelper.getName()));

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Nested

    class BuilderTest {
        @Test
        void setName() {

        }

        @Test
        void createSchemaTypeName() {

            String originalJSON = "{\"schemaTypeName\":" +
                    "{\"class\":\"PrimitivePropertyValue\"," +
                    "\"headerVersion\":0," +
                    "\"instancePropertyCategory\":\"PRIMITIVE\"," +
                    "\"typeGUID\":\"b34a64b9-554a-42b1-8f8a-7d5c2339f9c4\"," +
                    "\"typeName\":\"string\"," +
                    "\"primitiveDefCategory\":\"OM_PRIMITIVE_TYPE_STRING\"," +
                    "\"primitiveValue\":\"RelationalTableType\"}}";


            ObjectMapper mapper = new ObjectMapper();
            SchemaTypeName name = SchemaTypeNameHelper.getSchemaTypeName();

            try {
                String currentJSON = mapper.writeValueAsString(name);
                assertEquals( mapper.readTree(originalJSON), mapper.readTree(currentJSON));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}