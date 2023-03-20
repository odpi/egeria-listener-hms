package org.apache.hive.metastore.listener.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.codehaus.jackson.JsonProcessingException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class NameTest extends BaseEgeriaType {

    @Test
    void getHeaderVersion() {
        Name name = NameHelper.getName();
        assertEquals(0, name.getHeaderVersion());
    }

    @Test
    void getInstancePropertyCategory() {
        Name name = NameHelper.getName();
        assertEquals("PRIMITIVE", name.getInstancePropertyCategory());
    }

    @Test
    void getPrimitiveDefCategory() {

        Name name = NameHelper.getName();
        assertEquals("OM_PRIMITIVE_TYPE_STRING", name.getPrimitiveDefCategory());
    }

    @Test
    void getPrimitiveValue() {
        Name name = NameHelper.getName();
        assertEquals("RelationalTableType", name.getPrimitiveValue());
    }

    @Test
    void getTypeGUID() {
        Name name = NameHelper.getName();
        assertEquals("b34a64b9-554a-42b1-8f8a-7d5c2339f9c4", name.getTypeGUID());
    }

    @Test
    void getTypeName() {

        Name name = NameHelper.getName();
        assertEquals("string", name.getTypeName());
    }
    @Nested
    public class BuilderTest {


        @Test
        void setHeaderVersion() {
            Name name = NameHelper.getName();
            assertEquals(0, name.getHeaderVersion());
        }

        @Test
        void setInstancePropertyCategory() {
            Name name = NameHelper.getName();
            assertEquals("PRIMITIVE", name.getInstancePropertyCategory());
        }

        @Test
        void setPrimitiveDefCategory() {

            Name name = NameHelper.getName();
            assertEquals("OM_PRIMITIVE_TYPE_STRING", name.getPrimitiveDefCategory());
        }

        @Test
        void setPrimitiveValue() {
            Name name = NameHelper.getName();
            assertEquals("RelationalTableType", name.getPrimitiveValue());
        }

        @Test
        void setTypeGUID() {
            Name name = NameHelper.getName();
            assertEquals("b34a64b9-554a-42b1-8f8a-7d5c2339f9c4", name.getTypeGUID());
        }

        @Test
        void setTypeName() {

            Name name = NameHelper.getName();
            assertEquals("string", name.getTypeName());
        }

        @Test
        void createName()  {

            String originalJSON = "{\"class\": \"PrimitivePropertyValue\"," +
                    "\"headerVersion\": 0," +
                    "\"instancePropertyCategory\": \"PRIMITIVE\"," +
                    "\"typeGUID\": \"b34a64b9-554a-42b1-8f8a-7d5c2339f9c4\"," +
                    "\"typeName\": \"string\"," +
                    "\"primitiveDefCategory\": \"OM_PRIMITIVE_TYPE_STRING\"," +
                    "\"primitiveValue\": \"RelationalTableType\"}";

            ObjectMapper mapper = new ObjectMapper();
            Name name = NameHelper.getName();
            String currentJSON;

            try {
                currentJSON = mapper.writeValueAsString(name);
                assertEquals(mapper.readTree(originalJSON), mapper.readTree(currentJSON));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}