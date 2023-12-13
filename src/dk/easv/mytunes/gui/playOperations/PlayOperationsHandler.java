package dk.easv.mytunes.gui.playOperations;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.mainView.Model;
import dk.easv.mytunes.utility.ExceptionHandler;
import dk.easv.mytunes.utility.Operations;
import javafx.scene.media.Media;

public class PlayOperationsHandler implements PlayOperations {
    private static PlayOperationsHandler playOperationsHandler;
    public static  PlayOperationsHandler  instance;
    private Model model;

    private PlayOperationsHandler(){

    }

    public static PlayOperationsHandler getInstance(){
        if (instance==null){
            instance=new PlayOperationsHandler();
        }
        return instance;
    }

    private Media performGetMedia(Operations operation) {
        Media media = null;
        boolean retry = true;
        int counter = 0;
        while (retry && counter < 5) {
            try {
                media = getOperation(operation);
                retry = false;
            } catch (MyTunesException e) {
                ExceptionHandler.displayErrorAlert(e.getMessage());
            }
            counter++;
        }
        return media;
    }

    private Media getOperation(Operations operation) throws MyTunesException {
        Media media = null;
        switch (operation) {
            case Operations.PLAY_NEXT:
                media =model.getNextSong();
                break;
            case Operations.PLAY_PREVIOUS:
                media =model.getPreviousSong();
                break;
            default:
                media=model.getCurrentSongToBePlayed();
                break;
        }
        return media;
    }

    @Override
    public Media getMedia(Operations operation) {
       return performGetMedia(operation);
    }

    @Override
    public void setModel(Model model) {
        this.model=model;
    }
}
