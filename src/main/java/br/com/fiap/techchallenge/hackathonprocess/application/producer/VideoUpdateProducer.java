package br.com.fiap.techchallenge.hackathonprocess.application.producer;

import br.com.fiap.techchallenge.hackathonprocess.infra.gateway.producer.update.dto.VideoUpdateDTO;

public interface VideoUpdateProducer {

    void sendToVideo(VideoUpdateDTO dto);
}
