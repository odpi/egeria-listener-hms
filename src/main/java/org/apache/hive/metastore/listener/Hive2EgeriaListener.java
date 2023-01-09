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

    private static final String PROPOGATE_EXCEPTIONS = "org.apache.hive.metastore.listener.EgeriaListener.events.exceptions";

    public Hive2EgeriaListener(Configuration config) {
        super(config);

        if(logger.isDebugEnabled())
            logger.debug("Hive2EgeriaListener");
    }

    /**
     * debugging when this event is triggered
     * and if we can use them for hot swapping
     * @param tableEvent table event.
     * @throws MetaException
     */
    @Override
    public void onConfigChange(ConfigChangeEvent tableEvent) throws MetaException {
        super.onConfigChange(tableEvent);
        if(logger.isDebugEnabled())
            logger.debug(String.format("onConfigChange : %s ", tableEvent.toString()));
    }


    /**
     *
     * @param msg       the text to be displayed in the error log
     * @param ex        the original exception
     * @throws MetaException
     */
    private void handleException( String msg, Exception ex) throws MetaException {
        /*
        never hide the fact that exceptions are being thrown .
        just don't let HMS know
         */
        logger.error(msg, ex);
        if( getConf().get(PROPOGATE_EXCEPTIONS,"off").equalsIgnoreCase("on")) {
            throw new MetaException( ex.getMessage() );
        }

    }

}
