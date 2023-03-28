package org.apache.hive.metastore.listener;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.StorageDescriptor;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.hive.metastore.events.CreateTableEvent;

import java.util.ArrayList;
import java.util.List;

public class ListenerTesterApp {
    public static void main(String[] args) throws MetaException {
        Configuration config = new Configuration();

        config.set(HMSListener.CONFIG_METADATA_COLLECTION_ID,"1");
        config.set(HMSListener.CONFIG_SERVER_NAME, "aaa");
        config.set(HMSListener.CONFIG_QUALIFIEDNAME_ABOVE_TABLE, "todo");
        config.set(HMSListener.CONFIG_ORGANISATION_NAME  ,"Coco");
        config.set(HMSListener.CONFIG_KAFKA_TOPIC_NAME,"topic1");
        config.set(HMSListener.CONFIG_KAFKA_BOOTSTRAP_SERVER_URL,"localhost:9092");
        config.set(HMSListener.CONFIG_KAFKA_CLIENT_ID, "1");
        config.set(HMSListener.CONFIG_ACTIVE_FLAG,"ON");

        HMSListener hmsListener = new HMSListener(config);

        Table table = new Table();
        table.setDbName("db");
        table.setCatName("cat");
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

        //TODO: Signature changed v3 -> v4
        CreateTableEvent tableEvent = new CreateTableEvent(table, true, null, false);
        hmsListener.onCreateTable(tableEvent);





    }

}
