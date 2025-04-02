package br.com.fiap.techchallenge.hackathonprocess.application.service;

import java.io.InputStream;
import java.util.List;

public interface FrameExtractor {
    List<InputStream> extractFrames(InputStream videoStream);
}
