package dk.easv.mytunes.gui.playlistSongsOperations;

import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.listeners.Reloadable;
import dk.easv.mytunes.gui.mainView.Model;
import dk.easv.mytunes.utility.ExceptionHandler;
import dk.easv.mytunes.utility.ExceptionsMessages;

public class ReloadableHandler implements Reloadable {
    private Model model;
    private static ReloadableHandler instance;

    private ReloadableHandler(Model model){
        this.model=model;
    }

    public static  ReloadableHandler getInstance(Model model){
        if(instance==null){
            instance=new ReloadableHandler(model);
        }
        return instance;
    }



    @Override
    public void reloadSongsFromDB() {
        try {
            model.reloadSongsFromDB();
        } catch (MyTunesException e) {
            ExceptionHandler.displayErrorAlert(ExceptionsMessages.READING_FROMDB_FAILED);
        }
    }
}
