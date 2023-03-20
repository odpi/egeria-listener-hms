package org.apache.hive.metastore.listener.beans;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class ClassificationTest extends BaseEgeriaType {

    @Test
    void getCreateTime() {

        ClassificationHelper  helper = new ClassificationHelper();
        Classification classification = helper.getClassification();
        assertEquals(classification.getCreateTime(), helper.getCreateTime());
    }

    @Test
    void getCreatedBy() {

        ClassificationHelper  helper = new ClassificationHelper();
        Classification classification = helper.getClassification();
        assertEquals("Tests",classification.getCreatedBy());
    }

    @Test
    void getHeaderVersion() {

        ClassificationHelper  helper = new ClassificationHelper();
        Classification classification = helper.getClassification();
        assertEquals(1,classification.getHeaderVersion());

    }

    @Test
    void getInstanceProvenanceType() {
    }

    @Test
    void getName() {

        ClassificationHelper  helper = new ClassificationHelper();
        Classification classification = helper.getClassification();
        assertEquals("TestClassification",classification.getName());

    }


    @Test
    void getProperties() {
    }

    @Test
    void getStatus() {

        ClassificationHelper  helper = new ClassificationHelper();
        Classification classification = helper.getClassification();
        assertEquals("ACTIVE",classification.getStatus());

    }

    @Test
    void getType() {
    }

    @Test
    void getVersion() {
        ClassificationHelper  helper = new ClassificationHelper();
        Classification classification = helper.getClassification();
        assertEquals(1,classification.getVersion());

    }

    public static class BuilderTests {

        @Test
        void setCreateTime() {
            ClassificationHelper  helper = new ClassificationHelper();
            Classification classification = helper.getClassification();
            assertEquals(classification.getCreateTime(), helper.getCreateTime());
        }

        @Test
        void setCreatedBy() {
            ClassificationHelper  helper = new ClassificationHelper();
            Classification classification = helper.getClassification();
            assertEquals("Tests",classification.getCreatedBy());
        }

        @Test
        void setHeaderVersion() {
            ClassificationHelper  helper = new ClassificationHelper();
            Classification classification = helper.getClassification();
            assertEquals(1,classification.getHeaderVersion());
        }

        @Test
        void setInstanceProvenanceType() {
        }

        @Test
        void setName() {
            ClassificationHelper  helper = new ClassificationHelper();
            Classification classification = helper.getClassification();
            assertEquals("TestClassification",classification.getName());
        }

        @Test
        void setProperties() {
        }

        @Test
        void setStatus() {

            ClassificationHelper  helper = new ClassificationHelper();
            Classification classification = helper.getClassification();
            assertEquals("ACTIVE",classification.getStatus());
        }

        @Test
        void setType() {
        }

        @Test
        void setVersion() {
            ClassificationHelper  helper = new ClassificationHelper();
            Classification classification = helper.getClassification();
            assertEquals(1,classification.getVersion());
        }

        }

        @Test
        void createClassification() {

        }
    }
