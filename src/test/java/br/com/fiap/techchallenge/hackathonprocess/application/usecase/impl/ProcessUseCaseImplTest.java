package br.com.fiap.techchallenge.hackathonprocess.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonprocess.application.exceptions.ProcessException;
import br.com.fiap.techchallenge.hackathonprocess.application.producer.VideoUpdateProducer;
import br.com.fiap.techchallenge.hackathonprocess.application.service.FrameExtractor;
import br.com.fiap.techchallenge.hackathonprocess.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonprocess.infra.entrypoint.consumer.dto.VideoToProcessDTO;
import br.com.fiap.techchallenge.hackathonprocess.infra.gateway.filestorage.FileService;
import br.com.fiap.techchallenge.hackathonprocess.infra.gateway.producer.update.dto.VideoUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProcessUseCaseImplTest {

    private FileService fileService;
    private FrameExtractor frameExtractor;
    private VideoUpdateProducer videoUpdateProducer;
    private ProcessUseCaseImpl processUseCase;

    @BeforeEach
    void setUp() {
        fileService = mock(FileService.class);
        frameExtractor = mock(FrameExtractor.class);
        videoUpdateProducer = mock(VideoUpdateProducer.class);
        processUseCase = new ProcessUseCaseImpl(fileService, frameExtractor, videoUpdateProducer);
    }

    @Test
    void testProcess_SuccessfulProcessing(){
        UUID videoId = UUID.randomUUID();
        String bucketName = "my-bucket";
        String key = "videofiles/videos/sample.mp4";
        VideoToProcessDTO dto = new VideoToProcessDTO(videoId, bucketName, key);

        InputStream videoStream = new ByteArrayInputStream("dummy".getBytes());
        List<InputStream> frames = List.of(new ByteArrayInputStream("frame1".getBytes()));

        when(fileService.getFile(bucketName, key)).thenReturn(videoStream);
        when(frameExtractor.extractFrames(videoStream)).thenReturn(frames);
        when(fileService.uploadFile(eq(bucketName), anyString(), any())).thenReturn(true);

        processUseCase.process(dto);

        ArgumentCaptor<VideoUpdateDTO> updateCaptor = ArgumentCaptor.forClass(VideoUpdateDTO.class);
        verify(videoUpdateProducer).sendToVideo(updateCaptor.capture());

        VideoUpdateDTO update = updateCaptor.getValue();
        assertEquals(videoId, update.id());
        assertEquals(ProcessStatus.PROCESSED, update.status());

        verify(fileService).getFile(bucketName, key);
        verify(frameExtractor).extractFrames(videoStream);
        verify(fileService).uploadFile(eq(bucketName), contains("frames"), any());
    }

    @Test
    void testProcess_FailureDuringProcessing(){
        UUID videoId = UUID.randomUUID();
        String bucketName = "my-bucket";
        String key = "videofiles/videos/sample.mp4";
        VideoToProcessDTO dto = new VideoToProcessDTO(videoId, bucketName, key);

        when(fileService.getFile(bucketName, key)).thenThrow(new ProcessException("File not found"));

        processUseCase.process(dto);

        ArgumentCaptor<VideoUpdateDTO> updateCaptor = ArgumentCaptor.forClass(VideoUpdateDTO.class);
        verify(videoUpdateProducer).sendToVideo(updateCaptor.capture());

        VideoUpdateDTO update = updateCaptor.getValue();
        assertEquals(videoId, update.id());
        assertEquals(ProcessStatus.FAILED, update.status());

        verify(fileService).getFile(bucketName, key);
        verifyNoInteractions(frameExtractor);
        verify(fileService, never()).uploadFile(any(), any(), any());
    }
}
