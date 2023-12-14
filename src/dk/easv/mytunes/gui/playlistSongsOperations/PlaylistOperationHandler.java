package dk.easv.mytunes.gui.playlistSongsOperations;

import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.components.playListSongView.PlaylistContainerController;
import dk.easv.mytunes.gui.listeners.PlaylistReloadable;
import dk.easv.mytunes.gui.mainView.Model;
import dk.easv.mytunes.utility.ExceptionHandler;

public class PlaylistOperationHandler implements PlaylistReloadable {

    private Model model;
    private PlaylistContainerController playListContainerInitializer;

    private static PlaylistOperationHandler instance;

    private PlaylistOperationHandler(Model model, PlaylistContainerController playlistContainerController){
        this.model=model;
        this.playListContainerInitializer=playlistContainerController;
    }

    public static PlaylistOperationHandler getInstance(Model model,PlaylistContainerController playlistOperationController){
        if(instance==null){
            instance=new PlaylistOperationHandler(model,playlistOperationController);
        }
        return instance;
    }



    /**
     * reloads all the playlists from the database
     */
    @Override
    public void reloadPlaylistsFromDb() {
        try {
            this.model.reloadPlayListsFromDB();
        } catch (MyTunesException e) {
            ExceptionHandler.displayErrorAlert(e.getMessage());
        }
    }

    /**
     * reloads the playlist songs from the database
     */
    @Override
    public void reloadSongs() {
        try {
            this.model.reloadPlayListSongs();
        } catch (MyTunesException e) {
            ExceptionHandler.displayErrorAlert(e.getMessage());
        }
    }

    /**
     * Enables the moving song up and down  buttons
     */
    @Override
    public void resetButtons() {
        this.playListContainerInitializer.enableButtons();
    }

    /**
     * Changes the current playing index
     */
    @Override
    public void changePlayingIndex(int index) {
        this.model.setCurrentIndexOffTheSong(index);
    }

}
