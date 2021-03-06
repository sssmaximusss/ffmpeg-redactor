package ru.sssmaximusss.apps.ffmpeg_redactor;

import org.apache.log4j.Logger;
import ru.sssmaximusss.apps.ffmpeg_redactor.Info.VideoInfo;
import ru.sssmaximusss.apps.ffmpeg_redactor.Info.VideoInfoDirector;
import ru.sssmaximusss.apps.ffmpeg_redactor.Info.VideoInfoParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class RedactorImpl implements Redactor {

    public static final String DEFAULT_CMD_EXECUTE = "ffmpeg";
    public static final String DEFAULT_CMD_GETINFO = "ffprobe";
    public static final String DEFAULT_CMD_PLAY = "ffplay";

    private static final Logger logger = Logger.getLogger(RedactorImpl.class);
    private final ShellExecuter shellExecuter;

    private final String ffmpegCmd;
    private final String ffprobeCmd;
    private final String ffplayCmd;

    public RedactorImpl() {
        this("");
    }

    public RedactorImpl(String ffmpegPath) {
        this.shellExecuter = new ShellExecuter();
        this.ffmpegCmd = ffmpegPath + DEFAULT_CMD_EXECUTE;
        this.ffprobeCmd = ffmpegPath + DEFAULT_CMD_GETINFO;
        this.ffplayCmd = ffmpegPath + DEFAULT_CMD_PLAY;
    }

    public String extract(final File inputFile) throws IOException {
        List<String> params = new ArrayList<>();

        //ffprobe
        params.add(ffprobeCmd);
        params.add("-i");
        params.add(inputFile.getAbsolutePath());
        params.add("-show_format");
        params.add("-show_streams");
        params.add("-print_format");
        params.add("json");
        params.add("-v");
        params.add("quiet");

        logCommand(params);

        return shellExecuter.executeAndWait(params);

    }

    @Override
    public VideoInfo getInfo(final File inputFile) throws IOException, NoClassDefFoundError {
        String rawInfo = extract(inputFile);
        VideoInfoParser parser = new VideoInfoParser();
        Map<String, Map<String, Object>> parsingInfo = parser.parse(rawInfo);

        VideoInfoDirector director = new VideoInfoDirector();

        return director.constructVideoInfo(parsingInfo);

    }

    @Override
    public String cut(final File inputFile, final File outputFile, final String start, final String end) throws IOException {

        Integer duration = transformTime(end) - transformTime(start);
        if (duration < 0) {
            throw new IllegalArgumentException();
        }
        return cut(inputFile, outputFile, start, duration);
    }

    @Override
    public String cut(final File inputFile, final File outputFile, final String start, final Integer duration) throws IOException {
        List<String> params = new ArrayList<>();

        //ffmpeg
        params.add(ffmpegCmd);
        params.add("-i");
        params.add(inputFile.getAbsolutePath());
        params.add("-ss");
        params.add(start);
        params.add("-t");
        params.add(duration.toString());

        params.add("-c");
        params.add("copy");
        params.add("-y");

        params.add(outputFile.getAbsolutePath());

        logCommand(params);

        return shellExecuter.executeAndWait(params);
    }

    private int transformTime(String time) {
        int hours = Integer.parseInt(time.substring(0, 2));
        int minutes = Integer.parseInt(time.substring(3, 5));
        int seconds = Integer.parseInt(time.substring(6, 8));
        return (hours * 3600 + minutes * 60 + seconds);
    }


    public void resize(final File inputFile, final File outputFile, final int width, final int height) throws IOException {
        List<String> params = new ArrayList<>();

        params.add(ffmpegCmd);
        params.add("-i");
        params.add(inputFile.getAbsolutePath());
        params.add("-vf");
        params.add("scale=" + width + ":" + height);
        params.add("-y");
        params.add(outputFile.getAbsolutePath());

        logCommand(params);

        shellExecuter.executeAndWait(params);
    }

    public void resize(final File inputFile, final String outputFile, final File workingDir,
                       final int width, final int height) throws IOException {
        List<String> params = new ArrayList<>();

        params.add(ffmpegCmd);
        params.add("-i");
        params.add(inputFile.getAbsolutePath());
        params.add("-vf");
        params.add("scale=" + width + ":" + height);
        params.add("-y");
        params.add(outputFile);

        logCommand(params);

        shellExecuter.executeAndWait(params, workingDir);
    }

    public void imageSetToVideo(final String inputFilePattern, final File outputFile,
                                final File workingDir, final Integer duration) throws IOException {
        List<String> params = new ArrayList<>();

        params.add(ffmpegCmd);
        params.add("-framerate");
        params.add("1/" + duration);
        params.add("-i");
        params.add(inputFilePattern);
        params.add("-c:v");
        params.add("libx264");
        params.add("-r");
        params.add("30");
        params.add("-pix_fmt");
        params.add("yuv420p");
        params.add("-y");
        params.add(outputFile.getAbsolutePath());

        logCommand(params);

        shellExecuter.executeAndWait(params, workingDir);
    }

    public void imageToVideo(final File inputFile, final File outputFile, final Integer duration) throws IOException {
        List<String> params = new ArrayList<>();

        params.add(ffmpegCmd);
        params.add("-loop");
        params.add("1");
        params.add("-i");
        params.add(inputFile.getAbsolutePath());
        params.add("-c:v");
        params.add("libx264");
        params.add("-t");
        params.add(duration.toString());
        params.add("-pix_fmt");
        params.add("yuv420p");
        params.add("-y");
        params.add(outputFile.getAbsolutePath());

        logCommand(params);

        shellExecuter.executeAndWait(params);
    }

    /*
    Attention! inputFileList should actually be a plain text file in format:
        file 'path/to/file1'
        file 'path/to/file2'
        ...
     */
    public void concatenate(final File inputFileList, final File outputFile) throws IOException {
        List<String> params = new ArrayList<>();

        params.add(ffmpegCmd);
        params.add("-f");
        params.add("concat");
        params.add("-i");
        params.add(inputFileList.getAbsolutePath());
        params.add("-c");
        params.add("copy");
        params.add("-y");
        params.add(outputFile.getAbsolutePath());

        logCommand(params);

        shellExecuter.executeAndWait(params);
    }


    public void stabilize(final File inputFile, final File outputFile, final File workingDir)  throws IOException {
        List<String> params = new ArrayList<>();

        params.add(ffmpegCmd);
        params.add("-i");
        params.add(inputFile.getAbsolutePath());
        params.add("-vf");
        params.add("vidstabdetect=shakiness=5:show=1");
        params.add("-y");
        params.add("-strict");
        params.add("-2");
        params.add("temp.mp4");

        logCommand(params);

        shellExecuter.executeAndWait(params, workingDir);

        params = new ArrayList<>();

        params.add(ffmpegCmd);
        params.add("-i");
        params.add(inputFile.getAbsolutePath());
        params.add("-vf");
        params.add("vidstabtransform");
        params.add("-y");
        params.add("-strict");
        params.add("-2");
        params.add(outputFile.getAbsolutePath());

        logCommand(params);

        shellExecuter.executeAndWait(params, workingDir);
    }

    @Override
    public void rotate(final File inputFile, final File outputFile, final int degree) throws IOException {
        List<String> params = new ArrayList<>();

        params.add(ffmpegCmd);
        params.add("-i");
        params.add(inputFile.getAbsolutePath());
        params.add("-vf");
        params.add("rotate=" + degree + "*PI/2");
        params.add("-y");
        params.add("-strict");
        params.add("-2");
        params.add(outputFile.getAbsolutePath());

        logCommand(params);

        shellExecuter.executeAndWait(params);

    }

    @Override
    public void controlSpeed(final File inputFile, final File outputFile, final float tempo) throws IOException {
        List<String> params = new ArrayList<>();

        params.add(ffmpegCmd);
        params.add("-i");
        params.add(inputFile.getAbsolutePath());
        params.add("-vf");
        params.add("setpts=" + 1/tempo + "*PTS");
        params.add("-af");
        params.add("atempo=" + tempo);
        params.add("-y");
        params.add("-strict");
        params.add("-2");
        params.add(outputFile.getAbsolutePath());

        logCommand(params);

        shellExecuter.executeAndWait(params);
    }

    @Override
    public void setContrast(final File inputFile, final File outputFile, final float contrast) throws IOException {
        List<String> params = new ArrayList<>();

        params.add(ffmpegCmd);
        params.add("-i");
        params.add(inputFile.getAbsolutePath());
        params.add("-vf");
        params.add("eq=contrast=" + contrast);
        params.add("-y");
        params.add("-strict");
        params.add("-2");
        params.add(outputFile.getAbsolutePath());

        logCommand(params);

        shellExecuter.executeAndWait(params);
    }

    @Override
    public void setBrightness(final File inputFile, final File outputFile, final float brightness) throws IOException {
        List<String> params = new ArrayList<>();

        params.add(ffmpegCmd);
        params.add("-i");
        params.add(inputFile.getAbsolutePath());
        params.add("-vf");
        params.add("eq=brightness=" + brightness);
        params.add("-y");
        params.add("-strict");
        params.add("-2");
        params.add(outputFile.getAbsolutePath());

        logCommand(params);

        shellExecuter.executeAndWait(params);
    }

    @Override
    public void setSaturation(final File inputFile, final File outputFile, final float saturation) throws IOException {
        List<String> params = new ArrayList<>();

        params.add(ffmpegCmd);
        params.add("-i");
        params.add(inputFile.getAbsolutePath());
        params.add("-vf");
        params.add("eq=saturation=" + saturation);
        params.add("-y");
        params.add("-strict");
        params.add("-2");
        params.add(outputFile.getAbsolutePath());

        logCommand(params);

        shellExecuter.executeAndWait(params);
    }

    @Override
    public void setAllSetting(final File inputFile, final File outputFile, final float contrast, final float brightness, final float saturation) throws IOException {
        List<String> params = new ArrayList<>();

        params.add(ffmpegCmd);
        params.add("-i");
        params.add(inputFile.getAbsolutePath());
        params.add("-vf");
        params.add("eq=" + contrast + ":" + brightness + ":" + saturation);
        params.add("-y");
        params.add("-strict");
        params.add("-2");
        params.add(outputFile.getAbsolutePath());

        logCommand(params);

        shellExecuter.executeAndWait(params);
    }

    private void logCommand(List<String> params) {
        StringBuilder cmdInfo = new StringBuilder();
        for(String param : params) {
            cmdInfo.append(param + " ");
        }
        logger.info("Run command:    " + cmdInfo);
    }
}
