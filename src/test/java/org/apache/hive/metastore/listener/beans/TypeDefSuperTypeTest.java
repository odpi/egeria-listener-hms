package org.apache.hive.metastore.listener.beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypeDefSuperTypeTest {

    @Test
    void getGuid() {
        TypeDefSuperType type = TypeDefSuperTypeHelper.getTypeDefSuperType();
        assertEquals( "1a5e159b-913a-43b1-95fe-04433b25fca9",type.getGuid());
    }

    @Test
    void getHeaderVersion() {
        TypeDefSuperType type = TypeDefSuperTypeHelper.getTypeDefSuperType();
        assertEquals( 1L,type.getHeaderVersion());
    }

    @Test
    void getName() {
        TypeDefSuperType type = TypeDefSuperTypeHelper.getTypeDefSuperType();
        assertEquals( "SchemaAttribute",type.getName());
    }

    @Test
    void getStatus() {

        TypeDefSuperType type = TypeDefSuperTypeHelper.getTypeDefSuperType();
        assertEquals( "ACTIVE_TYPEDEF",type.getStatus());
    }

    @Nested
    public class BuilderTest {


        @Test
        void setGuid() {
            TypeDefSuperType type = TypeDefSuperTypeHelper.getTypeDefSuperType();
            assertEquals( "1a5e159b-913a-43b1-95fe-04433b25fca9",type.getGuid());
        }

        @Test
        void setHeaderVersion() {
            TypeDefSuperType type = TypeDefSuperTypeHelper.getTypeDefSuperType();
            assertEquals( 1L,type.getHeaderVersion());
        }

        @Test
        void setName() {
            TypeDefSuperType type = TypeDefSuperTypeHelper.getTypeDefSuperType();
            assertEquals( "SchemaAttribute",type.getName());
        }

        @Test
        void setStatus() {
            TypeDefSuperType type = TypeDefSuperTypeHelper.getTypeDefSuperType();
            assertEquals( "ACTIVE_TYPEDEF",type.getStatus());
        }

        @Test
        void createTypeDefSupperType (){

            String originalJSON = "{\"headerVersion\":1,\"guid\":\"1a5e159b-913a-43b1-95fe-04433b25fca9\",\"name\":\"SchemaAttribute\",\"status\":\"ACTIVE_TYPEDEF\"}";
            TypeDefSuperType type = TypeDefSuperTypeHelper.getTypeDefSuperType();
            ObjectMapper mapper = new ObjectMapper();
            try {
                String currentJSON = mapper.writeValueAsString(type);
                assertEquals( mapper.readTree(originalJSON), mapper.readTree(currentJSON));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }
    }

}