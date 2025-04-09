package br.com.fiap.techchallenge.hackathonprocess.infra.gateway.producer.update;

import br.com.fiap.techchallenge.hackathonprocess.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonprocess.infra.gateway.producer.update.dto.VideoUpdateDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoUpdateProducerImplTest {

    @Mock
    private SqsTemplate sqsTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private VideoUpdateProducerImpl videoUpdateProducer;

    private VideoUpdateDTO videoUpdateDTO;

    @BeforeEach
    void setUp() {
        this.buildArranges();
        ReflectionTestUtils.setField(videoUpdateProducer, "videoUpdateQueue", "sqs.queue.process.video.producer");
    }

    @Test
    @DisplayName("Should Send To Update Queue")
    void shouldSendToUpdateQueue() throws JsonProcessingException {
        videoUpdateProducer.sendToVideo(videoUpdateDTO);

        verify(sqsTemplate).send("sqs.queue.process.video.producer", objectMapper.writeValueAsString(videoUpdateDTO));
        verify(objectMapper, times(2)).writeValueAsString(videoUpdateDTO);
    }

    @Test
    @DisplayName("Should Log Error When JsonProcessingException Occurs")
    void shouldLogErrorWhenJsonProcessingExceptionOccurs() throws JsonProcessingException {
        doThrow(new JsonProcessingException("Erro de serialização") {})
                .when(objectMapper).writeValueAsString(videoUpdateDTO);

        videoUpdateProducer.sendToVideo(videoUpdateDTO);

        verify(objectMapper).writeValueAsString(videoUpdateDTO);

        verify(sqsTemplate, times(0)).send("sqs.queue.video.process.producer", videoUpdateDTO.toString());
    }


    private void buildArranges(){
        videoUpdateDTO = new VideoUpdateDTO(UUID.randomUUID(), ProcessStatus.PROCESSING, 20);
    }

}