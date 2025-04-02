package br.com.fiap.techchallenge.hackathonprocess.infra.config.filestorage;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.s3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class AwsS3Config {

    @Value("${aws.url:default}")
    private String awsUrl;

    @Value("${aws.access-key-id:default}")
    private String accessKeyId;

    @Value("${aws.secret-access-key:default}")
    private String secretAccessKey;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(URI.create(awsUrl))
                .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKeyId, secretAccessKey)
                        )
                )
                .build();
    }

    @Bean
    public S3OutputStreamProvider s3OutputStreamProvider(S3Client s3Client) {
        return new InMemoryBufferingS3OutputStreamProvider(s3Client, new PropertiesS3ObjectContentTypeResolver());
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(URI.create(awsUrl))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKeyId, secretAccessKey)
                        )
                )
                .build();
    }

    @Bean
    public S3Template s3Template(S3Client s3Client,
                                 S3OutputStreamProvider s3OutputStreamProvider,
                                 S3ObjectConverter s3ObjectConverter,
                                 S3Presigner s3Presigner) {
        return new S3Template(s3Client, s3OutputStreamProvider, new Jackson2JsonS3ObjectConverter(new ObjectMapper()), s3Presigner);
    }
}