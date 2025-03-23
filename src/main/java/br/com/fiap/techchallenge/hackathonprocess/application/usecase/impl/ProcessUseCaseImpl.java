package br.com.fiap.techchallenge.hackathonprocess.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonprocess.application.usecase.ProcessUseCase;
import br.com.fiap.techchallenge.hackathonprocess.infra.entrypoint.consumer.dto.VideoToProcessDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ProcessUseCaseImpl implements ProcessUseCase {

    @Override
    public void process(VideoToProcessDTO orderEvolve) throws JsonProcessingException {
        // Pega o video do bucket s3
        // processa o video
        // posta o zip no s3
    }
}
