package com.example.myservice.pubsub.google;

import com.example.myservice.pubsub.google.config.PubsubProperties;
import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.SubscriptionAdminSettings;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminSettings;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PushConfig;
import org.springframework.stereotype.Service;

@Service
public class PubSubAdminService {

    private final String projectId;
    private final CredentialsProvider credentialsProvider;

    public PubSubAdminService(PubsubProperties props) throws Exception {
        this.projectId = props.getProjectId();
        this.credentialsProvider = PubSubCredentialHelper.buildCredentialsProvider(props.getCredentials());
    }

    // ================== TOPIC ==================

    /** Tạo topic nếu chưa tồn tại */
    public void createTopic(String topicId) throws Exception {
        ProjectTopicName topicName = ProjectTopicName.of(projectId, topicId);
        try (TopicAdminClient topicAdminClient = TopicAdminClient.create(
                TopicAdminSettings.newBuilder().setCredentialsProvider(credentialsProvider).build())) {
            topicAdminClient.createTopic(topicName);
            System.out.println("Topic created: " + topicId);
        } catch (Exception e) {
            if (e.getMessage().contains("ALREADY_EXISTS")) {
                System.out.println("Topic already exists: " + topicId);
            } else {
                throw e;
            }
        }
    }

    /** Xóa topic */
    public void deleteTopic(String topicId) throws Exception {
        ProjectTopicName topicName = ProjectTopicName.of(projectId, topicId);
        try (TopicAdminClient topicAdminClient = TopicAdminClient.create(
                TopicAdminSettings.newBuilder().setCredentialsProvider(credentialsProvider).build())) {
            topicAdminClient.deleteTopic(topicName);
            System.out.println("Topic deleted: " + topicId);
        }
    }

    // ================== SUBSCRIPTION ==================

    /** Tạo subscription (Pull) nếu chưa tồn tại */
    public void createSubscription(String subscriptionId, String topicId) throws Exception {
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);
        ProjectTopicName topicName = ProjectTopicName.of(projectId, topicId);

        try (SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create(
                SubscriptionAdminSettings.newBuilder().setCredentialsProvider(credentialsProvider).build())) {
            subscriptionAdminClient.createSubscription(
                    subscriptionName,
                    topicName,
                    PushConfig.getDefaultInstance(),
                    10 // ackDeadlineSeconds
            );
            System.out.println("Subscription created: " + subscriptionId);
        } catch (Exception e) {
            if (e.getMessage().contains("ALREADY_EXISTS")) {
                System.out.println("Subscription already exists: " + subscriptionId);
            } else {
                throw e;
            }
        }
    }

    /** Xóa subscription */
    public void deleteSubscription(String subscriptionId) throws Exception {
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);
        try (SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create(
                SubscriptionAdminSettings.newBuilder().setCredentialsProvider(credentialsProvider).build())) {
            subscriptionAdminClient.deleteSubscription(subscriptionName);
            System.out.println("Subscription deleted: " + subscriptionId);
        }
    }
}
