package br.com.fiap.techchallenge.hackathonprocess.infra.gateway.producer;

import br.com.fiap.techchallenge.hackathonprocess.infra.gateway.producer.dto.VideoUpdateDTO;

public interface VideoUpdateProducer {

    void sendToVideo(VideoUpdateDTO dto);
}
