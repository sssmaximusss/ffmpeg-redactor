package ru.sssmaximusss.apps.ffmpeg_redactor;

import org.apache.log4j.Logger;
import ru.sssmaximusss.apps.ffmpeg_redactor.utils.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Mike on 26-Mar-15.
 */
public class SlideshowMakers {

    private static final Logger logger = Logger.getLogger(SlideshowMakers.class);

    private static final String tempImgFilePattern = "%05d.png";
    private final Redactor redactor;

    public SlideshowMakers(Redactor redactor) {
        this.redactor = redactor;
    }

    public void createSimpleSlideshow (
            Iterable<File> inputFiles, File outputFile,
            String width, String height, String duration) throws IOException
    {

        FileUtils fileUtils = new FileUtils();
        fileUtils.withTempDir( tempDir -> {
            int fileNumber = 0;
            for (File inputFile : inputFiles) {
                try {
                    redactor.resize(inputFile, String.format(tempImgFilePattern, fileNumber++), tempDir.toFile(),
                            Integer.valueOf(width), Integer.valueOf(height));
                } catch (IOException e) {
                    logger.warn(e, e);
                }
            }

            try {
                redactor.imageSetToVideo(tempImgFilePattern, outputFile, tempDir.toFile(), Integer.parseInt(duration));
            } catch (IOException e) {
                logger.warn(e, e);
            }

        });
    }
}
