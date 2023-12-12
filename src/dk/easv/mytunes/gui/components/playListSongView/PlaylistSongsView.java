package dk.easv.mytunes.gui.components.playListSongView;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.gui.listeners.MoveFocusAndSelect;
import dk.easv.mytunes.gui.listeners.SongSelectionListener;
import dk.easv.mytunes.utility.PlayingLocation;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;


public class PlaylistSongsView extends ListView<Song> {
    public void setMoveFocusAndSelect(MoveFocusAndSelect moveFocusAndSelect) {
        this.moveFocusAndSelect = moveFocusAndSelect;
    }

    private SongSelectionListener songSelectionListener;
    private MoveFocusAndSelect moveFocusAndSelect;

    public PlaylistSongsView() {
        this.setCellFactory(cell -> {
            TitleCell<Song> titleCell = new TitleCell<>(100, null);
            titleCell.setOnMouseClick(songSelectionListener, this.getId());
            return titleCell;
        });
        this.setPrefHeight(180);
        this.setId(PlayingLocation.PLAYLIST_SONGS.getValue());
    }

    public void setSongs(ObservableList<Song> songs) {
        this.setItems(songs);
    }

    public void setHeight(int height) {
        this.setPrefHeight(height);
    }

    public void setWidth(int width) {
        this.setPrefWidth(width);
    }

    public void setSongSelectionListener(SongSelectionListener songSelectionListener) {
        this.songSelectionListener = songSelectionListener;
    }

    public void updateFocus() {
        if (moveFocusAndSelect != null) {
            int newIndex = moveFocusAndSelect.moveFocusAndSelect();
            if (newIndex >= 0 && newIndex < this.getItems().size()) {
                this.getSelectionModel().select(newIndex);
                this.getFocusModel().focus(newIndex);
            }
        }
    }



}
