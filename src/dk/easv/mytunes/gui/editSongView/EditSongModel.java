package dk.easv.mytunes.gui.editSongView;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.bll.MyTunesCreation;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.utility.SongFormat;

import java.io.File;

public class EditSongModel {
    private static EditSongModel instance;
    private MyTunesCreation myTunesCreation;
    private Song initialSong;


    private EditSongModel() throws MyTunesException {
        myTunesCreation = MyTunesCreation.getInstance();
    }

    public static EditSongModel getInstance() throws MyTunesException {
        if (instance == null) {
            instance = new EditSongModel();
        }
        return instance;
    }

    public Song getInitialSong() {
        return initialSong;
    }

    public void setInitialSong(Song initialSong) {
        this.initialSong = initialSong;
    }

    public boolean checkIfFileExists(String filePath) {
        return myTunesCreation.checkFilePath(filePath);
    }

    public boolean areTitleOrPathEmpty(String title, String path) {
        return myTunesCreation.areTitleOrPathEmpty(title, path);
    }
    public SongFormat getFormat(String name) throws MyTunesException {
        return myTunesCreation.extractFormat(name);
    }
    public double getDuration(File file, SongFormat songFormat) throws MyTunesException {
        return myTunesCreation.getSongDuration(file,songFormat);
    }

    public boolean updateSong(Song updatedSong)throws MyTunesException {
        if(!myTunesCreation.checkIfEqual(initialSong,updatedSong)){
         if (myTunesCreation.updateSong(updatedSong)){
             return true;
         };

        }
        return false;
    }
}
