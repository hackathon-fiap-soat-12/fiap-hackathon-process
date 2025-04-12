package br.com.fiap.techchallenge.hackathonprocess.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonprocess.application.exceptions.ProcessException;
import br.com.fiap.techchallenge.hackathonprocess.application.producer.VideoUpdateProducer;
import br.com.fiap.techchallenge.hackathonprocess.application.service.FrameExtractor;
import br.com.fiap.techchallenge.hackathonprocess.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonprocess.infra.entrypoint.consumer.dto.VideoToProcessDTO;
import br.com.fiap.techchallenge.hackathonprocess.infra.gateway.filestorage.FileService;
import br.com.fiap.techchallenge.hackathonprocess.infra.gateway.producer.update.dto.VideoUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProcessUseCaseImplTest {

    @Mock
    private FileService fileService;

    @Mock
    private FrameExtractor frameExtractor;

    @Mock
    private VideoUpdateProducer videoUpdateProducer;

    @InjectMocks
    private ProcessUseCaseImpl processUseCase;

    private VideoToProcessDTO dto;

    @BeforeEach
    void setUp() {
        this.buildArranges();
    }

    private void buildArranges() {
        UUID videoId = UUID.randomUUID();
        String bucketName = "my-bucket";
        String key = "videofiles/videos/sample.mp4";
        dto = new VideoToProcessDTO(videoId, bucketName, key);
    }

    @Test
    @DisplayName("Should Process Video Successfully")
    void shouldProcessVideoSuccessfully(){
        var videoStream = new ByteArrayInputStream("dummy".getBytes());
        List<InputStream> frames = List.of(new ByteArrayInputStream("frame1".getBytes()));

        when(fileService.getFile(dto.bucketName(), dto.key())).thenReturn(videoStream);
        when(frameExtractor.extractFrames(videoStream)).thenReturn(frames);
        when(fileService.uploadFile(eq(dto.bucketName()), anyString(), any())).thenReturn(true);

        processUseCase.process(dto);

        ArgumentCaptor<VideoUpdateDTO> updateCaptor = ArgumentCaptor.forClass(VideoUpdateDTO.class);
        verify(videoUpdateProducer).sendToVideo(updateCaptor.capture());

        var update = updateCaptor.getValue();
        assertEquals(dto.id(), update.id());
        assertEquals(ProcessStatus.PROCESSED, update.status());

        verify(fileService).getFile(dto.bucketName(), dto.key());
        verify(frameExtractor).extractFrames(videoStream);
        verify(fileService).uploadFile(eq(dto.bucketName()), contains("frames"), any());
    }

    @Test
    @DisplayName("Should Process Video Failure During Processing")
    void shouldProcessVideoFailureDuringProcessing(){
        when(fileService.getFile(dto.bucketName(), dto.key())).thenThrow(new ProcessException("File not found"));

        processUseCase.process(dto);

        ArgumentCaptor<VideoUpdateDTO> updateCaptor = ArgumentCaptor.forClass(VideoUpdateDTO.class);
        verify(videoUpdateProducer).sendToVideo(updateCaptor.capture());

        VideoUpdateDTO update = updateCaptor.getValue();
        assertEquals(dto.id(), update.id());
        assertEquals(ProcessStatus.FAILED, update.status());

        verify(fileService).getFile(dto.bucketName(), dto.key());
        verifyNoInteractions(frameExtractor);
        verify(fileService, never()).uploadFile(any(), any(), any());
    }
}
