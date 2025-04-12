package br.com.fiap.techchallenge.hackathonprocess.infra.gateway.filestorage.impl;

import br.com.fiap.techchallenge.hackathonprocess.application.exceptions.DoesNotExistException;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceS3ImplTest {

    @Mock
    private S3Template s3Template;

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Resource resource;

    @InjectMocks
    private FileServiceS3Impl fileService;

    private InputStream mockInputStream;

    private String bucket;
    private String key;

    @BeforeEach
    void setUp() {
        this.buildArranges();
    }

    @Test
    @DisplayName("Should Get File")
    void shouldGetFile() throws IOException {
        String key2 = "video.mp4";
        when(s3Template.download(bucket, key2)).thenReturn(resource);
        when(resource.getInputStream()).thenReturn(mockInputStream);

        var result = fileService.getFile(bucket, key);

        assertNotNull(result);
        assertEquals(mockInputStream, result);
        verify(s3Template).download(bucket, key2);
    }

    @Test
    @DisplayName("Should Throw DoesNotExistException")
    void shouldThrowDoesNotExistException() throws IOException {
        when(s3Template.download(bucket, "video.mp4")).thenReturn(resource);
        when(resource.getInputStream()).thenThrow(new IOException("Not found"));

        var exception = assertThrows(DoesNotExistException.class,
                () -> fileService.getFile(bucket, key));

        assertEquals("File not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should Upload File")
    void shouldUploadFile() {
        when(resource.exists()).thenReturn(true);

        when(s3Template.upload(bucket, key, mockInputStream)).thenReturn(resource);

        var result = fileService.uploadFile(bucket, key, mockInputStream);

        assertTrue(result);
        verify(s3Template).upload(bucket, key, mockInputStream);
    }

    @Test
    @DisplayName("Should Upload File Failure")
    void shouldUploadFileFailure() {
        when(resource.exists()).thenReturn(false);

        when(s3Template.upload(bucket, key, mockInputStream)).thenReturn(resource);

        var result = fileService.uploadFile(bucket, key, mockInputStream);

        assertFalse(result);
        verify(s3Template).upload(bucket, key, mockInputStream);
    }

    @Test
    @DisplayName("Should Get Size")
    void shouldGetSize() {
        HeadObjectResponse response = HeadObjectResponse.builder()
                .contentLength(12345L)
                .build();

        when(s3Client.headObject(any(HeadObjectRequest.class))).thenReturn(response);

        var result = fileService.getSize(bucket, key);

        assertEquals(12345L, result);
        verify(s3Client).headObject(argThat((HeadObjectRequest req) ->
                req.bucket().equals(bucket) && req.key().equals(key)));

    }

    @Test
    @DisplayName("Should Get Size Failure")
    void shouldGetSizeFailure() {
        when(s3Client.headObject(any(HeadObjectRequest.class)))
                .thenThrow(S3Exception.builder().message("Not Found").build());

        assertThrows(S3Exception.class, () -> fileService.getSize(bucket, key));
    }

    private void buildArranges() {
        mockInputStream = new ByteArrayInputStream("test content".getBytes());
        bucket = "my-bucket";
        key = "videofiles/video.mp4";
    }
}
