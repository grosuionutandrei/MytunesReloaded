package dk.easv.mytunes.dal;

import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.utility.ExceptionsMessages;
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


    private static SongReader instance;

    private SongReader() {

    }

    public static SongReader getSongReader() {
        if (instance == null) {
            instance = new SongReader();
        }
        return instance;
    }


    private Media readMedia(String path) throws MyTunesException {
        URI mediaUri;
        try {
            mediaUri = new File(path).toURI();
            return new Media(mediaUri.toString());
        } catch (NullPointerException | MediaException | IllegalArgumentException | UnsupportedOperationException e) {
            throw new MyTunesException(ExceptionsMessages.READING_SONG_LOCAL, e.getCause());
        }
    }

    @Override
    public Media getMedia(String path) throws MyTunesException {
        return this.readMedia(path);
    }

}
