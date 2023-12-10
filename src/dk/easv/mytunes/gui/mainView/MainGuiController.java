package dk.easv.mytunes.gui.mainView;
import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.components.movebutton.DownGraphic;
import dk.easv.mytunes.gui.components.movebutton.MoveButton;
import dk.easv.mytunes.gui.components.movebutton.UpGraphic;
import dk.easv.mytunes.gui.components.playListSongView.PlaylistSongsView;
import dk.easv.mytunes.gui.components.playListTable.PlaylistTable;
import dk.easv.mytunes.gui.components.player.Player;
import dk.easv.mytunes.gui.components.searchButton.ISearchGraphic;
import dk.easv.mytunes.gui.components.searchButton.SearchGraphic;
import dk.easv.mytunes.gui.components.songsTable.SongsTable;
import dk.easv.mytunes.gui.components.volume.VolumeControl;
import dk.easv.mytunes.gui.deleteView.DeleteController;
import dk.easv.mytunes.gui.editSongView.EditSongController;
import dk.easv.mytunes.gui.filterSongs.FilterManager;
import dk.easv.mytunes.gui.listeners.*;
import dk.easv.mytunes.gui.newEditDeletePlaylist.AddToPlayListController;
import dk.easv.mytunes.gui.newEditDeletePlaylist.DeletePlayListController;
import dk.easv.mytunes.gui.newEditDeletePlaylist.EditPlaylistController;
import dk.easv.mytunes.gui.newEditDeletePlaylist.NewPlaylistController;
import dk.easv.mytunes.gui.newSongView.NewSongController;
import dk.easv.mytunes.gui.playlistSongsOperations.DeleteSongFromPlaylistController;
import dk.easv.mytunes.gui.playlistSongsOperations.MoveSongsController;
import dk.easv.mytunes.gui.songSelection.PlaySong;
import dk.easv.mytunes.utility.PlayButtonGraphic;
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

public class MainGuiController implements Initializable, SongSelectionListener, DataSupplier, VolumeBinder, Reloadable, PlayListSelectionListener, PlaylistReloadable {
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
    private PlaylistTable allPlaylistTable;
    @FXML
    private PlaylistSongsView playListSongs;
    @FXML
    private VBox playlistContainer;
    @FXML
    private VBox playListSongsContainer;
    @FXML
    private Button upButton;
    @FXML
    private Button downButton;


    public void playPreviousSong(ActionEvent event) {
        player.playNextSong(this.getPreviousSong(), this.isPlaying());
    }

    public void playMusic(ActionEvent event) {
        if (this.playButton.getText().equals(PlayButtonGraphic.PLAY.getValue())) {
            this.model.setPlayMusic(true);
            this.isPlaying();
            this.player.getMediaPlayer().play();
            this.playButton.setText(PlayButtonGraphic.STOP.getValue());
        } else {
            this.model.setPlayMusic(false);
            this.isPlaying();
            this.player.getMediaPlayer().pause();
            this.playButton.setText(PlayButtonGraphic.PLAY.getValue());
            this.model.setCurrentTime(this.player.getMediaPlayer().getCurrentTime());
        }
    }

    public void playNextSong(ActionEvent event) {
        player.playNextSong(this.getNextSong(), this.isPlaying());
    }

    /**
     *handles the filter behaviour off the application*/
    @FXML
    private void applyFilter(ActionEvent event) {
        FilterManager filterManager = new FilterManager(this.model, searchGraphic, infoLabel, searchButton, searchValue);
        filterManager.applyFilter(event);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alert = new Alert(Alert.AlertType.ERROR);
        searchGraphic = new SearchGraphic();
        searchButton.setGraphic(searchGraphic.getGraphic());
        initializeUpButton();
        initializeDownButton();
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
     * initiate the tableview of the playlist
     */
    private void initiatePlaylistTable() {
        this.allPlaylistTable = new PlaylistTable(this);
        try {
            this.allPlaylistTable.setSongs(model.getAllPlaylists());
        } catch (MyTunesException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }
        playlistContainer.getChildren().add(this.allPlaylistTable);
    }

    private void initiateSongListView() {
        this.playListSongs = new PlaylistSongsView();
        this.playListSongs.setSongs(this.model.getCurrentPlayListSongs());
        this.playListSongs.setSongSelectionListener(this);
        playListSongsContainer.getChildren().add(this.playListSongs);
        VBox.setVgrow(this.playListSongs, Priority.ALWAYS);
    }

    private void initializeUpButton() {
        MoveButton moveButton = new UpGraphic();
        upButton.setGraphic(moveButton.getGraphic());
        upButton.setPrefWidth(50);
        upButton.setPrefHeight(25);
    }

    private void initializeDownButton() {
        MoveButton moveButton = new DownGraphic();
        downButton.setGraphic(moveButton.getGraphic());
        downButton.setPrefWidth(50);
        downButton.setPrefHeight(25);
    }

    /**
     * listen to the playlist table click events
     */
    @Override
    public void onPlayListSelect(int selectedId) {
        System.out.println(selectedId + " " + "i am listening ");
        try {
            model.setPlayingPlayList(selectedId);
            System.out.println(model.getCurrentPlayingPlayListId());
        } catch (MyTunesException e) {
            displayAlert(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    /**
     * Play the selected song when it is double-clicked.
     *
     * @param rowIndex The index of the selected song.
     * @param tableId  The ID of the table containing the song.
     * @param play     Whether to play the song.
     */
    @Override
    public void onSongSelect(int rowIndex, String tableId, boolean play) {
        PlaySong playSong =  new PlaySong(this.model,this.player);
        playSong.playSelectedSong(rowIndex,tableId,play,this.playButton);
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
        boolean retry = true;
        int counter = 0;
        while (retry) {
            try {
                media = model.getNextSong();
                retry = false;
            } catch (MyTunesException e) {
                this.alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.show();
            }
        }
        return media;
    }

    /**
     * provides the player with the previous song that needs to be played
     */
    @Override
    public Media getPreviousSong() {
        Media media = null;
        boolean retry = true;
        while (retry) {
            try {
                media = model.getPreviousSong();
                retry = false;
            } catch (MyTunesException e) {
                this.alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.show();
            }
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
        Stage newSongStage = createPopupStage(mainStage, scene, stageName);
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
        Stage newSongStage = createPopupStage(mainStage, scene, stageName);
        newSongStage.show();
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
        del.setSongToDelete(songToDelete);
        del.setReloadable(this);
        del.initialize(null, null);
        Stage mainStage = getCurrentStage(event);
        Scene scene = new Scene(del.getConfirmationWindow());
        Stage confirmation = createPopupStage(mainStage, scene, "Delete Song");
        confirmation.show();
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
    private Stage createPopupStage(Stage mainStage, Scene scene, String name) {
        Stage newSongStage = new Stage();
        newSongStage.setX(utility.calculateMidPoint(mainStage.getX(), mainStage.getWidth(), this.new_editWindowWidth));
        newSongStage.setY(mainStage.getHeight() / 2);
        newSongStage.setTitle(name);
        newSongStage.setScene(scene);
        newSongStage.initModality(Modality.WINDOW_MODAL);
        newSongStage.initOwner(mainStage);
        return newSongStage;
    }


    private Stage getCurrentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    /**
     * Opens a new playlist creation window.
     *
     * @param event The ActionEvent that triggered the opening of the window.
     * @throws IOException If there is an issue with loading the FXML file.
     */
    public void openNewPlaylistWindow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../newEditDeletePlaylist/NewPlaylistView.fxml"));
        NewPlaylistController pc = new NewPlaylistController();
        loader.setController(pc);
        Parent root = loader.load();
        pc.setReloadable(this);
        Scene scene = new Scene(root);
        Stage mainStage = getCurrentStage(event);
        Stage newPlayListStage = createPopupStage(mainStage, scene, "Create playlist");
        newPlayListStage.show();
    }

    /**
     * Opens a playlist editing window for a selected playlist.
     *
     * @param event The ActionEvent that triggered the opening of the window.
     * @throws IOException If there is an issue with loading the FXML file.
     */
    public void openEditPlaylistWindow(ActionEvent event) throws IOException {
        PlayList playListToUpdate = getSelectedPlayList();
        if (playListToUpdate == null) {
            displayAlert(Alert.AlertType.ERROR, "No playlist has been selected");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../newEditDeletePlaylist/NewPlaylistView.fxml"));
        EditPlaylistController editController = new EditPlaylistController();
        loader.setController(editController);
        Parent root = loader.load();
        editController.setReloadable(this);
        editController.setPlaylistToEdit(playListToUpdate);
        Scene scene = new Scene(root);
        Stage mainStage = getCurrentStage(event);
        Stage newPlayListStage = createPopupStage(mainStage, scene, "Edit playlist");
        newPlayListStage.show();
    }

    /**
     * Deletes the selected playlist and displays a confirmation window.
     *
     * @param event The ActionEvent triggering the operation.
     */
    public void deletePlaylist(ActionEvent event) {
        PlayList playlistToDelete = getSelectedPlayList();

        // Check if a playlist is selected
        if (playlistToDelete == null) {
            displayAlert(Alert.AlertType.INFORMATION, "No Playlist selected");
            return;
        }

        // Check if the playlist is currently being played
        if (isPlaylistCurrentlyPlaying(playlistToDelete)) {
            displayAlert(Alert.AlertType.INFORMATION, "The playlist that you are trying to delete is in use");
            return;
        }

        // Create and initialize the DeletePlayListController
        DeletePlayListController deletePlayListController = createDeletePlayListController(playlistToDelete);
        Stage confirmationStage = createConfirmationStage(deletePlayListController, event);

        // Show the confirmation window
        confirmationStage.show();
    }

    /**
     * Checks if the given playlist is currently being played.
     *
     * @param playlist The playlist to check.
     * @return True if the playlist is currently playing, otherwise false.
     */
    private boolean isPlaylistCurrentlyPlaying(PlayList playlist) {
        return this.model.checkPlayListCurrentPlaying(playlist);
    }

    /**
     * Creates and initializes a DeletePlayListController with the specified playlist.
     *
     * @param playlistToDelete The playlist to be deleted.
     * @return The initialized DeletePlayListController.
     */
    private DeletePlayListController createDeletePlayListController(PlayList playlistToDelete) {
        DeletePlayListController deletePlayListController = new DeletePlayListController();
        deletePlayListController.getPlayListToDelete(playlistToDelete);
        deletePlayListController.initialize(null, null);
        deletePlayListController.setReloadable(this);
        return deletePlayListController;
    }

    /**
     * Creates a confirmation stage for deleting the playlist.
     *
     * @param deletePlayListController The DeletePlayListController for the confirmation window.
     * @param event                    The ActionEvent triggering the operation.
     * @return The created confirmation stage.
     */
    private Stage createConfirmationStage(DeletePlayListController deletePlayListController, ActionEvent event) {
        Stage mainStage = getCurrentStage(event);
        Scene scene = new Scene(deletePlayListController.getConfirmationWindow());
        return createPopupStage(mainStage, scene, "Delete Playlist");
    }


    /**
     * Adds a song to the current playlist and displays a confirmation window.
     *
     * @param event The ActionEvent triggering the operation.
     */
    public void addSongToPlaylist(ActionEvent event) {
        PlayList playListToAdd;
        Song selectedSong = getSelectedSong(allSongsTable);

        try {
            playListToAdd = model.getCurrentPlayList();
            if (playListToAdd == null || selectedSong == null) {
                displayAlert(Alert.AlertType.INFORMATION, "Please select a song and a playlist");
                return;
            }
            AddToPlayListController addToPlayListController = createAddToPlayListController(playListToAdd, selectedSong);
            Stage mainStage = getCurrentStage(event);
            Scene scene = new Scene(addToPlayListController.getConfirmationWindow());
            Stage confirmation = createPopupStage(mainStage, scene, "Add Song to Playlist");
            confirmation.show();
        } catch (MyTunesException e) {
            displayAlert(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    /**
     * Creates an instance of AddToPlayListController with the provided playlist and song.
     *
     * @param playListToAdd The playlist to add the song to.
     * @param selectedSong  The song to be added to the playlist.
     * @return An instance of AddToPlayListController.
     */
    private AddToPlayListController createAddToPlayListController(PlayList playListToAdd, Song selectedSong) {
        AddToPlayListController addToPlayListController = new AddToPlayListController();
        addToPlayListController.setPlayListToAdd(playListToAdd);
        addToPlayListController.setSongToAdd(selectedSong);
        addToPlayListController.setPlaylistReloadable(this);
        addToPlayListController.initialize(null, null);
        return addToPlayListController;
    }

    @FXML
    private void deleteSongFromPlayList(ActionEvent event) {
        Song songToDelete = getSelectedSongFromPlayList();
        if (songToDelete == null) {
            displayAlert(Alert.AlertType.INFORMATION, "Please select a song to be deleted");
            return;
        }
        DeleteSongFromPlaylistController dsfpc = new DeleteSongFromPlaylistController();
        dsfpc.getSongToDelete(songToDelete, this.model.getCurrentPlayListSongs());
        dsfpc.setReloadable(this);
        dsfpc.initialize(null, null);
        Scene scene = new Scene(dsfpc.getConfirmationWindow());
        Stage stage = createPopupStage(getCurrentStage(event), scene, "Delete song from playlist");
        stage.show();
    }

    @FXML
    private void moveSongInPlaylist(ActionEvent event) {
        this.upButton.setDisable(true);
        this.downButton.setDisable(true);
        int selectedSong = this.playListSongs.getSelectionModel().getSelectedIndex();
        String operation = ((Node) event.getTarget()).getId();
        MoveSongsController moveSongsController = new MoveSongsController();
        moveSongsController.setPlaylistReloadable(this);
        PlayList currentPlayList = null;
        try {
            currentPlayList = this.model.getCurrentPlayList();
        } catch (MyTunesException e) {
            displayAlert(Alert.AlertType.ERROR, e.getMessage());
        }
        int newPosition = moveSongsController.moveSong(currentPlayList, selectedSong, operation, this.model.getCurrentPlayListSongs());
        Platform.runLater(() -> {
            selectAndFocusPlaylistItem(newPosition);

        });
    }

    /**
     * Changes the focus and select off the plyListSongs view according to the movement operation
     */
    private void selectAndFocusPlaylistItem(int newPosition) {
        if (newPosition > 0) {
            this.playListSongs.getSelectionModel().select(newPosition);
            this.playListSongs.getFocusModel().focus(newPosition);
        }
    }

    @Override
    public void reloadPlaylistsFromDb() {
        try {
            this.model.reloadPlayListsFromDB();
        } catch (MyTunesException e) {
            displayAlert(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    @Override
    public void reloadSongs() {
        try {
            this.model.reloadPlayListSongs();
        } catch (MyTunesException e) {
            displayAlert(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    @Override
    public void resetButtons() {
        this.upButton.setDisable(false);
        this.downButton.setDisable(false);
    }

    @Override
    public void changePlayingIndex(int index) {
        this.model.setCurrentIndexOffTheSong(index);
    }


    private PlayList getSelectedPlayList() {
        return this.allPlaylistTable.getSelectionModel().getSelectedItem();
    }


    /**
     * Get the selected song from the playlist songs list view
     */
    private Song getSelectedSongFromPlayList() {
        return this.playListSongs.getSelectionModel().getSelectedItem();
    }


}
