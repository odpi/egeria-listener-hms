package org.apache.hive.metastore.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.metastore.MetaStoreEventListener;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;


public class Hive2EgeriaListener extends MetaStoreEventListener {

    private static final Logger logger = LoggerFactory.getLogger(Hive2EgeriaListener.class);

    public Hive2EgeriaListener(Configuration config) {
        super(config);

        if(logger.isDebugEnabled())
            logger.debug("Hive2EgeriaListener");
    }

    /**
     * @param tableEvent table event.
     * @throws MetaException
     */
    @Override
    public void onConfigChange(ConfigChangeEvent tableEvent) throws MetaException {
        super.onConfigChange(tableEvent);
        if(logger.isDebugEnabled())
            logger.debug(String.format("onConfigChange : %s ", tableEvent.toString()));
    }

}
