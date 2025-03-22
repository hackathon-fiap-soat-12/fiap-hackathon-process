package br.com.fiap.techchallenge.hackathonprocess.infra.gateway.producer;

import br.com.fiap.techchallenge.hackathonprocess.infra.gateway.producer.dto.VideoUpdateDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;

public class VideoUpdateProducer {

    @Value("${sqs.queue.process.video.producer}")
    private String videoUpdateQueue;

    private final SqsTemplate sqsTemplate;
    private final ObjectMapper objectMapper;

    public VideoUpdateProducer(SqsTemplate sqsTemplate, ObjectMapper objectMapper) {
        this.sqsTemplate = sqsTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendToVideo(VideoUpdateDTO dto) throws JsonProcessingException {
        sqsTemplate.send(videoUpdateQueue, objectMapper.writeValueAsString(dto));
    }
}
