package br.com.fiap.techchallenge.hackathonprocess.infra.gateway.producer.update.dto;

import br.com.fiap.techchallenge.hackathonprocess.domain.enums.ProcessStatus;

import java.util.UUID;

public record VideoUpdateDTO(UUID id,
                             ProcessStatus status,
                             Integer qtdFrames,
                             Long sizeInBytes) {
    public VideoUpdateDTO(UUID id, ProcessStatus status) {
        this(id, status, 0, 0L);
    }
}
