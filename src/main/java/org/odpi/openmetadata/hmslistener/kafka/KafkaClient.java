/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.hmslistener.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * This is a JAVA application to help to drive the connector for development testing purposes. It publishes events
 * to the local Kafka on the specified topic.
 *
 * Prior to running this application, you should have:
 * - a running Egeria OMAG platform with a metadata server and the lineage connector jar file present.
 * - the metadata server and lineage connector should be configured
 * - the topic referred to in the lineage connector configuration should have been created on the appropriate kafka.
 *
 * Run this application, you will be asked for topic name to write to, press enter if you want to use "legacyLineage"
 * as the topic name.
 * A list of files containing event payloads will be shown. Running them in order (sample1, sample2 etc...) will drive
 * the connector to issue some creations, updates and deletes.
 * Then copy and paste the json file you want to sent as an event to the topic. The application will continue to offer
 * inputs, in which you can continue to paste file names. Enter 'q' to quit.
 *
 * Testing of thi app, have been performed under Intellij IDE. Changes will be required if you wnt to run this application
 * from a jar file.
 *
 */
public class KafkaClient   {

    private String topicName;
    private String clientId;
    private  String bootstrapServerURL;
    private KafkaProducer producer;
    private int messageNoCount = 0;

    public KafkaClient(String clientId, String bootstrapServerURL, String topicName) {
        this.clientId = clientId;
        this.bootstrapServerURL = bootstrapServerURL;
        this.topicName = topicName;

        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServerURL);
        properties.put("client.id", clientId);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(properties);
    }


    public void sendEvent(String messageStr) {
        try {
            // send is async
            producer.send(new ProducerRecord<>(topicName,
                    messageNoCount++,
                    messageStr)).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
