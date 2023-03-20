package org.apache.hive.metastore.listener.beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class OriginatorTest {

    @Test
    void getMetadataCollectionId() {

        Originator originator = OriginatorHelper.getOriginator();
        assertEquals( "d81b0efd-b2b9-45de-a755-cfd9ce482c65", originator.getMetadataCollectionId());

    }

    @Test
    void getServerName() {

        Originator originator = OriginatorHelper.getOriginator();
        assertEquals( "danielIBM", originator.getServerName());

    }

    @Test
    void getServerType() {

        Originator originator = OriginatorHelper.getOriginator();
        assertEquals( "Repository Proxy", originator.getServerType());

    }

    @Nested
    class BuilderTest {

        @Test
        void setMetadataCollectionId() {
            Originator originator = OriginatorHelper.getOriginator();
            assertEquals( "d81b0efd-b2b9-45de-a755-cfd9ce482c65", originator.getMetadataCollectionId());
        }

        @Test
        void setServerName() {
            Originator originator = OriginatorHelper.getOriginator();
            assertEquals( "danielIBM", originator.getServerName());

        }

        @Test
        void setServerType() {

            Originator originator = OriginatorHelper.getOriginator();
            assertEquals( "Repository Proxy", originator.getServerType());

        }

        @Test
        void createOriginator () {

            String originalJSON = "{\"metadataCollectionId\":\"d81b0efd-b2b9-45de-a755-cfd9ce482c65\"," +
                                    "\"serverName\":\"danielIBM\"," +
                                    "\"serverType\":\"Repository Proxy\"},\n";

            ObjectMapper mapper = new ObjectMapper();
            Originator originator = OriginatorHelper.getOriginator();
            try {
                String currentJSON = mapper.writeValueAsString(originator);
                assertEquals( mapper.readTree( originalJSON ), mapper.readTree(currentJSON));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }
}