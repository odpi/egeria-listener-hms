package org.odpi.openmetadata.hmslistener.kafka;

import java.util.ArrayList;
import java.util.List;

public class MockKafkaClient extends KafkaClient{
    List<String> messages= null;

    public MockKafkaClient(String clientId, String bootstrapServerURL, String topicName) {
        super(clientId, bootstrapServerURL, topicName);
        messages = new ArrayList<>();
    }
    @Override
    public void sendEvent(String messageStr) {
        messages.add(messageStr);
    }
    public String getMessage() {
        if (messages.size() >0) {
            return messages.remove(0);
        } else {
            return null;
        }
    }
}
