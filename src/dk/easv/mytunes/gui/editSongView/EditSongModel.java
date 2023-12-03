package dk.easv.mytunes.gui.editSongView;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.bll.MyTunesCreation;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.newSongView.NewEditCommon;

public class EditSongModel extends NewEditCommon {
    private static EditSongModel instance;

    private Song initialSong;


    private EditSongModel() throws MyTunesException {
        setMyTunesCreation( MyTunesCreation.getInstance());
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


/**
 * Check if the  updated song is different and if it is performing the update operation
 * @param updatedSong the song that will be updated */
    public boolean updateSong(Song updatedSong)throws MyTunesException {
        if(!getMyTunesCreation().checkIfEqual(initialSong,updatedSong)){
         if (getMyTunesCreation().updateSong(updatedSong)){
             return true;
         };

        }
        return false;
    }
}
