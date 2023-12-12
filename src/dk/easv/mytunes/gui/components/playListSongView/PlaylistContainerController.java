package dk.easv.mytunes.gui.components.playListSongView;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.gui.listeners.MoveFocusAndSelect;
import dk.easv.mytunes.gui.listeners.SongSelectionListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PlaylistContainerController {

    @FXML
    private Button upButton;
    @FXML
    private Button downButton;
    @FXML
    private PlaylistSongsView playlistSongsView;
    private SongSelectionListener songSelectionListener;


    public void setSongSelectionListener(SongSelectionListener songSelectionListener) {
        this.songSelectionListener = songSelectionListener;
    }

    public PlaylistContainerController(VBox playlistContainer, SongSelectionListener songSelectionListener, ObservableList<Song> songsData, Button upButton, Button downButton) {
        this.songSelectionListener = songSelectionListener;
        this.upButton = upButton;
        this.downButton = downButton;
        initializePlaylistSongsView(songSelectionListener, songsData);
        addListViewToContainer(playlistContainer, this.playlistSongsView);
    }

    private void initializePlaylistSongsView(SongSelectionListener songSelectionListener, ObservableList<Song> songsData) {
        playlistSongsView = new PlaylistSongsView();
        this.playlistSongsView.setSongs(songsData);
        this.playlistSongsView.setSongSelectionListener(songSelectionListener);
        VBox.setVgrow(this.playlistSongsView, Priority.ALWAYS);
    }

    private void addListViewToContainer(VBox container, ListView<Song> songsView) {
        container.getChildren().add(songsView);
    }


    public PlaylistSongsView getPlaylistSongsView() {
        return playlistSongsView;
    }


    public void disableButtons() {
        this.upButton.setDisable(true);
        this.downButton.setDisable(true);
    }

    public void enableButtons() {
        this.upButton.setDisable(false);
        this.downButton.setDisable(false);
    }

    public int getSelectedSongIndex() {
        return this.playlistSongsView.getSelectionModel().getSelectedIndex();
    }

    public void setListMoveFocusAndSelect(MoveFocusAndSelect moveFocusAndSelect) {
        this.playlistSongsView.setMoveFocusAndSelect(moveFocusAndSelect);
    }

    public void updateListFocus() {
        this.playlistSongsView.updateFocus();
    }

    public Song getSelectedSongFromPlayList() {
        return this.playlistSongsView.getSelectionModel().getSelectedItem();
    }
}
