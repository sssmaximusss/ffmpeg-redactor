package ru.sssmaximusss.apps.ffmpeg_redactor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class VideoInfo {

    // flags
    private boolean audio = false;
    private boolean video = false;

    private String inputMetaData;
    private String outputMetaData;

    private String filename;
    private String creation_time;

    private String duration_string;
    private Integer duration;
    private Integer bit_rate;

    private Integer width;
    private Integer height;

    private JSONObject format = new JSONObject();

    private List<JSONObject> streams = new ArrayList<JSONObject>();

    private JSONObject videoStream = new JSONObject();

    private JSONObject audioStream = new JSONObject();

    public VideoInfo(Redactor redactor, File inputFile) throws IOException, JSONException {
        inputMetaData = redactor.extract(inputFile);
        if (inputMetaData == null || inputMetaData.length() == 0) {
            System.out.println("Error: input data is not received.");
            return;
        }
        parseData();
        processData();
    }

    private String getCleanValue(JSONObject json, String path) throws JSONException {
        String result = json.getString(path);
        if (result != null)
            result = result.trim();
        return result;
    }


    private void parseData() throws JSONException {
        JSONObject stream = null;
        int eq;

        for (String line : inputMetaData.split("\r\n|\r|\n")) {

            if (line.equals("[STREAM]")) {
                stream = new JSONObject();
                continue;
            }
            if (line.equals("[/STREAM]")) {
                streams.add(stream);
                stream = null;
                continue;
            }
            if (line.equals("[FORMAT]") || line.equals("[/FORMAT]")) {
                continue;
            }

            // Tags
            if (line.startsWith("TAG:")) {
                line = "tags/" + line.substring(4);
            }

            // File the data
            eq = line.indexOf("=");
            if (eq != -1) {

                //remove spaces
                String key = line.substring(0, eq).replace(" ", "_");
                String value = line.substring(eq + 1);

                if (stream == null) {
                    format.put(key, value);
                } else {
                    stream.put(key, value);
                }
            }
        }
    }

    private void processData() throws JSONException {
        getPrimaryStreams();
        JSONObject mData = new JSONObject();

        // Filename
        filename = getCleanValue(format, "filename");
        mData.put("filename", filename);

        // Creation_time
        try {
            creation_time = getCleanValue(format, "tags/creation_time");
        } catch (JSONException e) {


            creation_time = "No data";
            e.printStackTrace();
        }

        mData.put("creation_time", creation_time);

        // Duration
        String dString = getCleanValue(format, "duration");
        mData.put("duration_float", dString);
        duration = Float.valueOf(dString).intValue();
        mData.put("duration", duration);
        duration_string = duration_toString(duration);

        // Bit_rate
        String brString = getCleanValue(format, "bit_rate");
        bit_rate = Integer.valueOf(brString);
        mData.put("bit_rate", bit_rate);

        // Generic format data
        JSONObject formatData = new JSONObject();
        formatData.put("simple", getCleanValue(format, "format_name"));
        formatData.put("label", getCleanValue(format, "format_long_name"));
        mData.put("format", formatData);

        // Decode Video
        width = 0;
        height = 0;
        if (videoStream != null) {
            JSONObject videoData = new JSONObject();
            String codec = getCleanValue(videoStream, "codec_name");
            if (codec != null) {
                video = true;
            }

            // Language, two options
            String lang = getCleanValue(videoStream, "tags/language");
            videoData.put("language", lang);


            // Dimensions
            String widthStr = getCleanValue(videoStream, "width");
            if (widthStr != null) {
                width = Integer.valueOf(widthStr);
                videoData.put("width", widthStr);
            }
            String heightStr = getCleanValue(videoStream, "height");
            if (heightStr != null) {
                height = Integer.valueOf(heightStr);
                videoData.put("height", heightStr);
            }

            // Codec
            JSONObject videoCodec = new JSONObject();
            videoCodec.put("tag", getCleanValue(videoStream, "codec_tag"));
            videoCodec.put("tag_string",
                    getCleanValue(videoStream, "codec_tag_string"));
            videoCodec.put("simple", getCleanValue(videoStream, "codec_name"));
            videoCodec.put("label",
                    getCleanValue(videoStream, "codec_long_name"));
            videoData.put("codec", videoCodec);
            videoData.put("pixel_format",
                    getCleanValue(videoStream, "pix_fmt"));

            // Add video to metadata
            mData.put("video", videoData);
        }

        // Decode Audio
        if (audioStream != null) {
            JSONObject audioData = new JSONObject();
            String codec = getCleanValue(audioStream, "codec_name");
            if (codec != null) {
                audio = true;
            }


            String lang = getCleanValue(audioStream, "tags/language");
            audioData.put("language", lang);

            // Codec
            JSONObject audioCodec = new JSONObject();
            audioCodec.put("tag", getCleanValue(audioStream, "codec_tag"));
            audioCodec.put("tag_string",
                    getCleanValue(audioStream, "codec_tag_string"));
            audioCodec.put("simple", getCleanValue(audioStream, "codec_name"));
            audioCodec.put("label",
                    getCleanValue(audioStream, "codec_long_name"));
            audioData.put("codec", audioCodec);

            // Sample rate
            String sample_rate = getCleanValue(audioStream, "sample_rate");
            if (sample_rate != null) {
                audioData.put("sample_rate",
                        Float.valueOf(sample_rate).intValue());
            }
            // Channels
            audioData.put("channels", getCleanValue(audioStream, "channels"));

            // Add audio to metadata
            mData.put("audio", audioData);
        }
        outputMetaData = mData.toString();
    }


    private void getPrimaryStreams() throws JSONException {
        for (JSONObject stream : streams) {
            String type = stream.get("codec_type").toString();
            if (type == null) continue;

            if (type.equals("video") && videoStream != null) {
                videoStream = stream;
            }

            if (type.equals("audio") && audioStream != null) {
                audioStream = stream;
            }
        }
    }

    private String duration_toString(Integer duration) {
        StringBuilder builder = new StringBuilder();
        int hours = duration / 3600;
        if (hours == 0) {
            builder.append("00");
        } else {
            duration -= 3600 * hours;
            if (hours < 10) {
                builder.append("0");
            }
            builder.append(hours);
        }
        builder.append(":");

        int minutes = duration / 60;
        if (minutes == 0) {
            builder.append("00");
        } else {
            duration -= 60 * minutes;
            if (minutes < 10) {
                builder.append("0");
            }
            builder.append(minutes);
        }
        builder.append(":");
        if (duration < 10) {
            builder.append("0");
        }
        builder.append(duration);
        return builder.toString();

    }

    public String getFilename() {
        return filename;
    }

    public String getCreation_time() {
        return creation_time;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getDuration_string() {
        return duration_string;
    }

    public Integer getBit_rate() {
        return bit_rate / 1000;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWidth() {
        return width;
    }

    public String getInputMetaData() {
        return inputMetaData;
    }

    public String getOutputMetaData() {
        return outputMetaData;
    }

    public JSONObject getFormat() {
        return format;
    }

    public JSONObject getVideoStream() {
        return videoStream;
    }

    public JSONObject getAudioStream() {
        return audioStream;
    }

    public boolean isAudio() {
        return audio;
    }

    public boolean isVideo() {
        return video;
    }

    @Override
    public String toString() {
        return outputMetaData;
    }

}
