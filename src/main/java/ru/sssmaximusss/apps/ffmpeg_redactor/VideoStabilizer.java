package ru.sssmaximusss.apps.ffmpeg_redactor;

import org.apache.log4j.Logger;
import ru.sssmaximusss.apps.ffmpeg_redactor.utils.FileUtils;

import java.io.File;
import java.io.IOException;


public class VideoStabilizer {

    private static final Logger logger = Logger.getLogger(VideoStabilizer.class);

    private final Redactor redactor;

    public VideoStabilizer(Redactor redactor) {
        this.redactor = redactor;
    }

    public void stabilize(File inputFile, File outputFile) throws IOException {

        FileUtils fileUtils = new FileUtils();
        fileUtils.withTempDir( tempDir -> {
            try {
                redactor.stabilize(inputFile, outputFile, tempDir.toFile());
            } catch (IOException e) {
                logger.warn(e, e);
            }
        });
    }
}
