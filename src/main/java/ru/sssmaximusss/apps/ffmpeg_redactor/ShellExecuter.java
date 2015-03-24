package ru.sssmaximusss.apps.ffmpeg_redactor;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShellExecuter {

    Map<String, String> env;
    StringBuilder output;

    public ShellExecuter() {
        env = new HashMap<String, String>();
    }

    private Process execute() throws IOException {
        List<String> noParams = Collections.emptyList();
        return execute(noParams, null);
    }

    private Process execute(List<String> cmd, File dir) throws IOException {
        return createProcess(cmd, dir).start();
    }

    private ProcessBuilder createProcess(List<String> cmds, File dir) {
        if (env.isEmpty()) {
            return new ProcessBuilder(cmds);
        }

        ProcessBuilder processBuilder = new ProcessBuilder(cmds);

        if (dir != null) {
            processBuilder.directory(dir);
        }

        Map<String, String> currentEnv = processBuilder.environment();
        for (String key : env.keySet()) {
            currentEnv.put(key, env.get(key));
        }

        /*
         Merging both StdOutStream and ErrorStream in one, so we could deal with them concurrently
         Sadly, it doesn't seem to work properly (further investigation needed?)
         */
        //processBuilder.redirectErrorStream(true);

        return processBuilder;
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
            e.printStackTrace();
        }

        return output.toString();
    }

    public void setEnvironmentVariable(String key, String value) {
        env.put(key, value);
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
                e.printStackTrace();
            } finally {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
