package org.apache.hive.metastore.listener.beans;

public class EntityDeleteEvent<Originator> {

    private Long eventTimestamp;
    private Originator originator;
    private String Version;


}
