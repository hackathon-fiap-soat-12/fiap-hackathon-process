package br.com.fiap.techchallenge.hackathonprocess.application.service;

import br.com.fiap.techchallenge.hackathonprocess.application.exceptions.ErrorOnZipFileException;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static br.com.fiap.techchallenge.hackathonprocess.domain.constants.Constants.PREFIX_FILE;
import static br.com.fiap.techchallenge.hackathonprocess.domain.constants.Constants.SUFFIX_FILE;

public class ZipFiles {

    private ZipFiles() { }

    public static InputStream zipFilesToInputStream(List<InputStream> frames) {
        var byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            int index = 0;
            for (InputStream frameStream : frames) {
                zipOutputStream.putNextEntry(new ZipEntry(String.format("%s%d%s", PREFIX_FILE, index, SUFFIX_FILE)));
                frameStream.transferTo(zipOutputStream);
                zipOutputStream.closeEntry();
                index++;
            }
        } catch (IOException e) {
            throw new ErrorOnZipFileException("Error on Zipfile");
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }
}
