package br.com.fiap.techchallenge.hackathonprocess.infra.entrypoint.consumer;

import br.com.fiap.techchallenge.hackathonprocess.application.usecase.ProcessUseCase;
import br.com.fiap.techchallenge.hackathonprocess.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonprocess.infra.entrypoint.consumer.dto.VideoToProcessDTO;
import br.com.fiap.techchallenge.hackathonprocess.application.producer.VideoUpdateProducer;
import br.com.fiap.techchallenge.hackathonprocess.infra.gateway.producer.update.dto.VideoUpdateDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class VideoConsumer {

    private static final Logger logger = LoggerFactory.getLogger(VideoConsumer.class);

    private final ProcessUseCase processUseCase;
    private final VideoUpdateProducer videoUpdateProducer;
    private final ObjectMapper objectMapper;


    public VideoConsumer(ProcessUseCase processUseCase, VideoUpdateProducer videoUpdateProducer, ObjectMapper objectMapper) {
        this.processUseCase = processUseCase;
        this.videoUpdateProducer = videoUpdateProducer;
        this.objectMapper = objectMapper;
    }

    @SqsListener("${sqs.queue.process.video.listener}")
    public void receiveMessage(String message) throws JsonProcessingException {
        var videoToProcess = objectMapper.readValue(message, VideoToProcessDTO.class);

        logger.info("Received video id {} to process", videoToProcess.id());

        videoUpdateProducer.sendToVideo(new VideoUpdateDTO(videoToProcess.id(), ProcessStatus.PROCESSING, 0));

        processUseCase.process(videoToProcess);
    }
}
