package com.example.myservice.pubsub.google;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class PubSubCredentialHelper {

    public static CredentialsProvider buildCredentialsProvider(String credentialJson) throws Exception {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new ByteArrayInputStream(credentialJson.getBytes(StandardCharsets.UTF_8))
        );
        return FixedCredentialsProvider.create(credentials);
    }
}
