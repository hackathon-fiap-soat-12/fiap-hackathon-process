package br.com.fiap.techchallenge.hackathonprocess.infra.extractor;

import br.com.fiap.techchallenge.hackathonprocess.application.exceptions.ErrorOnExtractFramesException;
import br.com.fiap.techchallenge.hackathonprocess.application.service.FrameExtractor;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class VideoFrameExtractor implements FrameExtractor {
    private static final int SECONDS = 20;

    @Override
    public List<InputStream> extractFrames(InputStream videoStream) {
        List<InputStream> frameStreams = new ArrayList<>();
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoStream)) {
            grabber.start();
            int frameRate = (int) grabber.getFrameRate();
            int intervalFrames = frameRate * SECONDS;

            try (Java2DFrameConverter converter = new Java2DFrameConverter()) {
                this.extractAndConvert(grabber, intervalFrames, converter, frameStreams);
            }
            grabber.stop();
        } catch (IOException e) {
            throw new ErrorOnExtractFramesException("Error On Extract Frames");
        }
        return frameStreams;
    }

    private void extractAndConvert(FFmpegFrameGrabber grabber, int intervalFrames, Java2DFrameConverter converter, List<InputStream> frameStreams) throws IOException {
        for (int i = 0; i < grabber.getLengthInFrames(); i++) {
            var frame = grabber.grabImage();
            if (frame == null) continue;

            this.bufferAndWrite(intervalFrames, converter, frameStreams, i, frame);
        }
    }

    private void bufferAndWrite(int intervalFrames, Java2DFrameConverter converter, List<InputStream> frameStreams, int i, Frame frame) throws IOException {
        if (i % intervalFrames == 0) {
            var bufferedImage = converter.convert(frame);
            if (bufferedImage != null) {
                var byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
                frameStreams.add(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            }
        }
    }
}