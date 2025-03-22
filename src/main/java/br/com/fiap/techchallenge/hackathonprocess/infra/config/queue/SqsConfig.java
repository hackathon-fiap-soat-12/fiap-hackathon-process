package br.com.fiap.techchallenge.hackathonprocess.infra.config.queue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

@Configuration
public class SqsConfig {

    @Value("${sqs.queue.url:default}")
    private String urlSqs;

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                 .endpointOverride(URI.create(urlSqs))
                .region(Region.US_EAST_1)
                 .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}