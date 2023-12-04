package dk.easv.mytunes.gui.mainView;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.components.playListSongView.PlaylistSongsView;
import dk.easv.mytunes.gui.components.playListTable.PlaylistTable;
import dk.easv.mytunes.gui.components.player.Player;
import dk.easv.mytunes.gui.components.searchButton.ISearchGraphic;
import dk.easv.mytunes.gui.components.searchButton.SearchGraphic;
import dk.easv.mytunes.gui.components.searchButton.UndoGraphic;
import dk.easv.mytunes.gui.components.songsTable.SongsTable;
import dk.easv.mytunes.gui.components.volume.VolumeControl;
import dk.easv.mytunes.gui.deleteView.DeleteController;
import dk.easv.mytunes.gui.editSongView.EditSongController;
import dk.easv.mytunes.gui.listeners.*;
import dk.easv.mytunes.gui.newSongView.NewSongController;
import dk.easv.mytunes.utility.GraphicIdValues;
import dk.easv.mytunes.utility.Utility;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainGuiController implements Initializable, SongSelectionListener, DataSupplier, VolumeBinder, Reloadable, PlayListSelectionListener {
    private final int FIRST_INDEX = 0;
    private final int new_editWindowWidth = 420;

    private Model model;
    private Player player;
    private ISearchGraphic searchGraphic;
    private VolumeControl volumeControl;
    private Utility utility;


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
    @FXML
    private SongsTable allSongsTable;
    @FXML
    private VBox playlistContainer;
    @FXML
    private VBox playListSongsContainer;



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
        if (searchButton.getGraphic().getId().equals(GraphicIdValues.SEARCH.getValue())) {
            if (!filter.isEmpty()) {
                searchGraphic = new UndoGraphic();
                model.applyFilter(filter);
                infoLabel.setVisible(false);
                searchButton.setGraphic(searchGraphic.getGraphic());
                this.searchValue.setText("");
                this.searchValue.setEditable(false);
            } else {
                infoLabel.setVisible(true);
            }
        } else {
            searchGraphic = new SearchGraphic();
            searchButton.setGraphic(searchGraphic.getGraphic());
            searchValue.setEditable(true);
            model.resetFilter();

        }


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alert = new Alert(Alert.AlertType.ERROR);
        searchGraphic = new SearchGraphic();
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
            initiatePlaylistTable();
            initiateSongListView();
            searchValue.textProperty().addListener((obs, oldValue, newValue) -> {
                if (infoLabel.isVisible()) {
                    infoLabel.setVisible(false);
                }
            });
            this.player = Player.useMediaPlayer(this);
            this.currentPlayingSongName.textProperty().bind(this.model.currentSongPlayingNameProperty());
            this.utility = new Utility();
        }

    }


    private void initiateTableSong() {
        allSongsTable = new SongsTable(this);
        allSongsTable.setSongs(model.getAllSongsObjectsToDisplay());
        allSongsContainer.getChildren().add(allSongsTable);
    }


    /**
     * initiate the tableview of the playlist */
    private void initiatePlaylistTable() {
        PlaylistTable allPlaylists = new PlaylistTable(this);
        try {
            allPlaylists.setSongs(model.getAllPlaylists());
        } catch (MyTunesException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }
        playlistContainer.getChildren().add(allPlaylists);
    }
    private void initiateSongListView(){
        PlaylistSongsView playlistSongsView = new PlaylistSongsView();
        playlistSongsView.setSongs(this.model.getCurrentPlayListSongs());
        playListSongsContainer.getChildren().add(playlistSongsView);
       // playlistSongsView.bindModelToPlayListSongs(this.model,this.player,this.playButton);
        VBox.setVgrow(playlistSongsView, Priority.ALWAYS);
    }
    /**
     * listen to the playlist table click events  */
    @Override
    public void onPlayListSelect(int selectedId) {
        System.out.println(selectedId + " " + "i am listening ");
        try {
            model.setPlayingPlayList(selectedId);
            System.out.println(model.getCurrentPlayingPlayListId());
        } catch (MyTunesException e) {
            displayAlert(Alert.AlertType.ERROR,e.getMessage());
        }
    }

    /**
     * controls the media player when is double-clicked on a song
     */
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
        if (this.playButton.getText().equals(">")) {
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

    @Override
    public void reloadSongsFromDB() {
        try {
            model.reloadSongsFromDB();
        } catch (MyTunesException e) {
            alert.setContentText(e.getMessage() + "\n" + "Check your internet connection");
            alert.showAndWait();
        }
    }

    @FXML
    private void openNewSongWindow(ActionEvent event) throws IOException {
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../newSongView/NewSongView.fxml"));
        Parent parent = loader.load();
        NewSongController news = loader.getController();
        news.setReloadableController(this);
        Scene scene = new Scene(parent);
        String stageName = "Add new Song";
        Stage newSongStage = popupStage(mainStage, scene, stageName);
        newSongStage.show();
    }

    @FXML
    private void openEditWindow(ActionEvent event) throws IOException {
        Song songToUpdate = getSelectedSong(this.allSongsTable);
        if (songToUpdate == null) {
            displayAlert(Alert.AlertType.INFORMATION, "No song selected");
            return;
        }

        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../editSongView/EditSongView.fxml"));
        Parent parent = loader.load();
        EditSongController esc = loader.getController();
        esc.initializeEditView(songToUpdate);
        esc.setReloadableController(this);
        Scene scene = new Scene(parent);
        String stageName = "Edit Song";
        Stage newSongStage = popupStage(mainStage, scene, stageName);
        newSongStage.show();
    }

    private void displayAlert(Alert.AlertType type, String message) {
        alert.setAlertType(type);
        alert.setContentText(message);
        alert.show();
    }

    private Song getSelectedSong(TableView<Song> table) {
        return table.getSelectionModel().getSelectedItem();
    }

    /**
     * initiate a  new stage that will block all the events, besides the current window
     */
    private Stage popupStage(Stage mainStage, Scene scene, String name) {
        Stage newSongStage = new Stage();
        newSongStage.setX(utility.calculateMidPoint(mainStage.getX(), mainStage.getWidth(), this.new_editWindowWidth));
        newSongStage.setY(mainStage.getHeight() / 2);
        newSongStage.setTitle(name);
        newSongStage.setScene(scene);
        newSongStage.initModality(Modality.WINDOW_MODAL);
        newSongStage.initOwner(mainStage);
        return newSongStage;
    }

    /**
     * Deletes the selected song from the table
     */
    public void deleteSong(ActionEvent event) throws IOException {
        Song songToDelete = getSelectedSong(this.allSongsTable);
        if (songToDelete == null) {
            displayAlert(Alert.AlertType.INFORMATION, "No song selected");
            return;
        }
        DeleteController del = new DeleteController();
        del.initialize(null, null);
        del.setSongToDelete(songToDelete);
        del.setReloadable(this);
        Stage mainStage = getCurrentStage(event);
        Scene scene = new Scene(del.getConfirmationWindow());
        Stage confirmation = popupStage(mainStage, scene, "Delete Song");
        confirmation.show();
    }


    private Stage getCurrentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }



}
