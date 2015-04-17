package ru.sssmaximusss.apps.ffmpeg_redactor;

import ru.sssmaximusss.apps.ffmpeg_redactor.Info.VideoInfo;

import java.io.File;
import java.io.IOException;

public interface Redactor {

    public static final String DEFAULT_CMD_EXECUTE = "ffmpeg";

    public static final String DEFAULT_CMD_GETINFO = "ffprobe";

    public static final String DEFAULT_CMD_PLAY = "ffplay";

    public String extract(final File file) throws IOException;

    public VideoInfo getInfo(final File inputFile) throws IOException, ClassNotFoundException;

    //ffmpeg -i inputFile -ss 00:01:00 -to 00:02:00
    public String cut(final File inputFile,final File outputFile, final String start, final String end) throws IOException;

    //ffmpeg -i inputFile -ss 00:01:00 -t 60
    public String cut(final File inputFile,final File outputFile, final String start, final Integer duration) throws IOException;

    public void resize(final File inputFile, final File outputFile, final int width, final int height) throws IOException;

    public void resize(final File inputFile, final String outputFile, final int width, final int height) throws IOException;

    public void imageSetToVideo(final String inputFilePattern, final File outputFile, final Integer duration) throws IOException;

    public void imageToVideo(final File inputFile, final File outputFile, final Integer duration) throws IOException;

    public void concatenate(final File inputFileList, final File outputFile) throws IOException;

    public void stabilize(final File inputFile, final File outputFile) throws IOException;

    public void rotate(final File inputFile, final File outputFile, final int degree) throws IOException;

    public void controlSpeed(final File inputFile, final File outputFile, final float tempo) throws IOException;

    public void setContrast(final File inputFile, final File outputFile, final float contrast) throws IOException;

    public void setBrightness(final File inputFile, final File outputFile, final float brightness) throws IOException;

    public void setSaturation(final File inputFile, final File outputFile, final float saturation) throws IOException;

    public void setAllSetting(final File inputFile, final File outputFile, final float contrast, final float brightness, final float saturation) throws IOException;
}
