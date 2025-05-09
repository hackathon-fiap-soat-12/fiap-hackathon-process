package br.com.fiap.techchallenge.hackathonprocess.infra.gateway.producer.update;

import br.com.fiap.techchallenge.hackathonprocess.application.producer.VideoUpdateProducer;
import br.com.fiap.techchallenge.hackathonprocess.infra.gateway.producer.update.dto.VideoUpdateDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VideoUpdateProducerImpl implements VideoUpdateProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoUpdateProducerImpl.class);

    @Value("${sqs.queue.process.video.producer}")
    private String videoUpdateQueue;

    private final SqsTemplate sqsTemplate;
    private final ObjectMapper objectMapper;

    public VideoUpdateProducerImpl(SqsTemplate sqsTemplate, ObjectMapper objectMapper) {
        this.sqsTemplate = sqsTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendToVideo(VideoUpdateDTO dto) {
        try {
            sqsTemplate.send(videoUpdateQueue, objectMapper.writeValueAsString(dto));

            LOGGER.info("Sent Update Status {} for id {}", dto.status(), dto.id());
        } catch (JsonProcessingException e) {
            LOGGER.error("Error on send update status for id {}", dto.id());
        }
    }
}
