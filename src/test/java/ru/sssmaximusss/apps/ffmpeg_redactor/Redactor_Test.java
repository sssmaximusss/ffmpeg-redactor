package ru.sssmaximusss.apps.ffmpeg_redactor;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.sssmaximusss.apps.ffmpeg_redactor.Info.VideoInfo;
import ru.sssmaximusss.apps.ffmpeg_redactor.Info.VideoInfoParser;

import java.io.File;
import java.io.IOException;
import java.util.Map;


public class Redactor_Test {
    private Redactor redactor;

    @Before
    public void setUp() {
        redactor = new RedactorImpl();
    }

    @Test
    public void testCommandGetInfoForMP4() {
        try {
            VideoInfo info = redactor.getInfo(new File("/home/smax/clip.mp4"));

            Assert.assertNotNull(info);
            Assert.assertNotNull(info.getBit_rate());
            Assert.assertNotNull(info.getCreationTime());
            Assert.assertNotNull(info.getDuration());
            Assert.assertNotNull(info.getFilename());
            Assert.assertNotNull(info.getHeight());
            Assert.assertNotNull(info.getWidth());
            Assert.assertNotNull(info.getVideoStream());
            Assert.assertNotNull(info.getAudioStream());
            Assert.assertEquals("Filename is not correct", info.getFilename(), "/home/smax/clip.mp4");
            Assert.assertEquals("Creation time is not correct.", info.getCreationTime(), "2015-02-24 00:02:27");
            Assert.assertEquals("Duration is not correct", info.getDuration().longValue(), 218);
            Assert.assertEquals("Duration(string) is not correct.", info.getDurationString(), "00:03:38");
            Assert.assertEquals("Bit_rate is not correct", info.getBit_rate().longValue(), 1250098);
            Assert.assertEquals("Width is not correct.", info.getWidth().longValue(), 1280);
            Assert.assertEquals("Height is not correct.", info.getHeight().longValue(), 720);
            Assert.assertEquals("DAR is not correct.", info.getVideoStream().get("dar").toString(), "16:9");
            Assert.assertEquals("Video codec is not correct.", info.getVideoStream().get("codec_name").toString(), "h264");
            Assert.assertEquals("Video bit_rate is not correct.", info.getVideoStream().get("bit_rate"), 1055616);
            Assert.assertEquals("Audio codec is not correct.", info.getAudioStream().get("codec_name").toString(), "aac");
            Assert.assertEquals("Audio bit_rate is not correct.", info.getAudioStream().get("bit_rate"), 192008);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCommandGetInfoForAvi() {
        try {
            VideoInfo info = redactor.getInfo(new File("/home/smax/clip.avi"));

            Assert.assertNotNull(info);
            Assert.assertNotNull(info.getBit_rate());
            Assert.assertNotNull(info.getDuration());
            Assert.assertNotNull(info.getFilename());
            Assert.assertNotNull(info.getHeight());
            Assert.assertNotNull(info.getWidth());
            Assert.assertNotNull(info.getVideoStream());
            Assert.assertNotNull(info.getAudioStream());
            Assert.assertEquals("Filename is not correct", info.getFilename(), "/home/smax/clip.avi");
            Assert.assertEquals("Creation time is not correct.", info.getCreationTime(), "");
            Assert.assertEquals("Duration is not correct", info.getDuration().longValue(), 218);
            Assert.assertEquals("Duration(string) is not correct.", info.getDurationString(), "00:03:38");
            Assert.assertEquals("Bit_rate is not correct", info.getBit_rate().longValue(), 861675);
            Assert.assertEquals("Width is not correct.", info.getWidth().longValue(), 1280);
            Assert.assertEquals("Height is not correct.", info.getHeight().longValue(), 720);
            Assert.assertEquals("DAR is not correct.", info.getVideoStream().get("dar").toString(), "16:9");
            Assert.assertEquals("Video codec is not correct.", info.getVideoStream().get("codec_name").toString(), "mpeg4");
            Assert.assertEquals("Video bit_rate is not correct.", info.getVideoStream().get("bit_rate"), 659224);
            Assert.assertEquals("Audio codec is not correct.", info.getAudioStream().get("codec_name").toString(), "ac3");
            Assert.assertEquals("Audio bit_rate is not correct.", info.getAudioStream().get("bit_rate"), 192000);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void TestVideoInfoParser() {
        try {
            VideoInfoParser parser = new VideoInfoParser();

            String rawInfo = "{\n" +
                    "    \"streams\": [\n" +
                    "        {\n" +
                    "            \"index\": 0,\n" +
                    "            \"codec_name\": \"h264\",\n" +
                    "            \"codec_long_name\": \"H.264 / AVC / MPEG-4 AVC / MPEG-4 part 10\",\n" +
                    "            \"profile\": \"High\",\n" +
                    "            \"codec_type\": \"video\",\n" +
                    "            \"codec_time_base\": \"1001/48000\",\n" +
                    "            \"codec_tag_string\": \"avc1\",\n" +
                    "            \"codec_tag\": \"0x31637661\",\n" +
                    "            \"width\": 1280,\n" +
                    "            \"height\": 720,\n" +
                    "            \"coded_width\": 1280,\n" +
                    "            \"coded_height\": 720,\n" +
                    "            \"has_b_frames\": 0,\n" +
                    "            \"sample_aspect_ratio\": \"1:1\",\n" +
                    "            \"display_aspect_ratio\": \"16:9\",\n" +
                    "            \"pix_fmt\": \"yuv420p\",\n" +
                    "            \"level\": 31,\n" +
                    "            \"chroma_location\": \"left\",\n" +
                    "            \"refs\": 1,\n" +
                    "            \"is_avc\": \"1\",\n" +
                    "            \"nal_length_size\": \"4\",\n" +
                    "            \"r_frame_rate\": \"24000/1001\",\n" +
                    "            \"avg_frame_rate\": \"24000/1001\",\n" +
                    "            \"time_base\": \"1/24000\",\n" +
                    "            \"start_pts\": 0,\n" +
                    "            \"start_time\": \"0.000000\",\n" +
                    "            \"duration_ts\": 5251246,\n" +
                    "            \"duration\": \"218.801917\",\n" +
                    "            \"bit_rate\": \"1055616\",\n" +
                    "            \"bits_per_raw_sample\": \"8\",\n" +
                    "            \"nb_frames\": \"5246\",\n" +
                    "            \"disposition\": {\n" +
                    "                \"default\": 1,\n" +
                    "                \"dub\": 0,\n" +
                    "                \"original\": 0,\n" +
                    "                \"comment\": 0,\n" +
                    "                \"lyrics\": 0,\n" +
                    "                \"karaoke\": 0,\n" +
                    "                \"forced\": 0,\n" +
                    "                \"hearing_impaired\": 0,\n" +
                    "                \"visual_impaired\": 0,\n" +
                    "                \"clean_effects\": 0,\n" +
                    "                \"attached_pic\": 0\n" +
                    "            },\n" +
                    "            \"tags\": {\n" +
                    "                \"creation_time\": \"1970-01-01 00:00:00\",\n" +
                    "                \"language\": \"und\",\n" +
                    "                \"handler_name\": \"VideoHandler\"\n" +
                    "            }\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"index\": 1,\n" +
                    "            \"codec_name\": \"aac\",\n" +
                    "            \"codec_long_name\": \"AAC (Advanced Audio Coding)\",\n" +
                    "            \"profile\": \"LC\",\n" +
                    "            \"codec_type\": \"audio\",\n" +
                    "            \"codec_time_base\": \"1/44100\",\n" +
                    "            \"codec_tag_string\": \"mp4a\",\n" +
                    "            \"codec_tag\": \"0x6134706d\",\n" +
                    "            \"sample_fmt\": \"fltp\",\n" +
                    "            \"sample_rate\": \"44100\",\n" +
                    "            \"channels\": 2,\n" +
                    "            \"channel_layout\": \"stereo\",\n" +
                    "            \"bits_per_sample\": 0,\n" +
                    "            \"r_frame_rate\": \"0/0\",\n" +
                    "            \"avg_frame_rate\": \"0/0\",\n" +
                    "            \"time_base\": \"1/44100\",\n" +
                    "            \"start_pts\": 0,\n" +
                    "            \"start_time\": \"0.000000\",\n" +
                    "            \"duration_ts\": 9649152,\n" +
                    "            \"duration\": \"218.801633\",\n" +
                    "            \"bit_rate\": \"192008\",\n" +
                    "            \"nb_frames\": \"9423\",\n" +
                    "            \"disposition\": {\n" +
                    "                \"default\": 1,\n" +
                    "                \"dub\": 0,\n" +
                    "                \"original\": 0,\n" +
                    "                \"comment\": 0,\n" +
                    "                \"lyrics\": 0,\n" +
                    "                \"karaoke\": 0,\n" +
                    "                \"forced\": 0,\n" +
                    "                \"hearing_impaired\": 0,\n" +
                    "                \"visual_impaired\": 0,\n" +
                    "                \"clean_effects\": 0,\n" +
                    "                \"attached_pic\": 0\n" +
                    "            },\n" +
                    "            \"tags\": {\n" +
                    "                \"creation_time\": \"2015-02-24 00:02:28\",\n" +
                    "                \"language\": \"eng\",\n" +
                    "                \"handler_name\": \"IsoMedia File Produced by Google, 5-11-2011\"\n" +
                    "            }\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"format\": {\n" +
                    "        \"filename\": \"clip.mp4\",\n" +
                    "        \"nb_streams\": 2,\n" +
                    "        \"nb_programs\": 0,\n" +
                    "        \"format_name\": \"mov,mp4,m4a,3gp,3g2,mj2\",\n" +
                    "        \"format_long_name\": \"QuickTime / MOV\",\n" +
                    "        \"start_time\": \"0.000000\",\n" +
                    "        \"duration\": \"218.801667\",\n" +
                    "        \"size\": \"34190449\",\n" +
                    "        \"bit_rate\": \"1250098\",\n" +
                    "        \"probe_score\": 100,\n" +
                    "        \"tags\": {\n" +
                    "            \"major_brand\": \"mp42\",\n" +
                    "            \"minor_version\": \"0\",\n" +
                    "            \"compatible_brands\": \"isommp42\",\n" +
                    "            \"creation_time\": \"2015-02-24 00:02:27\"\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            Map<String, Map<String, Object>> parsingInfo = parser.parse(rawInfo);
            Assert.assertEquals("Filename is not correct.", parsingInfo.get("format").get("filename").toString(), "clip.mp4");
            Assert.assertEquals("Duration is not correct.", parsingInfo.get("format").get("duration"), 218);
            Assert.assertEquals("Duration(string) is not correct.", parsingInfo.get("format").get("duration_string").toString(), "00:03:38");
            Assert.assertEquals("Bit_rate is not correct.", parsingInfo.get("format").get("bit_rate"), 1250098);
            Assert.assertEquals("Creation time is not correct.", parsingInfo.get("format").get("creation_time").toString(), "2015-02-24 00:02:27");
            Assert.assertEquals("Width is not correct.", parsingInfo.get("video").get("width"), 1280);
            Assert.assertEquals("Height is not correct.", parsingInfo.get("video").get("height"), 720);
            Assert.assertEquals("DAR is not correct.", parsingInfo.get("video").get("dar").toString(), "16:9");
            Assert.assertEquals("Video codec is not correct.", parsingInfo.get("video").get("codec_name").toString(), "h264");
            Assert.assertEquals("Video bit_rate is not correct.", parsingInfo.get("video").get("bit_rate"), 1055616);
            Assert.assertEquals("Audio codec is not correct.", parsingInfo.get("audio").get("codec_name").toString(), "aac");
            Assert.assertEquals("Audio bit_rate is not correct.", parsingInfo.get("audio").get("bit_rate"), 192008);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCommandCutStartEnd() {
        try {

            String cutStartEnd = redactor.cut(new File("/home/smax/clip.mp4"), new File("/home/smax/out.mp4"), "00:02:00", "00:02:05");
            Assert.assertNotNull(cutStartEnd);

        } catch (IllegalArgumentException e) {
            System.out.println("Error. Start time < end time.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCommandCutDuration() {
        try {

            String cutDuration = redactor.cut(new File("/home/smax/clip.mp4"), new File("/home/smax/out2.mp4"), "00:02:00", 5);
            Assert.assertNotNull(cutDuration);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
