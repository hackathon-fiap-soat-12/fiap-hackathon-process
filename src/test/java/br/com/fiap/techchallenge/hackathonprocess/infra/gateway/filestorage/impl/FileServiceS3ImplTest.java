package br.com.fiap.techchallenge.hackathonprocess.infra.gateway.filestorage.impl;

import br.com.fiap.techchallenge.hackathonprocess.application.exceptions.DoesNotExistException;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.BeforeEach;
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

    private final String bucket = "my-bucket";
    private final String key = "videofiles/video.mp4";

    @BeforeEach
    void setup() {
        fileService = new FileServiceS3Impl(s3Template, s3Client);
    }

    @Test
    void testGetFile_Success() throws IOException {
        InputStream mockInputStream = new ByteArrayInputStream("test content".getBytes());

        when(s3Template.download(bucket, "video.mp4")).thenReturn(resource);
        when(resource.getInputStream()).thenReturn(mockInputStream);

        InputStream result = fileService.getFile(bucket, key);

        assertNotNull(result);
        assertEquals(mockInputStream, result);
        verify(s3Template).download(bucket, "video.mp4");
    }

    @Test
    void testGetFile_ThrowsDoesNotExistException() throws IOException {
        when(s3Template.download(bucket, "video.mp4")).thenReturn(resource);
        when(resource.getInputStream()).thenThrow(new IOException("Not found"));

        DoesNotExistException exception = assertThrows(DoesNotExistException.class,
                () -> fileService.getFile(bucket, key));

        assertEquals("File not found", exception.getMessage());
    }

    @Test
    void testUploadFile_Success() {
        InputStream file = new ByteArrayInputStream("dummy".getBytes());

        S3Resource mockUploadedResource = mock(S3Resource.class);
        when(mockUploadedResource.exists()).thenReturn(true);

        when(s3Template.upload(bucket, key, file)).thenReturn(mockUploadedResource);

        Boolean result = fileService.uploadFile(bucket, key, file);

        assertTrue(result);
        verify(s3Template).upload(bucket, key, file);
    }

    @Test
    void testUploadFile_Failure() {
        InputStream file = new ByteArrayInputStream("dummy".getBytes());

        S3Resource mockUploadedResource = mock(S3Resource.class);
        when(mockUploadedResource.exists()).thenReturn(false);

        when(s3Template.upload(bucket, key, file)).thenReturn(mockUploadedResource);

        Boolean result = fileService.uploadFile(bucket, key, file);

        assertFalse(result);
        verify(s3Template).upload(bucket, key, file);
    }

    @Test
    void testGetSize_Success() {
        String bucket = "my-bucket";
        String key = "test.txt";

        HeadObjectResponse response = HeadObjectResponse.builder()
                .contentLength(12345L)
                .build();

        when(s3Client.headObject(any(HeadObjectRequest.class))).thenReturn(response);

        Long result = fileService.getSize(bucket, key);

        assertEquals(12345L, result);
        verify(s3Client).headObject(argThat((HeadObjectRequest req) ->
                req.bucket().equals(bucket) && req.key().equals(key)));

    }

    @Test
    void testGetSize_ThrowsException() {
        String bucket = "my-bucket";
        String key = "nonexistent.txt";

        when(s3Client.headObject(any(HeadObjectRequest.class)))
                .thenThrow(S3Exception.builder().message("Not Found").build());

        assertThrows(S3Exception.class, () -> fileService.getSize(bucket, key));
    }

}
