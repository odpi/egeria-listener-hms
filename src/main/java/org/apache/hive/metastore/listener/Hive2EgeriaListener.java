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

    /*
    property values
     */
    private static final String ON = "on";
    private static final String OFF = "off";

    /*
    property names
     */
    private static final String PROPOGATE_EXCEPTIONS = "org.apache.hive.metastore.listener.EgeriaListener.events.exceptions";
    private static final String HOT_SWAP = "org.apache.hive.metastore.listener.EgeriaListener.events.active.hotswap";
    private static final String ACTIVE = "org.apache.hive.metastore.listener.EgeriaListener.events.active";
    private static final String MAPPER = "org.apache.hive.metastore.listener.EgeriaListener.events.mapper";
    private static final String NATIVE = "NATIVE";

    public Hive2EgeriaListener(Configuration config) {
        super(config);

        if(logger.isDebugEnabled())
            logger.debug("Hive2EgeriaListener");
    }

    /**
     * @param dbEvent database event
     * @throws MetaException
     */
    @Override
    public void onCreateDatabase(CreateDatabaseEvent dbEvent) throws MetaException {
        super.onCreateDatabase(dbEvent);

        if(logger.isDebugEnabled())
            logger.debug(String.format("onCreateDatabase %n%s", dbEvent.toString()));

        if( getConf().get(HOT_SWAP, OFF).equalsIgnoreCase(ON) ) {
            getConf().reloadConfiguration();
        }

        if( getConf().get(ACTIVE,OFF).equalsIgnoreCase(ON)) {

            if(getConf().get(MAPPER, NATIVE).equalsIgnoreCase(NATIVE)) {


                postEvent(dbEvent.getDatabase());
            }
        }


    }


    /**
     * debugging when this event is triggered
     * and if we can use them for hot swapping
     * @param tableEvent table event.
     * @throws MetaException
     */
    @Override
    public void onConfigChange(ConfigChangeEvent tableEvent) throws MetaException {
        if(logger.isDebugEnabled()) {
            logger.debug(String.format("onConfigChange : %s ", tableEvent.toString()));
        }
        super.onConfigChange(tableEvent);
    }


    /**
     *
     * @param msg       the text to be displayed in the error log
     * @param ex        the original exception
     * @throws MetaException
     */
    private void handleException( String msg, Exception ex) throws MetaException {

        if( logger.isDebugEnabled()) {
            logger.debug( String.format("handleException %n%s%n%s",msg,ex.getMessage()));
        }
        /*
        never hide the fact that exceptions are being thrown .
        just decide if we let HMS know
         */
        logger.error(msg, ex);
        if( getConf().get(HOT_SWAP, OFF).equalsIgnoreCase(ON)) {
            getConf().reloadConfiguration();
        }
        if( getConf().get(PROPOGATE_EXCEPTIONS,OFF).equalsIgnoreCase(ON)) {
            throw new MetaException( ex.getMessage() );
        }

    }


    /**
     *
     * @param event       the bean transformed into the JSON to be posted to kafka

     * @throws MetaException
     */
    private void postEvent( Object event) throws MetaException {

        if( logger.isDebugEnabled()) {
            logger.debug( String.format("postEvent %n%s", event.toString()));
        }

        ObjectMapper objMapper = new ObjectMapper();
        try {
            String json = objMapper.writeValueAsString(event);

            /*
            posting to kafka to be added later
             */

            System.out.println( String.format("Event %n%s",json) );

        } catch (Exception ex) {
            handleException("An Exception was thrown mapping an event to json", ex);
        }

    }

}
