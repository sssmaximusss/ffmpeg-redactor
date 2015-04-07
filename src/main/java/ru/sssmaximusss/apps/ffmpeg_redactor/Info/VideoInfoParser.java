package ru.sssmaximusss.apps.ffmpeg_redactor.Info;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class VideoInfoParser {

    public Map<String, Map<String, Object>> parse(String rawInfo) throws IOException, NoClassDefFoundError {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(rawInfo);

        JsonNode formatNode = rootNode.path("format");

        Map<String, Object> formatInfo = new HashMap<>();
        formatInfo.put("filename", formatNode.path("filename").asText());
        formatInfo.put("creation_time", formatNode.path("tags").path("creation_time").asText());
        formatInfo.put("duration", formatNode.path("duration").asInt());
        formatInfo.put("duration_string", duration_toString(formatNode.path("duration").asInt()));
        formatInfo.put("bit_rate", formatNode.path("bit_rate").asInt());

        Map<String, Map<String, Object>> parsingInfo = new HashMap<>();
        parsingInfo.put("format", formatInfo);

        for (JsonNode streamNode : rootNode.path("streams")) {
            Map<String, Object> streamInfo = new HashMap<>();

            String type = streamNode.path("codec_type").asText();
            streamInfo.put("codec_name", streamNode.path("codec_name").asText());
            streamInfo.put("bit_rate", streamNode.path("bit_rate").asInt());

            if (Objects.equals(type, "video")) {
                streamInfo.put("width", streamNode.path("width").asInt());
                streamInfo.put("height", streamNode.path("height").asInt());
                streamInfo.put("dar", streamNode.path("display_aspect_ratio").asText());
            }
            parsingInfo.put(type, streamInfo);
        }
        return parsingInfo;
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
}
