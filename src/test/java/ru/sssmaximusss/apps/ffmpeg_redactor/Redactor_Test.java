package ru.sssmaximusss.apps.ffmpeg_redactor;


import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;


public class Redactor_Test
{
    private Redactor redactor;

    @Before
    public void setUp()
    {
        redactor = new RedactorImpl();
    }

    @Test
    public void testCommandGetInfoForMP4()
    {
        try {
            VideoInfo info = redactor.getInfo(new File("/home/smax/clip.mp4"));

            Assert.assertNotNull(info);
            Assert.assertNotNull(info.getBit_rate());
            Assert.assertNotNull(info.getCreation_time());
            Assert.assertNotNull(info.getDuration());
            Assert.assertNotNull(info.getFilename());
            Assert.assertNotNull(info.getHeight());
            Assert.assertNotNull(info.getWidth());
            Assert.assertNotNull(info.getInputMetaData());
            Assert.assertNotNull(info.getOutputMetaData());
            Assert.assertEquals("Duration is not correct", "218", info.getDuration().toString());
            Assert.assertEquals("Bit_rate is not correct", "1250098", info.getBit_rate().toString());
            Assert.assertEquals("Filename is not correct", "/home/smax/clip.mp4", info.getFilename());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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
            Assert.assertNotNull(info.getInputMetaData());
            Assert.assertNotNull(info.getOutputMetaData());
            Assert.assertEquals("Duration is not correct", "218", info.getDuration().toString());
            Assert.assertEquals("Bit_rate is not correct", "1250098", info.getBit_rate().toString());
            Assert.assertEquals("Filename is not correct", "/home/smax/clip.avi", info.getFilename());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCommandCutStartEnd()
    {
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
    public void testCommandCutDuration()
    {
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
