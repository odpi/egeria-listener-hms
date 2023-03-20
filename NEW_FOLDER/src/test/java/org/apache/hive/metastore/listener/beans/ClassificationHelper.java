package org.apache.hive.metastore.listener.beans;

import java.time.Instant;

public class ClassificationHelper {

    private Long createTime;

    public Classification getClassification() {
        createTime = Instant.now().toEpochMilli();
        Classification classification = new Classification.Builder()
                .setCreatedBy("Tests")
                .setCreateTime(createTime)
                .setHeaderVersion(1l)
                .setInstanceProvenanceType("LOCAL_COHORT")
                .setName("TestClassification")
                .setStatus("ACTIVE")
                .setVersion(1l)
                .createClassification();

        return classification;
    }

    public Long getCreateTime() {
        return createTime;
    }
}
