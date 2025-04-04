package br.com.fiap.techchallenge.hackathonprocess.infra.gateway.filestorage.impl;

import br.com.fiap.techchallenge.hackathonprocess.application.exceptions.DoesNotExistException;
import br.com.fiap.techchallenge.hackathonprocess.infra.gateway.filestorage.FileService;
import io.awspring.cloud.s3.S3Template;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;

import static br.com.fiap.techchallenge.hackathonprocess.domain.constants.Constants.BUCKET_NAME_BREADCRUMB;
import static br.com.fiap.techchallenge.hackathonprocess.domain.constants.Constants.EMPTY_FOLDER;

@Service
public class FileServiceS3Impl implements FileService {

    private final S3Template s3Template;

    public FileServiceS3Impl(S3Template s3Template) {
        this.s3Template = s3Template;
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
}