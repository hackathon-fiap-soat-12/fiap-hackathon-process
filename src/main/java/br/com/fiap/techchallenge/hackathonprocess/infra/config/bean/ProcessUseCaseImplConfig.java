package br.com.fiap.techchallenge.hackathonprocess.infra.config.bean;

import br.com.fiap.techchallenge.hackathonprocess.application.usecase.impl.ProcessUseCaseImpl;
import br.com.fiap.techchallenge.hackathonprocess.infra.extractor.VideoFrameExtractor;
import br.com.fiap.techchallenge.hackathonprocess.infra.gateway.filestorage.impl.FileServiceS3Impl;
import br.com.fiap.techchallenge.hackathonprocess.infra.gateway.producer.update.VideoUpdateProducerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessUseCaseImplConfig {

    @Bean
    public ProcessUseCaseImpl processUseCase(FileServiceS3Impl fileServiceS3,
                                             VideoFrameExtractor videoFrameExtractor,
                                             VideoUpdateProducerImpl videoUpdateProducer){
        return new ProcessUseCaseImpl(fileServiceS3, videoFrameExtractor, videoUpdateProducer);
    }
}
