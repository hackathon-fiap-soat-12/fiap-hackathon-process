package br.com.fiap.techchallenge.hackathonprocess.infra.gateway.filestorage.impl;

import br.com.fiap.techchallenge.hackathonprocess.application.exceptions.DoesNotExistException;
import br.com.fiap.techchallenge.hackathonprocess.infra.gateway.filestorage.FileService;
import io.awspring.cloud.s3.S3Template;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import static br.com.fiap.techchallenge.hackathonprocess.domain.constants.Constants.BUCKET_NAME_BREADCRUMB;
import static br.com.fiap.techchallenge.hackathonprocess.domain.constants.Constants.EMPTY_FOLDER;

@Service
public class FileServiceS3Impl implements FileService {

    private final S3Template s3Template;
    private final S3Client s3Client;

    public FileServiceS3Impl(S3Template s3Template, S3Client s3Client) {
        this.s3Template = s3Template;
        this.s3Client = s3Client;
    }

    @Override
    public InputStream getFile(String bucketName, String key) {
        try {
            return s3Template.download(bucketName, key.replace(BUCKET_NAME_BREADCRUMB, EMPTY_FOLDER)).getInputStream();
        } catch (IOException e) {
            throw new DoesNotExistException("File not found");
        }
    }

    @Override
    public Boolean uploadFile(String bucketName, String key, InputStream file) {
        var uploaded = s3Template.upload(bucketName, key, file);
        return uploaded.exists();
    }

    @Override
    public Long getSize(String bucketName, String key) {
        var headRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        HeadObjectResponse headResponse = s3Client.headObject(headRequest);
        return headResponse.contentLength();
    }
}