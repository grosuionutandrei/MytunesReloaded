package dk.easv.mytunes.gui.components.playListSongView;


import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.gui.components.player.Player;
import dk.easv.mytunes.gui.mainView.Model;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;


public class PlaylistSongsView extends ListView<Song> {

    public PlaylistSongsView(){
        this.setCellFactory(cell->new TitleCell<>(100,null));
        this.setPrefHeight(180);
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


}
