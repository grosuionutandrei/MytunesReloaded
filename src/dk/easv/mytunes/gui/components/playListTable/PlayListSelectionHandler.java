package dk.easv.mytunes.gui.components.playListTable;

import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.listeners.PlayListSelectionListener;
import dk.easv.mytunes.gui.mainView.Model;
import dk.easv.mytunes.utility.ExceptionHandler;

public class PlayListSelectionHandler implements PlayListSelectionListener {
    private Model model;
    private static PlayListSelectionHandler instance;

    private PlayListSelectionHandler(Model model) {
        this.model = model;
    }

    public static PlayListSelectionHandler getInstance(Model model) {
        if (instance == null) {
            instance = new PlayListSelectionHandler(model);
        }
        return instance;
    }

    @Override
    public void onPlayListSelect(int selectedId) {
        try {
            model.setPlayingPlayList(selectedId);
        } catch (MyTunesException e) {
            ExceptionHandler.displayErrorAlert(e.getMessage());
        }
    }
}
