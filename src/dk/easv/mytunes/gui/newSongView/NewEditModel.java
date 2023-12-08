package dk.easv.mytunes.gui.newSongView;

import dk.easv.mytunes.bll.MyTunesCreation;
import dk.easv.mytunes.exceptions.MyTunesException;


public class NewEditModel extends NewEditCommonModel {
    private static NewEditModel instance;


    private NewEditModel() throws MyTunesException {
        setMyTunesCreation(MyTunesCreation.getInstance());
    }

    public static NewEditModel getInstance() throws MyTunesException {
        if (instance == null) {
            instance = new NewEditModel();
        }
        return instance;
    }

    public boolean createNewSong(String path, String title, String artist, String genre, String songDuration) throws MyTunesException {
        return getMyTunesCreation().createNewSong(path, title, artist, genre, songDuration);
    }

    public boolean checkIfDuplicate(String path) throws MyTunesException {
        return getMyTunesCreation().checkIfDuplicate(path);
    }


}
