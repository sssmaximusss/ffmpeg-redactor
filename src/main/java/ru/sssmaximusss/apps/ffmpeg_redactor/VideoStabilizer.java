package ru.sssmaximusss.apps.ffmpeg_redactor;

import org.apache.log4j.Logger;
import ru.sssmaximusss.apps.ffmpeg_redactor.utils.FileUtils;

import java.io.File;
import java.io.IOException;


public class VideoStabilizer {

    private static final Logger logger = Logger.getLogger(VideoStabilizer.class);

    private Redactor redactor;


    public void stabilize (File inputFile, File outputFile) throws IOException {

        FileUtils fileUtils = new FileUtils();
        fileUtils.withTempDir( tempDir -> {

            redactor = new RedactorImpl(tempDir.toFile(), "");

            try {
                redactor.stabilize(inputFile, outputFile);
            } catch (IOException e) {
                logger.warn(e, e);
            }

        });
    }
}
