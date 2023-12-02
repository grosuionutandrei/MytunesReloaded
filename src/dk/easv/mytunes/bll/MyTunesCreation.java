package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.dal.ISongReader;
import dk.easv.mytunes.dal.ISongsDao;
import dk.easv.mytunes.dal.SongReader;
import dk.easv.mytunes.dal.SongsDao;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.utility.SongFormat;
import javafx.util.Duration;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import org.tritonus.share.sampled.file.TAudioFileFormat;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class MyTunesCreation {
    private static MyTunesCreation instance;
    private ISongsDao songsDao;
    private ISongReader songReader;


    private MyTunesCreation() throws MyTunesException {
        songsDao = SongsDao.getSongsDao();
        songReader = SongReader.getSongReader();
    }

    public static MyTunesCreation getInstance() throws MyTunesException {
        if (instance == null) {
            instance = new MyTunesCreation();
        }
        return instance;
    }


    public void createNewSong(String path, String title, String artist, String genre,String songDuration) throws MyTunesException {

        Song song = new Song(path,title,artist,genre,Double.parseDouble(songDuration));
        System.out.println(song);
        songsDao.createSong(song);
    }
/** extract format off the song, in order to know the file format  */
    public SongFormat extractFormat(String name) throws MyTunesException {
       SongFormat songFormat = null;
        int index = name.lastIndexOf('.');
       String format = "";
       if(index>0 && index<name.length()-1){
           format=name.substring(index+1);
       }
        System.out.println(format);
       SongFormat[] songFormats = SongFormat.values();
       for(SongFormat elem:songFormats){
           if(elem.getValue().equalsIgnoreCase(format)){
               songFormat=elem;
           }
       }
       if(songFormat==null){
           throw  new MyTunesException("Format not supported.Supported files:MP3,WAV!");
       }
       return songFormat;
    }
/** get the time duration in seconds for the WAV files*/
    private float getDurationWav(File file) throws MyTunesException {
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(file);
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new MyTunesException("File not supported");
        }
        AudioFormat format = audioInputStream.getFormat();
        long audioFileLength = file.length();
        int frameSize = format.getFrameSize();
        float frameRate = format.getFrameRate();
        return ((audioFileLength / (frameSize * frameRate)));
    }
    /**get time duration for the mp3 file*/
    private double getDurationMp3(File file) throws MyTunesException{
        double time = 0.0;
        AudioFileFormat fileFormat = null;
        try {
            fileFormat = new MpegAudioFileReader().getAudioFileFormat(file);
            if (fileFormat instanceof TAudioFileFormat) {
                Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
                String key = "duration";
                Long microseconds = (Long) properties.get(key);

                double  durationInSeconds = microseconds / 1_000_000.0;

                System.out.println(durationInSeconds);
                time=durationInSeconds;
            } else {
              throw new  UnsupportedOperationException() ;
            }
        } catch (UnsupportedAudioFileException e) {
            throw new MyTunesException("File not supported");
        } catch (IOException e) {
            throw new MyTunesException("Can not read file");
        }
       return time;
    }
    public double getSongDuration(File file,SongFormat format) throws MyTunesException {
        if(format==SongFormat.WAV){
           return getDurationWav(file);
        }
       return getDurationMp3(file);
    }


}
