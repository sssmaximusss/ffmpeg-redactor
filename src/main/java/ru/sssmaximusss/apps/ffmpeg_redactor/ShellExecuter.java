package ru.sssmaximusss.apps.ffmpeg_redactor;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShellExecuter {

    Map<String, String> env;

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

        ProcessBuilder process = new ProcessBuilder(cmds);

        if (dir != null) {
            process.directory(dir);
        }

        Map<String, String> currentEnv = process.environment();
        for (String key : env.keySet()) {
            currentEnv.put(key, env.get(key));
        }

        return process;
    }


    public String executeAndWait(List<String> params, File dir) throws IOException {

        final Process process = execute(params, dir);
        InputStream is = process.getInputStream();
        InputStreamReader inputReader = new InputStreamReader(is);
        BufferedReader buffReader = new BufferedReader(inputReader);

        String line = buffReader.readLine();
        StringBuilder stringBuilder = new StringBuilder();
        while (line != null) {
            stringBuilder.append(line);
            line = buffReader.readLine();
        }

        return stringBuilder.toString();
    }

    public void setEnvironmentVariable(String key, String value) {
        env.put(key, value);
    }


}
