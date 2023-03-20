
package org.apache.hive.metastore.listener.beans;


import java.util.List;

public class Entity extends BaseEgeriaType {

    private List<Classification> classifications;
    private Long createTime;
    private String guid;
    private Long headerVersion;
    private String instanceProvenanceType;
    private String metadataCollectionId;
    private Properties properties;
    private String status;
    private Type type;
    private Long version;
}
