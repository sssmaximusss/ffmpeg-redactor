package ru.sssmaximusss.apps.ffmpeg_redactor;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RedactorImpl implements Redactor
{

    ShellExecuter shellExecuter;


    public RedactorImpl()
    {
        shellExecuter = new ShellExecuter();
    }

    public String extract(File inputFile) throws IOException {

        List<String> params = new ArrayList<String>();

        //ffprobe
        params.add(DEFAULT_CMD_GETINFO);
        params.add("-show_format");
        params.add("-show_streams");
        params.add("-i");

        params.add(inputFile.getAbsolutePath());
        return shellExecuter.executeAndWait(params, null);

    }

    @Override
    public VideoInfo getInfo(final File inputFile) throws IOException, JSONException {
        return new VideoInfo(this, inputFile);
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
        List<String> params = new ArrayList<String>();

        //ffmpeg
        params.add(DEFAULT_CMD_EXECUTE);
        params.add("-i");
        params.add(inputFile.getAbsolutePath());
        params.add("-ss");
        params.add(start);
        params.add("-t");
        params.add(duration.toString());

        params.add("-c");
        params.add("copy");

        params.add(outputFile.getAbsolutePath());

        return shellExecuter.executeAndWait(params, null);
    }

    private int transformTime(String time) {
        int hours = Integer.parseInt(time.substring(0, 2));
        int minutes = Integer.parseInt(time.substring(3, 5));
        int seconds = Integer.parseInt(time.substring(6, 8));
        return (hours * 3600 + minutes * 60 + seconds);
    }



    public void resize(final File inputFile, final File outputFile, final int width, final int height) throws IOException {
        List<String> params = new ArrayList<String>();

        params.add(DEFAULT_CMD_EXECUTE);
        params.add("-i");
        params.add(inputFile.getAbsolutePath());
        params.add("-vf");
        params.add("scale=" + width + ":" + height);
        params.add("-y");
        params.add(outputFile.getAbsolutePath());

        shellExecuter.executeAndWait(params, null);
    }

    public void imageSetToVideo(final String inputFilePattern, final File outputFile, final Integer duration) throws IOException {
        List<String> params = new ArrayList<String>();

        params.add(DEFAULT_CMD_EXECUTE);
        params.add("-r");
        params.add("1/" + duration);
        params.add("-i");
        params.add(inputFilePattern);
        params.add("-r");
        params.add("30");
        params.add("-y");
        params.add(outputFile.getAbsolutePath());

        shellExecuter.executeAndWait(params, null);
    }

    public void imageToVideo(final File inputFile, final File outputFile, final Integer duration) throws IOException {
        List<String> params = new ArrayList<String>();

        params.add(DEFAULT_CMD_EXECUTE);
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

        shellExecuter.executeAndWait(params, null);
    }

    /*
    Attention! inputFileList should actually be a plain text file in format:
        file 'path/to/file1'
        file 'path/to/file2'
        ...
     */
    public void concatenate(final File inputFileList, final File outputFile) throws IOException {
        List<String> params = new ArrayList<String>();

        params.add(DEFAULT_CMD_EXECUTE);
        params.add("-f");
        params.add("concat");
        params.add("-i");
        params.add(inputFileList.getAbsolutePath());
        params.add("-c");
        params.add("copy");
        params.add("-y");
        params.add(outputFile.getAbsolutePath());

        shellExecuter.executeAndWait(params, null);
    }


    public void stabilizeStep1(final File inputFile, final File outputFile) throws IOException {
        List<String> params = new ArrayList<String>();

        params.add(DEFAULT_CMD_EXECUTE);
        params.add("-i");
        params.add(inputFile.getAbsolutePath());
        params.add("-vf");
        params.add("vidstabdetect=shakiness=5:show=1");
        params.add("-y");
        params.add(outputFile.getAbsolutePath());

        shellExecuter.executeAndWait(params, null);
    }
    public void stabilizeStep2(final File inputFile, final File outputFile) throws IOException {
        List<String> params = new ArrayList<String>();

        params.add(DEFAULT_CMD_EXECUTE);
        params.add("-i");
        params.add(inputFile.getAbsolutePath());
        params.add("-vf");
        params.add("vidstabtransform");
        params.add("-y");
        params.add(outputFile.getAbsolutePath());

        shellExecuter.executeAndWait(params, null);
    }
}
