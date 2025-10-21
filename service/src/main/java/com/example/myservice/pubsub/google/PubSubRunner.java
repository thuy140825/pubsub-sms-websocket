package com.example.myservice.pubsub.google;

import com.example.myservice.pubsub.google.consumer.PubSubSubscriberService;
import com.example.myservice.pubsub.google.publisher.PubSubPublisherService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class PubSubRunner {

    private final PubSubPublisherService publisherService;
    private final PubSubSubscriberService subscriberService;
    private final PubSubAdminService adminService;

    public PubSubRunner(PubSubPublisherService publisherService,
                        PubSubSubscriberService subscriberService, PubSubAdminService adminService) {
        this.publisherService = publisherService;
        this.subscriberService = subscriberService;
        this.adminService = adminService;
    }

    @PostConstruct
    public void runTest() throws Exception {
        String topicId = "test-message";                // đã tạo sẵn trong GCP
        String subscriptionId = "test-message-1";       // đã tạo sẵn và gắn với topic

        // Đảm bảo topic & subscription tồn tại
        adminService.createTopic(topicId);
        adminService.createSubscription(subscriptionId, topicId);

        // Start subscriber
        new Thread(() -> {
            try {
                subscriberService.subscribe(subscriptionId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Publish message
        publisherService.publish(topicId, "Hello PubSub from Spring Boot!");
    }
}
