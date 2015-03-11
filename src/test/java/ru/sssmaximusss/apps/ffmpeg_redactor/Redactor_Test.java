package ru.sssmaximusss.apps.ffmpeg_redactor;

/**
 * Created by smax on 11.03.15.
 */

import java.io.File;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class Redactor_Test
{
    private Redactor redactor;

    @Before
    public void setUp()
    {
        redactor = new Redactor( "ffmpeg" );
    }

    @Test
    public void testDuration()
    {
        String duration = redactor.getDuration( "/home/smax/clip.mp4" );
        Assert.assertNotNull( duration );
        Assert.assertEquals( "Duration is not correct", "00:03:38.80", duration );
    }

    @Test
    public void testThumbnails()
    {
        redactor.generateThumbnails("/home/smax/clip.mp4",
                new File( "/home/smax/Thumbs" ), 480, 480, 6 );
        Assert.assertTrue(true );
    }

    @Test
    public void testGetFfmpegCommand()
    {
        String ffmpegCommand = redactor.getFfmpegCommand();
        Assert.assertNotNull(ffmpegCommand);
        Assert.assertEquals("FfmpegCommand is not correct", "ffmpeg", ffmpegCommand);
    }

}
