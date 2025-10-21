package com.example.myservice.sms.controller;

import com.example.myservice.pubsub.kafka.MessageProducer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageProducer producer;

    public MessageController(MessageProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public String sendMessage(@RequestParam String msg) {
        producer.sendMessage(msg);
        return "Message sent: " + msg;
    }
}
