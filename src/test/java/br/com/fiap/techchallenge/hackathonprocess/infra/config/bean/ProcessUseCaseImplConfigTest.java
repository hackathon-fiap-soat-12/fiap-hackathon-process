package br.com.fiap.techchallenge.hackathonprocess.infra.config.bean;

import br.com.fiap.techchallenge.hackathonprocess.application.usecase.impl.ProcessUseCaseImpl;
import br.com.fiap.techchallenge.hackathonprocess.infra.extractor.VideoFrameExtractor;
import br.com.fiap.techchallenge.hackathonprocess.infra.gateway.filestorage.impl.FileServiceS3Impl;
import br.com.fiap.techchallenge.hackathonprocess.infra.gateway.producer.update.VideoUpdateProducerImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProcessUseCaseImplConfigTest {

    @Mock
    private FileServiceS3Impl fileServiceS3;

    @Mock
    private VideoFrameExtractor videoFrameExtractor;

    @Mock
    private VideoUpdateProducerImpl videoUpdateProducer;

    @InjectMocks
    private ProcessUseCaseImplConfig processUseCaseImplConfig;

    @Test
    @DisplayName("Should Create a Singleton Instance Of ProcessUseCaseImpl")
    void shouldCreateSingletonInstanceOfProcessUseCaseImpl() {
        var processUseCaseImpl = processUseCaseImplConfig.processUseCase(fileServiceS3, videoFrameExtractor, videoUpdateProducer);

        assertNotNull(processUseCaseImpl);
        assertNotNull(fileServiceS3);
        assertNotNull(videoFrameExtractor);
        assertNotNull(videoUpdateProducer);
        assertInstanceOf(ProcessUseCaseImpl.class, processUseCaseImpl);
    }


}