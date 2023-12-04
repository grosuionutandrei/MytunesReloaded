package dk.easv.mytunes.gui.deleteView;

import dk.easv.mytunes.bll.MyTunesCreation;
import dk.easv.mytunes.exceptions.MyTunesException;

public class DeleteModel {
    private MyTunesCreation myTunesCreation;
    private static DeleteModel instance;
    private DeleteModel() throws MyTunesException {
        myTunesCreation = MyTunesCreation.getInstance();
    }
    public static DeleteModel getInstance() throws MyTunesException {
        if(instance==null)
        {
            instance= new DeleteModel();
        }
        return instance;
    }

    /**
     * delete a song from the database  and from the local storage
     * @param songId the id off the song that will be deleted*/
    public boolean deleteSong(int songId, String songPath) throws MyTunesException {
        return myTunesCreation.deleteSong(songId,songPath);
    }
}
