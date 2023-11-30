package dk.easv.mytunes.gui.mainView;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.components.player.Player;
import dk.easv.mytunes.gui.components.songsTable.SongsTable;
import dk.easv.mytunes.gui.components.volume.VolumeControl;
import dk.easv.mytunes.gui.listeners.DataSupplier;
import dk.easv.mytunes.gui.listeners.SongSelectionListener;
import dk.easv.mytunes.gui.listeners.VolumeBinder;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import java.net.URL;
import java.util.ResourceBundle;

public class MainGuiController implements Initializable, SongSelectionListener, DataSupplier, VolumeBinder {
    private final int FIRST_INDEX=0;
    private Model model;
    private Player player;
    private VolumeControl volumeControl;

    private Alert alert;
    @FXML
    private VBox allSongsContainer;
    @FXML
    private HBox volumeControlContainer;





    public void playPreviousSong(ActionEvent event) {
    }

    public void playMusic(ActionEvent event) {
    }

    public void playNextSong(ActionEvent event) {
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alert= new Alert(Alert.AlertType.ERROR);
        try {
            this.model= Model.getModel();
        } catch (MyTunesException e) {
          alert.setContentText(e.getMessage());
          Platform.runLater(()->{
              alert.show();
          });
        }

        if(this.model!=null){
            volumeControl= new VolumeControl(this);
            volumeControlContainer.getChildren().add(FIRST_INDEX,volumeControl.getButton());
            volumeControlContainer.getChildren().add(volumeControl.getVolumeValue());
            initiateTableSong();
            this.player=Player.useMediaPlayer(this);
        }

    }


    private void initiateTableSong(){
        SongsTable allSongsTable = new SongsTable(this);
        allSongsTable.setSongs(model.getAllSongsObjects());
        allSongsContainer.getChildren().add(allSongsTable);
    }


    @Override
    public void onSongSelect(int index,String tableId) {
        model.currentIndexOffTheSongProperty().set(index);
        model.setCurrentTablePlaying(tableId);
        try{
            player.setSong(model.getCurrentSongToBePlayed(),100,true);
        }catch (MyTunesException e){
            this.alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }


    }

/** store the selected song in the table to a var if, the play button is pressed , then the song will be played */
    @Override
    public void updateTemporarySelection(int index, String tableId) {

    }



    @Override
    public Media getMedia() {
        Media media = null;
        try{
            media=model.getCurrentSongToBePlayed();
        }catch (MyTunesException e){
            this.alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
   return media;
    }



    @Override
    public boolean isPlaying() {
        return this.model.isPlayMusic();
    }

    @Override
    public void setVolumeLevel(Double volumeLevel) {
        System.out.println(this.player);
        this.model.volumeLevelProperty().set(volumeLevel/100);
    }
    @Override
    public Double getVolumeLevel(){
        return this.model.volumeLevelProperty().getValue();
    }

    @Override
    public void setIsMuteValue(boolean value) {
        this.model.isMuteProperty().setValue(value);
    }

    @Override
    public DoubleProperty getVolumeObservable() {
        return this.model.volumeLevelProperty();
    }

    @Override
    public BooleanProperty isMute() {
        return this.model.isMuteProperty();
    }
}
