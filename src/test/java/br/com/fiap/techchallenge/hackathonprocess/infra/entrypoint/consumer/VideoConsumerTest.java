package br.com.fiap.techchallenge.hackathonprocess.infra.entrypoint.consumer;

import br.com.fiap.techchallenge.hackathonprocess.application.producer.VideoUpdateProducer;
import br.com.fiap.techchallenge.hackathonprocess.application.usecase.ProcessUseCase;
import br.com.fiap.techchallenge.hackathonprocess.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonprocess.infra.entrypoint.consumer.dto.VideoToProcessDTO;
import br.com.fiap.techchallenge.hackathonprocess.infra.gateway.producer.update.dto.VideoUpdateDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoConsumerTest {

    @Mock
    private ProcessUseCase processUseCase;

    @Mock
    private VideoUpdateProducer videoUpdateProducer;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private VideoConsumer videoConsumer;

    private VideoToProcessDTO videoToProcessDTO;

    private VideoUpdateDTO videoUpdateDTO;

    @BeforeEach
    void setUp() {
        this.buildArranges();
    }

    @Test
    @DisplayName("Should Call VideoConsumer When Receiving message")
    void shouldCallVideoConsumerWhenReceivingMessage() throws JsonProcessingException {
        when(objectMapper.readValue(videoToProcessDTO.toString(), VideoToProcessDTO.class)).thenReturn(videoToProcessDTO);

        videoConsumer.receiveMessage(videoToProcessDTO.toString());

        verify(videoUpdateProducer, times(1)).sendToVideo(videoUpdateDTO);

        verify(processUseCase, times(1))
                .process(objectMapper.readValue(videoToProcessDTO.toString(), VideoToProcessDTO.class));
        verify(objectMapper, times(2)).readValue(videoToProcessDTO.toString(), VideoToProcessDTO.class);
    }


    private void buildArranges(){
        videoToProcessDTO = new VideoToProcessDTO(UUID.randomUUID(), "my-bucket", "video.mp4");
        videoUpdateDTO = new VideoUpdateDTO(videoToProcessDTO.id(), ProcessStatus.PROCESSING);
    }

}