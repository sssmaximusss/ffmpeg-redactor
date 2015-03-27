package ru.sssmaximusss.apps.ffmpeg_redactor;

import ru.sssmaximusss.apps.ffmpeg_redactor.Redactor;
import ru.sssmaximusss.apps.ffmpeg_redactor.RedactorImpl;
import ru.sssmaximusss.apps.ffmpeg_redactor.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import org.apache.log4j.Logger;

/**
 * Created by Mike on 26-Mar-15.
 */
public class SlideshowMakers {

    private  final Logger logger = Logger.getLogger(SlideshowMakers.class);

    private static final String tempImgFilePattern = "%05d.png";

    private Redactor redactor;


    public void createSimpleSlideshow (Iterable<File> inputFiles, File outputFile, String width, String height, String duration) throws IOException {

        FileUtils fileUtils = new FileUtils();
        fileUtils.withTempDir( tempDir -> {

            redactor = new RedactorImpl(tempDir.toFile());

            int fileNumber = 0;
            for (File inputFile : inputFiles) {
                try {
                    redactor.resize(inputFile, String.format(tempImgFilePattern, fileNumber++), Integer.valueOf(width), Integer.valueOf(height));
                } catch (IOException e) {
                    logger.warn(e, e);
                }
            }

            try {
                redactor.imageSetToVideo(tempImgFilePattern, outputFile, Integer.parseInt(duration));
            } catch (IOException e) {
                logger.warn(e, e);
            }

        });
    }
}
