package org.apache.hive.metastore.listener;

import org.apache.hadoop.conf.Configuration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Hive2EgeriaListenerTest {

    @Test
    void onConfigChange() {
        Configuration conf = new Configuration();
        conf.set("org.open.events.endpoint", "https://localhost:9443");
        Hive2EgeriaListener listener = new Hive2EgeriaListener(conf);
        assert(listener.getConf().get("org.open.events.endpoint").equals("https://localhost:9443"));
    }

    @Test
    void onCreateTable() {
    }

    @Test
    void onDropTable() {
    }

    @Test
    void onAlterTable() {
    }

    @Test
    void onCreateDatabase() {
    }

    @Test
    void onDropDatabase() {
    }

    @Test
    void onAlterDatabase() {
    }

    @Test
    void getConf() {
    }

    @Test
    void setConf() {
    }
}