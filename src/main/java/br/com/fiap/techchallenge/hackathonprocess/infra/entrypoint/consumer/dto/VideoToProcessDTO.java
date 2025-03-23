package br.com.fiap.techchallenge.hackathonprocess.infra.entrypoint.consumer.dto;

import java.util.UUID;

public record VideoToProcessDTO(UUID id,
                                String bucketName,
                                String key) {
}
