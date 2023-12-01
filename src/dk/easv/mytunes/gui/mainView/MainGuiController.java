package dk.easv.mytunes.gui.mainView;

import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.components.player.Player;
import dk.easv.mytunes.gui.components.searchButton.ISearchGraphic;
import dk.easv.mytunes.gui.components.searchButton.SearchGraphic;
import dk.easv.mytunes.gui.components.searchButton.UndoGraphic;
import dk.easv.mytunes.gui.components.songsTable.SongsTable;
import dk.easv.mytunes.gui.components.volume.VolumeControl;
import dk.easv.mytunes.gui.listeners.DataSupplier;
import dk.easv.mytunes.gui.listeners.SongSelectionListener;
import dk.easv.mytunes.gui.listeners.VolumeBinder;
import dk.easv.mytunes.utility.GraphicIdValues;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;

import java.net.URL;
import java.util.ResourceBundle;

public class MainGuiController implements Initializable, SongSelectionListener, DataSupplier, VolumeBinder {
    private final int FIRST_INDEX = 0;
    private Model model;
    private Player player;
    private ISearchGraphic searchGraphic;
    private VolumeControl volumeControl;
    @FXML
    private Label infoLabel;
    private Alert alert;
    @FXML
    private VBox allSongsContainer;
    @FXML
    private HBox volumeControlContainer;
    @FXML
    private Button playButton;
    @FXML
    private Label currentPlayingSongName;
    @FXML
    private Label time;
    @FXML
    private TextField searchValue;
    @FXML
    private Button searchButton;


    public void playPreviousSong(ActionEvent event) {
        player.playNextSong(this.getPreviousSong(), this.isPlaying());
    }

    public void playMusic(ActionEvent event) {
        if (this.playButton.getText().equals(">")) {
            this.model.setPlayMusic(true);
            this.isPlaying();
            this.player.getMediaPlayer().play();
            this.playButton.setText("||");
        } else {
            this.model.setPlayMusic(false);
            this.isPlaying();
            this.player.getMediaPlayer().pause();
            this.playButton.setText(">");
            this.model.setCurrentTime(this.player.getMediaPlayer().getCurrentTime());
        }
    }

    public void playNextSong(ActionEvent event) {
        player.playNextSong(this.getNextSong(), this.isPlaying());
    }


    public void applyFilter(ActionEvent event) {
        String filter = this.searchValue.getText();
        if(searchButton.getGraphic().getId().equals(GraphicIdValues.SEARCH.getValue())){
            if (!filter.isEmpty()) {
                searchGraphic=new UndoGraphic();
                model.applyFilter(filter);
                infoLabel.setVisible(false);
                searchButton.setGraphic(searchGraphic.getGraphic());
                this.searchValue.setText("");
                this.searchValue.setEditable(false);
            }else{
                infoLabel.setVisible(true);
            }
        }
        else{
            searchGraphic=new SearchGraphic();
            searchButton.setGraphic(searchGraphic.getGraphic());
            searchValue.setEditable(true);
            model.resetFilter();

        }





    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alert = new Alert(Alert.AlertType.ERROR);
        searchGraphic= new SearchGraphic();
        searchButton.setGraphic(searchGraphic.getGraphic());
        try {
            this.model = Model.getModel();
        } catch (MyTunesException e) {
            alert.setContentText(e.getMessage());
            Platform.runLater(() -> {
                alert.show();
            });
        }

        if (this.model != null) {
            volumeControl = new VolumeControl(this);
            volumeControlContainer.getChildren().add(FIRST_INDEX, volumeControl.getButton());
            volumeControlContainer.getChildren().add(volumeControl.getVolumeValue());
            initiateTableSong();
            searchValue.textProperty().addListener((obs,oldValue,newValue)->{
                if(infoLabel.isVisible()){
                    infoLabel.setVisible(false);
                }
            });
            this.player = Player.useMediaPlayer(this);
            this.currentPlayingSongName.textProperty().bind(this.model.currentSongPlayingNameProperty());
        }

    }


    private void initiateTableSong() {
        SongsTable allSongsTable = new SongsTable(this);
        allSongsTable.setSongs(model.getAllSongsObjectsToDisplay());
        allSongsContainer.getChildren().add(allSongsTable);
    }

/** controls the media player when is double-clicked on a song*/
    @Override
    public void onSongSelect(int index, String tableId, boolean play) {
        model.currentIndexOffTheSongProperty().set(index);
        model.setCurrentTablePlaying(tableId);
        model.setPlayMusic(play);
        try {
            player.setSong(model.getCurrentSongToBePlayed(), model.isPlayMusic());

        } catch (MyTunesException e) {
            this.alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
        if(this.playButton.getText().equals(">")){
            this.playButton.setText("||");
        }


    }

    /**
     * store the selected song in the table to a var if, the play button is pressed , then the song will be played
     */
    @Override
    public void updateTemporarySelection(int index, String tableId) {

    }


    /**
     * provides the media that needs to be played by the player
     */
    @Override
    public Media getMedia() {
        Media media = null;
        try {
            media = model.getCurrentSongToBePlayed();
        } catch (MyTunesException e) {
            this.alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
        return media;
    }

    /**
     * provides the player the next song that needs to be played
     */
    @Override
    public Media getNextSong() {
        Media media = null;
        try {
            media = model.getNextSong();
        } catch (MyTunesException e) {
            this.alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
        return media;
    }

    /**
     * provides the player with the previous song that needs to be played
     */
    @Override
    public Media getPreviousSong() {
        Media media = null;
        try {
            media = model.getPreviousSong();
        } catch (MyTunesException e) {
            this.alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
        return media;
    }

    /**
     * binds view label with the current time off the song
     */
    @Override
    public void bindMediaTimeToScreen(StringProperty binder) {
        this.time.textProperty().bind(binder);
    }


    /**
     * controls iff the song can be played or not
     */
    @Override
    public boolean isPlaying() {
        return this.model.isPlayMusic();
    }

    /**
     * sets the  model volume property according to the current slider value
     */
    @Override
    public void setVolumeLevel(Double volumeLevel) {
        System.out.println(this.player);
        this.model.volumeLevelProperty().set(volumeLevel / 100);
    }

    /**
     * sets the value off the slider back to the previous value
     */
    @Override
    public Double getVolumeLevel() {
        return this.model.volumeLevelProperty().getValue();
    }

    /**
     * sets the model isMute boolean propriety when the mute/unmute button is pressed
     */
    @Override
    public void setIsMuteValue(boolean value) {
        this.model.isMuteProperty().setValue(value);
    }


    /**
     * binds the volume off the player with the volume level stored in the model
     */
    @Override
    public DoubleProperty getVolumeObservable() {
        return this.model.volumeLevelProperty();
    }


    /**
     * supplies data to the player when the volume is mute
     */
    @Override
    public BooleanProperty isMute() {
        return this.model.isMuteProperty();
    }


}
