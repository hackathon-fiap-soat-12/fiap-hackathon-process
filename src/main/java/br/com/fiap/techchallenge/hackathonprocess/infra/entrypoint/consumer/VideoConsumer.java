package br.com.fiap.techchallenge.hackathonprocess.infra.entrypoint.consumer;

import br.com.fiap.techchallenge.hackathonprocess.application.usecase.ProcessUseCase;
import br.com.fiap.techchallenge.hackathonprocess.infra.entrypoint.consumer.dto.VideoToProcessDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class VideoConsumer {

    private final ProcessUseCase processUseCase;
    private final ObjectMapper objectMapper;


    public VideoConsumer(ProcessUseCase processUseCase, ObjectMapper objectMapper) {
        this.processUseCase = processUseCase;
        this.objectMapper = objectMapper;
    }

    @SqsListener("${sqs.queue.order.evolve.listener}")
    public void receiveMessage(String message) throws JsonProcessingException {
        processUseCase.process(objectMapper.readValue(message, VideoToProcessDTO.class));
    }
}
