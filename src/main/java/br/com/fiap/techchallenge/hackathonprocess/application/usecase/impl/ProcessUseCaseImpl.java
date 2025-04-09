package br.com.fiap.techchallenge.hackathonprocess.application.usecase.impl;

import br.com.fiap.techchallenge.hackathonprocess.application.exceptions.ProcessException;
import br.com.fiap.techchallenge.hackathonprocess.application.service.FrameExtractor;
import br.com.fiap.techchallenge.hackathonprocess.application.usecase.ProcessUseCase;
import br.com.fiap.techchallenge.hackathonprocess.domain.enums.ProcessStatus;
import br.com.fiap.techchallenge.hackathonprocess.infra.entrypoint.consumer.dto.VideoToProcessDTO;
import br.com.fiap.techchallenge.hackathonprocess.infra.gateway.filestorage.FileService;
import br.com.fiap.techchallenge.hackathonprocess.application.producer.VideoUpdateProducer;
import br.com.fiap.techchallenge.hackathonprocess.infra.gateway.producer.update.dto.VideoUpdateDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static br.com.fiap.techchallenge.hackathonprocess.application.service.ZipFiles.zipFilesToInputStream;
import static br.com.fiap.techchallenge.hackathonprocess.domain.constants.Constants.*;


public class ProcessUseCaseImpl implements ProcessUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ProcessUseCaseImpl.class);
    private final FileService fileService;
    private final FrameExtractor frameExtractor;
    private final VideoUpdateProducer videoUpdateProducer;

    public ProcessUseCaseImpl(FileService fileService, FrameExtractor frameExtractor, VideoUpdateProducer videoUpdateProducer) {
        this.fileService = fileService;
        this.frameExtractor = frameExtractor;
        this.videoUpdateProducer = videoUpdateProducer;
    }

    @Override
    public void process(VideoToProcessDTO dto){
        boolean processed = false;
        int qtdFrames = 0;

        try {
            var videoStream = fileService.getFile(dto.bucketName(), dto.key());

            var frames = frameExtractor.extractFrames(videoStream);

            processed = fileService.uploadFile(dto.bucketName(), generateKeyName(dto.key()), zipFilesToInputStream(frames));

            qtdFrames = frames.size();
            logger.info("Success on process {}", dto.id());
        } catch (ProcessException e){
            logger.error("Error on process {}", dto.id());
        } finally {
            videoUpdateProducer.sendToVideo(new VideoUpdateDTO(dto.id(), processed ? ProcessStatus.PROCESSED : ProcessStatus.FAILED, qtdFrames));
        }
    }

    private String generateKeyName(String key) {
        return key.replace(BUCKET_NAME_BREADCRUMB, EMPTY_FOLDER)
                .replace(VIDEOS_FOLDER_NAME, FRAMES_FOLDER_NAME)
                + ZIP_FILE_EXTENSION;
    }
}
