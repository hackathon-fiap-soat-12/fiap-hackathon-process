package br.com.fiap.techchallenge.hackathonprocess.application.usecase;

import br.com.fiap.techchallenge.hackathonprocess.infra.entrypoint.consumer.dto.VideoToProcessDTO;

public interface ProcessUseCase {

    void process(VideoToProcessDTO dto);
}
