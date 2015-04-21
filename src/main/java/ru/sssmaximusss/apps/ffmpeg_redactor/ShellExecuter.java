package ru.sssmaximusss.apps.ffmpeg_redactor;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShellExecuter {

    private static final Logger logger = Logger.getLogger(RedactorImpl.class);
    StringBuilder output;

    private Process execute() throws IOException {
        List<String> noParams = Collections.emptyList();
        return execute(noParams, null);
    }

    private Process execute(List<String> cmd, File dir) throws IOException {
        return createProcess(cmd, dir).start();
    }

    private ProcessBuilder createProcess(List<String> cmds, File dir) {

        ProcessBuilder processBuilder = new ProcessBuilder(cmds);

        if (dir != null) {
            // Actually sets not where the process will be executed, but where it would look for files
            processBuilder.directory(dir);
        }

        return processBuilder;
    }


    public String executeAndWait(List<String> params) throws IOException {
        return executeAndWait(params, null);
    }

    public String executeAndWait(List<String> params, File dir) throws IOException {

        final Process process = execute(params, dir);
        InputStream is = process.getInputStream();
        InputStream es = process.getErrorStream();

        output = new StringBuilder();

        StreamConsumer inputConsumer = new StreamConsumer(is);
        StreamConsumer errorConsumer = new StreamConsumer(es);
        inputConsumer.start();
        errorConsumer.start();

        try {
            process.waitFor();
            inputConsumer.join();
            errorConsumer.join();
        } catch (InterruptedException e) {
            logger.warn(e, e);
        }

        return output.toString();
    }

    private class StreamConsumer extends Thread {
        private InputStream inputStream;

        StreamConsumer(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    output.append(line);
                    output.append("\n");
                }
            } catch (IOException e) {
                logger.warn(e, e);
            } finally {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    logger.warn(e, e);
                }
            }
        }
    }
}
