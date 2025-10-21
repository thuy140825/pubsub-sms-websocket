package com.example.myservice.pubsub.google.publisher;

import com.example.myservice.pubsub.google.PubSubCredentialHelper;
import com.example.myservice.pubsub.google.config.PubsubProperties;
import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import org.springframework.stereotype.Service;

@Service
public class PubSubPublisherService {

    private final String projectId;
    private final CredentialsProvider credentialsProvider;

    public PubSubPublisherService(PubsubProperties props) throws Exception {
        this.projectId = props.getProjectId();
        this.credentialsProvider = PubSubCredentialHelper.buildCredentialsProvider(props.getCredentials());
    }

    public void publish(String topicId, String message) throws Exception {
        ProjectTopicName topicName = ProjectTopicName.of(projectId, topicId);

        Publisher publisher = Publisher.newBuilder(topicName)
                .setCredentialsProvider(credentialsProvider)
                .build();

        PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                .setData(ByteString.copyFromUtf8(message))
                .build();

        publisher.publish(pubsubMessage).get();
        publisher.shutdown();

        System.out.println("Published message: " + message);
    }
}

