package com.example.myservice.pubsub.google.consumer;

import com.example.myservice.pubsub.google.PubSubCredentialHelper;
import com.example.myservice.pubsub.google.config.PubsubProperties;
import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import org.springframework.stereotype.Service;

@Service
public class PubSubSubscriberService {

    private final String projectId;
    private final CredentialsProvider credentialsProvider;

    public PubSubSubscriberService(PubsubProperties props) throws Exception {
        this.projectId = props.getProjectId();
        this.credentialsProvider = PubSubCredentialHelper.buildCredentialsProvider(props.getCredentials());
    }

    public void subscribe(String subscriptionId) {
        ProjectSubscriptionName subscriptionName =
                ProjectSubscriptionName.of(projectId, subscriptionId);

        Subscriber subscriber = Subscriber.newBuilder(subscriptionName, (PubsubMessage message, AckReplyConsumer consumer) -> {
                    System.out.println("Received message ID: " + message.getMessageId());
                    System.out.println("Payload: " + message.getData().toStringUtf8());
                    consumer.ack();
                })
                .setCredentialsProvider(credentialsProvider)
                .build();

        subscriber.startAsync().awaitRunning();
        System.out.println("Listening for messages on subscription: " + subscriptionId);
    }
}
