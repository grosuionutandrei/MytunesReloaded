package dk.easv.mytunes.gui.components.playListSongView;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.gui.listeners.SongSelectionListener;
import dk.easv.mytunes.utility.PlayingLocation;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;



public class PlaylistSongsView extends ListView<Song> {
private SongSelectionListener songSelectionListener;
    public PlaylistSongsView(){
        this.setCellFactory(cell->{
            TitleCell<Song> titleCell=new TitleCell<>(100,null);
            titleCell.setOnMouseClick(songSelectionListener,this.getId());
            return titleCell;
        });
        this.setPrefHeight(180);
        this.setId(PlayingLocation.PLAYLIST_SONGS.getValue());

    }
    public void setSongs(ObservableList<Song> songs){
        this.setItems(songs);
    }
    public void setHeight(int height){
        this.setPrefHeight(height);
    }

    public void setWidth(int width){
        this.setPrefWidth(width);
    }










//    public void bindModelToPlayListSongs(Model model, Player player, Button button){
//            this.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
//                if (newValue.intValue() >= 0) {
//                   // model.setCurrentListSelected(this.getId());
//                    //model.setCurrentPlayingList();
//                    model.setCurrentMediaPosition(newValue.intValue());
//                    player.setSong(model.getCurrentSong(),model.getVolume(),model.isPlayMusic());
//                    button.setText(">");
//                    player.bindDurationToLabel(model.timePassedProperty());
//                }
//            });
//        }


    public void setSongSelectionListener(SongSelectionListener songSelectionListener) {
        this.songSelectionListener = songSelectionListener;
    }
}
