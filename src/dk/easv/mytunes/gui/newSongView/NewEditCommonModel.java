package dk.easv.mytunes.gui.newSongView;

import dk.easv.mytunes.bll.MyTunesCreation;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.utility.SongFormat;

import java.io.File;

public abstract class NewEditCommonModel {
     private MyTunesCreation myTunesCreation;


/**
 * checks if the file given at the entered path exists
 * @param filePath the path to the file to be checked*/
    public boolean checkIfFileExists(String filePath) {
        return myTunesCreation.checkFilePath(filePath);
    }

/**Checks if the title or path are empty*/
    public boolean areTitleOrPathEmpty(String title, String path) {
        return myTunesCreation.areTitleOrPathEmpty(title, path);
    }


    /**
     * get the format off the file , it is used by getDuration() method
     * @param name  it is the name off the file that is being processed */
    public SongFormat getFormat(String name) throws MyTunesException {
        return myTunesCreation.extractFormat(name);
    }
    /**
     * returns the time duration off the song in seconds
     * @param file the song for getting the time duration
     * @param songFormat represents the format off the current song*/
    public double getDuration(File file, SongFormat songFormat) throws MyTunesException {
        return myTunesCreation.getSongDuration(file,songFormat);
    }

    public MyTunesCreation getMyTunesCreation() {
        return myTunesCreation;
    }

    public void setMyTunesCreation(MyTunesCreation myTunesCreation) {
        this.myTunesCreation = myTunesCreation;
    }
}
