package br.com.fiap.techchallenge.hackathonprocess.infra.extractor;

import br.com.fiap.techchallenge.hackathonprocess.application.exceptions.ErrorOnExtractFramesException;
import br.com.fiap.techchallenge.hackathonprocess.application.service.FrameExtractor;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.springframework.stereotype.Component;

import org.jcodec.scale.AWTUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Component
public class VideoFrameExtractor implements FrameExtractor {
    private static final int SECONDS_INTERVAL = 20;

    @Override
    public List<InputStream> extractFrames(InputStream videoStream) {
        try {
            List<InputStream> frameStreams = new ArrayList<>();
            var tempFile = this.transformInFile(videoStream);

            try (SeekableByteChannel channel = NIOUtils.readableChannel(tempFile)) {
                FrameGrab grab = FrameGrab.createFrameGrab(channel);

                double frameRate = grab.getVideoTrack().getMeta().getTotalFrames()
                        / grab.getVideoTrack().getMeta().getTotalDuration();
                int totalFrames = grab.getVideoTrack().getMeta().getTotalFrames();
                int frameInterval = (int) (frameRate * SECONDS_INTERVAL);

                this.executeExtraction(grab, totalFrames, frameInterval, frameStreams);
            }
            Files.delete(tempFile.toPath());

            return frameStreams;
        } catch (IOException | JCodecException e) {
            throw new ErrorOnExtractFramesException("Erro ao extrair frames com JCodec");
        }
    }

    private void executeExtraction(FrameGrab grab, int totalFrames, int frameInterval, List<InputStream> frameStreams) throws IOException, JCodecException {
        for (int frameNumber = 0; frameNumber < totalFrames; frameNumber += frameInterval) {
            grab.seekToFramePrecise(frameNumber);
            Picture picture = grab.getNativeFrame();
            if (picture == null) continue;

            BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);

            frameStreams.add(new ByteArrayInputStream(baos.toByteArray()));
        }
    }

    private File transformInFile(InputStream videoStream) throws IOException {
        var tempFile = File.createTempFile("video", ".mp4");

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            videoStream.transferTo(fos);
        }
        return tempFile;
    }
}