package dk.easv.mytunes.utility;

import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.components.movebutton.DownGraphic;
import dk.easv.mytunes.gui.components.movebutton.MoveButton;
import dk.easv.mytunes.gui.components.movebutton.UpGraphic;
import dk.easv.mytunes.gui.components.playListTable.PlaylistTable;
import dk.easv.mytunes.gui.components.searchButton.ISearchGraphic;
import dk.easv.mytunes.gui.components.searchButton.SearchGraphic;
import dk.easv.mytunes.gui.components.songsTable.SongsTable;
import dk.easv.mytunes.gui.listeners.PlayListSelectionListener;
import dk.easv.mytunes.gui.listeners.SongSelectionListener;
import dk.easv.mytunes.gui.mainView.MainGuiController;
import dk.easv.mytunes.gui.mainView.Model;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class UIInitializer {

    private final Model model;
    private static UIInitializer instance;

    public static UIInitializer getInstance( Model model) {
        if (instance == null) {
            instance = new UIInitializer(model);
        }
        return instance;
    }

    private UIInitializer( Model model) {
        this.model = model;
    }

    /**
     * initializes the filter view
     */
    public void initializeSearchView(ISearchGraphic searchGraphic, Button searchButton, TextField searchValue, Label infoLabel) {
        searchGraphic = new SearchGraphic();
        searchButton.setGraphic(searchGraphic.getGraphic());
        searchValue.textProperty().addListener((obs, oldValue, newValue) -> {
            if (infoLabel.isVisible()) {
                infoLabel.setVisible(false);
            }
        });
    }

    /**
     * initializes the song table view
     */
    public SongsTable initiateTableSong(VBox allSongsContainer, SongSelectionListener songSelectionListener) {
        SongsTable allSongsTable = new SongsTable(songSelectionListener);
        allSongsTable.setSongs(model.getAllSongsObjectsToDisplay());
        allSongsContainer.getChildren().add(allSongsTable);
        return allSongsTable;
    }

    /**
     * initiate the tableview of the playlist
     */
    public PlaylistTable initiatePlaylistTable(VBox playlistContainer, PlayListSelectionListener playListSelectionListener) throws MyTunesException {
        PlaylistTable allPlaylistTable = new PlaylistTable(playListSelectionListener);
        allPlaylistTable.setSongs(model.getAllPlaylists());
        playlistContainer.getChildren().add(allPlaylistTable);
        return allPlaylistTable;
    }


    /**
     * Initializes the move buttons
     */
    public void initializeMoveButton(Button button, GraphicIdValues graphicType) {
        MoveButton moveButton = (graphicType == GraphicIdValues.UP) ? new UpGraphic() : new DownGraphic();
        button.setGraphic(moveButton.getGraphic());
    }


}