package dk.easv.mytunes.gui.playlistSongsOperations;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.newEditDeletePlaylist.PlayListModel;
import dk.easv.mytunes.utility.GraphicIdValues;
import javafx.scene.control.Alert;

import java.util.List;

public class MoveSongsController {
    private PlayListModel playListModel;


    public MoveSongsController() {
        try {
            this.playListModel = PlayListModel.getInstance();
        } catch (MyTunesException e) {
            displayInfoMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public int moveSong(List<Song> songs, int selectedIndex,String operation){
        Song songToMove = songs.get(selectedIndex);
        int newIndex=-1;
//        check if model is null,to implement;
        if(operation.equals(GraphicIdValues.UP.getValue())){
            newIndex=moveSongUp(songs,selectedIndex,songToMove);
        }else{
            newIndex=moveSongDown(songs,selectedIndex,songToMove);
        }
        return newIndex;
    }

    private int moveSongUp(List<Song> songs, int selectedIndex,Song songToMove) {
         int newIndex= selectedIndex-1;
        if(selectedIndex>0){
        songs.remove(selectedIndex);
        songs.add(newIndex,songToMove);
    }
        System.out.println(songs);

//        if (this.playListModel != null) {
//
//        } else {
//            return;
//        }

return newIndex;
    }


    private int moveSongDown(List<Song> songs, int selectedIndex,Song songToMove) {
        int newIndex= selectedIndex+1;
        if(selectedIndex<songs.size()-1 && selectedIndex>=0){
            songs.remove(selectedIndex);
            songs.add(newIndex,songToMove);
        }
        System.out.println(songs);

//        if (this.playListModel != null) {
//
//        } else {
//            return;
//        }

        return newIndex;
    }

    public PlayListModel getPlayListModel() {
        return playListModel;
    }

    public void setPlayListModel(PlayListModel playListModel) {
        this.playListModel = playListModel;
    }

    private void displayInfoMessage(String message, Alert.AlertType type) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setAlertType(type);
        alert.setContentText(message);
        alert.show();
    }


}
