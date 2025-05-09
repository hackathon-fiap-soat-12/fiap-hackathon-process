package br.com.fiap.techchallenge.hackathonprocess.infra.gateway.filestorage;

import java.io.InputStream;

public interface FileService {

    InputStream getFile(String bucketName, String key);

    Boolean uploadFile(String bucketName, String key, InputStream file);

    Long getSize(String bucketName, String key);

    void deleteFile(String bucketName, String key);
}
