package com.example.myservice.pubsub.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    @KafkaListener(topics = "message-chat", groupId = "my-consumer-group")
    public void listen(String message) {
        System.out.println("📩 Received: " + message);
    }
}
