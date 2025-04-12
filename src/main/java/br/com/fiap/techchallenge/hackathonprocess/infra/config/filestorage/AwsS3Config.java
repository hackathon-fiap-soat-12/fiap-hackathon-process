package br.com.fiap.techchallenge.hackathonprocess.infra.config.filestorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.*;

@Configuration
@Profile("!local")
public class AwsS3Config {

    private static final Logger LOGGER = LoggerFactory.getLogger(AwsS3Config.class);

    @Value("${aws.access-key-id}")
    private String accessKeyId;

    @Value("${aws.secret-access-key}")
    private String secretAccessKey;

    @Value("${aws.session-token}")
    private String sessionToken;

    @Bean
    public S3Client s3Client() {
        LOGGER.info("Initializing S3Client with session credentials");
        LOGGER.info("Initializing S3Client accessKeyId {} - secretAccessKey {} - sessionToken {}", accessKeyId, secretAccessKey, sessionToken);

        AwsSessionCredentials credentials = AwsSessionCredentials.create(
                accessKeyId,
                secretAccessKey,
                sessionToken
        );

        return S3Client.builder()
                .region(Region.US_EAST_1)
                .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}