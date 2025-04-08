package br.com.fiap.techchallenge.hackathonprocess.application.service;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;

class ZipFilesTest {

    @Test
    void shouldZipFilesSuccessfully() throws IOException {
        // Arrange: cria 2 arquivos simulados
        String content1 = "frame data 1";
        String content2 = "frame data 2";
        InputStream inputStream1 = new ByteArrayInputStream(content1.getBytes());
        InputStream inputStream2 = new ByteArrayInputStream(content2.getBytes());

        // Act
        InputStream result = ZipFiles.zipFilesToInputStream(List.of(inputStream1, inputStream2));

        // Assert
        ZipInputStream zipInputStream = new ZipInputStream(result);
        ZipEntry entry;

        int count = 0;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            zipInputStream.transferTo(baos);
            String content = baos.toString();

            if (entry.getName().equals("frame_0.jpg")) {
                assertEquals(content1, content);
            } else if (entry.getName().equals("frame_1.jpg")) {
                assertEquals(content2, content);
            } else {
                fail("Unexpected file name: " + entry.getName());
            }

            count++;
        }

        assertEquals(2, count);
    }

    @Test
    void shouldThrowErrorOnZipFileExceptionWhenIOExceptionOccurs() {
        try (InputStream brokenInputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Simulated error");
            }
        }) {
            assertThrows(Exception.class, () ->
                    ZipFiles.zipFilesToInputStream(List.of(brokenInputStream))
            );
        } catch (IOException e) {
            fail("IOException should not occur while closing the broken input stream.");
        }
    }
}
