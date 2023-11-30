package dk.easv.mytunes.dal;

import dk.easv.mytunes.exceptions.MyTunesException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SongReader implements ISongReader {


    private static SongReader instance ;

    private SongReader (){

    }
    public static SongReader getSongReader (){
        if(instance==null){
            instance= new SongReader();
        }
        return instance;
    }


    private Media readMedia(String path) throws MyTunesException {
        Media media = null;
        URI mediaUri;
        try{
            mediaUri=new File(path).toURI();
            media = new Media(mediaUri.toString());
        }catch (NullPointerException| MediaException|IllegalArgumentException|UnsupportedOperationException e){
            throw new MyTunesException("Reading file from local storage went wrong",e.getCause());
        }
        return media;
    }
@Override
    public Media getMedia(String path) throws MyTunesException {
        return this.readMedia(path);
    }

}
