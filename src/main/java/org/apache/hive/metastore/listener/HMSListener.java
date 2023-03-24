package org.apache.hive.metastore.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.metastore.MetaStoreEventListener;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.events.AlterTableEvent;
import org.apache.hadoop.hive.metastore.events.ConfigChangeEvent;
import org.apache.hadoop.hive.metastore.events.CreateTableEvent;
import org.apache.hadoop.hive.metastore.events.DropTableEvent;
import org.odpi.openmetadata.hmslistener.kafka.KafkaClient;
import org.odpi.openmetadata.hmslistener.mapper.HMSToOMRSInstanceEventMapper;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class HMSListener extends MetaStoreEventListener {

    private static final Logger logger = LoggerFactory.getLogger(HMSListener.class);

    /*
    property values
     */
    private static final String ON = "on";
    private static final String OFF = "off";

    /*
    property names
     */
    public static final String CONFIG_PROPOGATE_EXCEPTIONS = "EgeriaListener.events.exceptions";
    private static final String HOT_SWAP = "EgeriaListener.events.active.hotswap";
    public static final String CONFIG_ACTIVE_FLAG = "EgeriaListener.events.active";

    // Egeria config
    public static final String CONFIG_METADATA_COLLECTION_ID = "EgeriaListener.metadataCollectionId";
    public static final String CONFIG_SERVER_NAME = "EgeriaListener.serverName";
    public static final String CONFIG_ORGANISATION_NAME = "EgeriaListener.organisationName";

    public static final String CONFIG_QUALIFIEDNAME_ABOVE_TABLE = "EgeriaListener.qualifiedNameAboveTable";

    public static final String CONFIG_CATALOG = "EgeriaListener.catalogName";

    public static final String CONFIG_DB = "EgeriaListener.databaseName";

    // Kafka config
    public static final String CONFIG_KAFKA_TOPIC_NAME = "EgeriaListener.events.kafka.topicname";
    public static final String CONFIG_KAFKA_CLIENT_ID = "EgeriaListener.events.kafka.clientId";
    public static final String CONFIG_KAFKA_BOOTSTRAP_SERVER_URL = "EgeriaListener.events.kafka.bootstrapServerurl";


    HMSToOMRSInstanceEventMapper hmsToOMRSInstanceEventMapper = null;

    // listener state
    private boolean issueEventRequired = false;
    private String topicName = null;
    private String clientId = null;
    private String bootstrapServerURL = null;

    private String metadatadCollectionId = null;
    private String serverName = null;
    private String qualifiedNameAboveTable = null;
    private String organisationName = null;

    private KafkaClient kafkaClient= null;


    public HMSListener(Configuration config) {
        super(config);

        if(logger.isDebugEnabled()) {
            logger.debug("HiveListener constructor");
        }
        if (config.get(CONFIG_ACTIVE_FLAG, OFF).equalsIgnoreCase(ON)) {
            issueEventRequired = true;
        }
        initialiseKafkaClientFromConfig(config);
        initialiseHmstoOMRSInstanceMapperFromConfig(config);
    }

    private void initialiseHmstoOMRSInstanceMapperFromConfig(Configuration config) {
        metadatadCollectionId = config.get(CONFIG_METADATA_COLLECTION_ID, "");
        serverName = config.get(CONFIG_SERVER_NAME, "");
        qualifiedNameAboveTable = config.get(CONFIG_QUALIFIEDNAME_ABOVE_TABLE, "");
        organisationName = config.get(CONFIG_ORGANISATION_NAME, "LF");

        hmsToOMRSInstanceEventMapper = new HMSToOMRSInstanceEventMapper(
                metadatadCollectionId,
                serverName,
                "Repository Proxy",
                organisationName,
                qualifiedNameAboveTable);
    }

    private void initialiseKafkaClientFromConfig(Configuration config) {
        topicName = config.get(CONFIG_KAFKA_TOPIC_NAME, "");

        bootstrapServerURL = config.get(CONFIG_KAFKA_BOOTSTRAP_SERVER_URL, "");

        clientId =  config.get(CONFIG_KAFKA_CLIENT_ID, "");
        // TODO error checking & logging
        kafkaClient = new KafkaClient(clientId, bootstrapServerURL, topicName);
    }

    @Override
    public void onCreateTable (CreateTableEvent tableEvent) throws MetaException {

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("==> onCreateTable %n%s", tableEvent.toString()));
        }

        if (issueEventRequired) {
            // issue the event

            List<OMRSInstanceEvent> events = hmsToOMRSInstanceEventMapper.getEventsForCreateTable(tableEvent);

            for ( OMRSInstanceEvent event:events) {
                ObjectMapper om = new ObjectMapper();
                try {
                    String eventStr = om.writeValueAsString(event);
                    kafkaClient.sendEvent(eventStr);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("<== onCreateTable");
        }
    }

    /**
     * @param tableEvent table event.
     * @throws MetaException
     */
    @Override
    public void onDropTable (DropTableEvent tableEvent)  throws MetaException {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("==> onDropTable %n%s", tableEvent.toString()));
        }

        if (issueEventRequired) {
            // issue the event

            List<OMRSInstanceEvent> events =  hmsToOMRSInstanceEventMapper.getEventsForDropTable(tableEvent) ;

            for ( OMRSInstanceEvent event:events) {
                ObjectMapper om = new ObjectMapper();
                try {
                    String eventStr = om.writeValueAsString(event);
                    kafkaClient.sendEvent(eventStr);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("<== onDropTable");
        }

    }

    /**
     * @param tableEvent alter table event
     * @throws MetaException
     */
    @Override
    public void onAlterTable (AlterTableEvent tableEvent) throws MetaException {
    }

    @Override
    public void onConfigChange(ConfigChangeEvent configChangeEvent) throws MetaException {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onConfigChange : %s ", configChangeEvent.toString()));
        }
        String key = configChangeEvent.getKey();
        String value = configChangeEvent.getNewValue();
        if (key.equalsIgnoreCase(CONFIG_ACTIVE_FLAG)) {
            if (value.equalsIgnoreCase(ON)) {
                issueEventRequired = true;
            } else {
                issueEventRequired = false;
            }
        }
        if (key.equalsIgnoreCase(CONFIG_METADATA_COLLECTION_ID) ||
                key.equalsIgnoreCase(CONFIG_SERVER_NAME) ||
                key.equalsIgnoreCase(CONFIG_QUALIFIEDNAME_ABOVE_TABLE) ||
                key.equalsIgnoreCase(CONFIG_ORGANISATION_NAME)
        ) {
            getConf().reloadConfiguration();
            initialiseHmstoOMRSInstanceMapperFromConfig(getConf());
        }
        if (key.equalsIgnoreCase(CONFIG_KAFKA_CLIENT_ID) ||
                key.equalsIgnoreCase(CONFIG_KAFKA_BOOTSTRAP_SERVER_URL) ||
                key.equalsIgnoreCase(CONFIG_KAFKA_TOPIC_NAME)
        ) {
            getConf().reloadConfiguration();
            initialiseKafkaClientFromConfig(getConf());
        }
    }

//    /**
//     *
//     * @param msg       the text to be displayed in the error log
//     * @param ex        the original exception
//     * @throws MetaException
//     */
//    private void handleException( String msg, Exception ex) throws MetaException {
//
//        if( logger.isDebugEnabled()) {
//            logger.debug( String.format("handleException %n%s%n%s",msg,ex.getMessage()));
//        }
//        /*
//        never hide the fact that exceptions are being thrown .
//        just decide if we let HMS know
//         */
//        logger.error(msg, ex);
//        if( getConf().get(HOT_SWAP, OFF).equalsIgnoreCase(ON)) {
//            getConf().reloadConfiguration();
//        }
//        if( getConf().get(CONFIG_PROPOGATE_EXCEPTIONS,OFF).equalsIgnoreCase(ON)) {
//            throw new MetaException( ex.getMessage() );
//        }
//
//
//    }


//    /**
//     *
//     * @param event       the bean transformed into the JSON to be posted to kafka
//
//     * @throws MetaException
//     */
//    private void postEvent( Object event) throws MetaException {
//
//        if( logger.isDebugEnabled()) {
//            logger.debug( String.format("postEvent %n%s", event.toString()));
//        }
//
//        ObjectMapper objMapper = new ObjectMapper();
//        try {
//            String json = objMapper.writeValueAsString(event);
//
//            /*
//            posting to kafka to be added later
//             */
//
//            System.out.printf("Event %n%s%n",json);
//
//        } catch (Exception ex) {
//            handleException("An Exception was thrown mapping an event to json", ex);
//        }
//
//    }

}
