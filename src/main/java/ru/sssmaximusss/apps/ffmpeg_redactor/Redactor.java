package ru.sssmaximusss.apps.ffmpeg_redactor;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Redactor
{
    private String ffmpegCommand;

    public Redactor(String ffmpegCommand)
    {
        this.ffmpegCommand = ffmpegCommand;
    }

    protected Map<String,String> executeInquiry( String filename )
    {
        System.out.println( "Execute Inquiry for file: " + filename );
        Map<String,String> fieldMap = new HashMap<String,String>();

        try
        {
            // Build the command line
            StringBuilder sb = new StringBuilder();
            sb.append( ffmpegCommand );
            sb.append( " -i " );
            sb.append( filename );

            // Execute the command
            System.out.println( "Command line: " + sb );
            Process p = Runtime.getRuntime().exec( sb.toString() );

            // Read the response
            BufferedReader input = new BufferedReader( new InputStreamReader( p.getInputStream() ) );
            BufferedReader error = new BufferedReader( new InputStreamReader( p.getErrorStream() ) );

            // Parse the input stream
            String line = input.readLine();
            System.out.println( "ffmpeg execution of: " + filename );
            while( line != null )
            {
                System.out.println( "\t---" + line );
                line = input.readLine();
            }

            // Parse the error stream
            line = error.readLine();
            System.out.println( "Error Stream: " + filename );
            while( line != null )
            {
                // Handle the line
                if( line.startsWith( "FFmpeg version" ) )
                {
                    // Handle the version line:
                    String version = line.substring( 15, line.indexOf( ", Copyright", 16  ) );
                    fieldMap.put( "version", version );
                }
                else if(line.contains("Duration:"))
                {
                    // Handle Duration line:
                    String duration = line.substring( line.indexOf( "Duration: " ) + 10, line.indexOf( ", start:" ) );
                    fieldMap.put( "duration", duration );

                    String bitrate = line.substring( line.indexOf( "bitrate: " ) + 9 );
                    fieldMap.put( "bitrate", bitrate );
                }

                // Read the next line
                System.out.println( "\t---" + line );
                line = error.readLine();
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        // Debug: dump fields:
        System.out.println( "Fields:" );
        for( String field : fieldMap.keySet() )
        {
            System.out.println( "\t" + field + " = " + fieldMap.get( field ) );
        }

        return fieldMap;
    }

    public String getDuration( String filename )
    {
        Map<String,String> fieldMap = executeInquiry( filename );
        if( fieldMap.containsKey( "duration" ) )
        {
            return fieldMap.get( "duration" );
        }
        return "0:00";
    }


    public void generateThumbnails( String filename, File dir, int width, int height, int count )
    {
        // The following example shows how to generate thumbnails at seconds 4, 8, 12, and 16
        String duration = getDuration( filename );
        int hours = Integer.parseInt( duration.substring( 0, 2 ) );
        int minutes = Integer.parseInt( duration.substring( 3, 5 ) );
        int seconds = Integer.parseInt( duration.substring( 6, 8 ) );
        int totalSeconds = hours * 3600 + minutes * 60 + seconds;
        System.out.println( "Total Seconds: " + totalSeconds );

        // Create the thumbnails
        String shortFilename = filename;
        if(filename.contains(File.separator))
        {
            // Strip the path
            shortFilename = filename.substring(filename.lastIndexOf(File.separator) + 1);
        }
        if(shortFilename.contains("."))
        {
            // Strip extension
            shortFilename = shortFilename.substring( 0, shortFilename.lastIndexOf( "." ) );
        }
        
        // Define a shift in seconds
        int shift = 4;

        // The step is the number of seconds to step between thumbnails
        int step = totalSeconds / count;
        for( int index = 0; index < count; index++ )
        {
            // Build the command
            StringBuilder sb = new StringBuilder();
            sb.append( ffmpegCommand );
            sb.append( " -itsoffset -" );
            sb.append( Integer.toString( shift + ( index * step ) ) );
            sb.append( " -i " );
            sb.append( filename );
            sb.append( " -vcodec mjpeg -vframes 1 -an -f rawvideo -s " );
            sb.append( Integer.toString( width ) );
            sb.append( "x" );
            sb.append( Integer.toString( height ) );
            sb.append( " " );
            sb.append( dir.getAbsolutePath() );
            sb.append( File.separator );
            sb.append( shortFilename );
            sb.append( "-" );
            sb.append( Integer.toString( index + 1 ) );
            sb.append( ".jpg" );
            System.out.println( "Thumbnail command: " + sb );

            try
            {
                // Execute the command
                Process p = Runtime.getRuntime().exec( sb.toString() );

                // Detach from the process
                p.getOutputStream().close();
                consumeStream( p.getInputStream() );
                consumeStream( p.getErrorStream() );
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }
        }
    }

    protected String consumeStream( InputStream is )
    {
        StringBuilder sb = new StringBuilder();
        try
        {
            BufferedReader br = new BufferedReader( new InputStreamReader( is ) );
            String line = br.readLine();
            while( line != null )
            {
                sb.append( line );
                line = br.readLine();
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String getFfmpegCommand()
    {
        return ffmpegCommand;
    }

    public void setFfmpegCommand(String ffmpegCommand)
    {
        this.ffmpegCommand = ffmpegCommand;
    }
}
