package br.com.fiap.techchallenge.hackathonprocess.application.usecase;

import br.com.fiap.techchallenge.hackathonprocess.infra.entrypoint.consumer.dto.VideoToProcessDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ProcessUseCase {
    void process(VideoToProcessDTO orderEvolve) throws JsonProcessingException;
}
