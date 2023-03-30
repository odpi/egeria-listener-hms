package org.apache.hive.metastore.listener;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.metastore.IHMSHandler;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.StorageDescriptor;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.hive.metastore.events.CreateTableEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ListenerTesterApp {
    public static void main(String[] args) throws MetaException {
        Configuration config = new Configuration();
// TODO change these to match your system
        config.set(HMSListener.CONFIG_METADATA_COLLECTION_ID,"metadata collection id");
        config.set(HMSListener.CONFIG_SERVER_NAME, "server1");
        config.set(HMSListener.CONFIG_QUALIFIEDNAME_ABOVE_TABLE, "data-engine::spark.default.deployed-schema.relational-DB-Schema-Type");
       // config.set(HMSListener.CONFIG_ORGANISATION_NAME  ,"Coco");
        config.set(HMSListener.CONFIG_KAFKA_TOPIC_NAME,"egeriaTopics.openmetadata.repositoryservices.cohort.myCohort2.OMRSTopic.instances");
        config.set(HMSListener.CONFIG_KAFKA_BOOTSTRAP_SERVER_URL,"localhost:9092");
        config.set(HMSListener.CONFIG_KAFKA_CLIENT_ID, "4");
        config.set(HMSListener.CONFIG_ACTIVE_FLAG,"ON");

        HMSListener hmsListener = new HMSListener(config);

        Table table = new Table();
        table.setDbName("default");
        table.setCatName("spark");
        table.setTableName("Test1");
        StorageDescriptor sd = new StorageDescriptor();
        List<FieldSchema> cols = new ArrayList<>();
        for (int i=0;i<5;i++) {
            FieldSchema fs = new FieldSchema();
            fs.setName("col" + i);
            fs.setType("string");
            cols.add(fs);
        }
        sd.setCols(cols);
        table.setSd(sd);
        CreateTableEvent tableEvent = getCreateTableEvent(table);
        hmsListener.onCreateTable(tableEvent);

    }

    /**
     * Unfortunately HMS 3.1.3 and HMS 4.0.0 alpha 2 have different constructor parameters for CreateTableEvent, this method uses reflection
     * to try each constructor shape.
     * @param table the table to put in the CreateTableEvent
     * @return CreateTableEvent
     */
    private static CreateTableEvent getCreateTableEvent(Table table) {
        CreateTableEvent tableEvent;
        try {
            Class<?> createTableEventclass = Class.forName("org.apache.hadoop.hive.metastore.events.CreateTableEvent");
            Constructor constructor = null;
            boolean isV4 = false;
            try {
             constructor = createTableEventclass.getDeclaredConstructor(Table.class, boolean.class, IHMSHandler.class, boolean.class);

            } catch (NoSuchMethodException e) {
                    // we expect this if running against hms v3.1.3
            }

            if (isV4) {
                tableEvent = (CreateTableEvent) constructor.newInstance(table, true, null, false);
            } else {
                constructor = createTableEventclass.getDeclaredConstructor(Table.class, boolean.class, IHMSHandler.class);
                tableEvent = (CreateTableEvent) constructor.newInstance(table, true, null);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return tableEvent;
    }

}
