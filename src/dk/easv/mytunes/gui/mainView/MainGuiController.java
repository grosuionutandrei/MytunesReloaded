package dk.easv.mytunes.gui.mainView;

import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.components.playListSongView.PlaylistContainerController;
import dk.easv.mytunes.gui.components.playListTable.PlayListSelectionHandler;
import dk.easv.mytunes.gui.components.playListTable.PlaylistTable;
import dk.easv.mytunes.gui.components.player.DataHandler;
import dk.easv.mytunes.gui.components.player.Player;
import dk.easv.mytunes.gui.components.player.PlayerCommander;
import dk.easv.mytunes.gui.components.player.PlayerControl;
import dk.easv.mytunes.gui.components.searchButton.ISearchGraphic;
import dk.easv.mytunes.gui.components.songsTable.SongsTable;
import dk.easv.mytunes.gui.components.volume.VolumeControl;
import dk.easv.mytunes.gui.components.volume.VolumeHandlerCommunication;
import dk.easv.mytunes.gui.deleteView.DeleteController;
import dk.easv.mytunes.gui.editSongView.EditSongController;
import dk.easv.mytunes.gui.filterSongs.FilterManager;
import dk.easv.mytunes.gui.listeners.*;
import dk.easv.mytunes.gui.newEditDeletePlaylist.*;
import dk.easv.mytunes.gui.newSongView.NewSongController;
import dk.easv.mytunes.gui.playOperations.PlayOperations;
import dk.easv.mytunes.gui.playOperations.PlayOperationsHandler;
import dk.easv.mytunes.gui.playlistSongsOperations.DeleteSongFromPlaylistController;
import dk.easv.mytunes.gui.playlistSongsOperations.MoveSongsController;
import dk.easv.mytunes.gui.playlistSongsOperations.PlaylistOperationHandler;
import dk.easv.mytunes.gui.playlistSongsOperations.ReloadableHandler;
import dk.easv.mytunes.gui.songSelection.PlaySongHandler;
import dk.easv.mytunes.utility.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainGuiController implements Initializable{
    private final int FIRST_INDEX = 0;
    private final int POPUP_WIDTH = 420;
    private Model model;
    private PlayerCommander playerCommander;
    private UIInitializer uiInitializer;
    private ISearchGraphic searchGraphic;
    private VolumeControl volumeControl;
    private Stage currentStage;
    private DataSupplier dataSupplier;
    private SongSelectionListener songSelectionListener;
    private VolumeBinder volumeBinder;
    private PlayListSelectionListener playListSelectionListener;
    private PlaylistReloadable playlistReloadable;
    private Reloadable reloadable;

    @FXML
    private Label infoLabel;
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
    private VBox playlistContainer;
    @FXML
    private VBox playListSongsContainer;
    private PlaylistContainerController playListContainerInitializer;
    @FXML
    private Button upButton;
    @FXML
    private Button downButton;
    private boolean errorOccurred = false;


    public void playPreviousSong(ActionEvent event) {
        this.playerCommander.processOperation(Operations.PLAY_PREVIOUS);
    }

    public void playMusic(ActionEvent event) {
        if (this.playButton.getText().equals(PlayButtonGraphic.PLAY.getValue())) {
            this.model.setPlayMusic(true);
            this.playerCommander.processOperation(Operations.PLAY);
            this.playButton.setText(PlayButtonGraphic.STOP.getValue());
        } else {
            this.model.setPlayMusic(false);
            this.playerCommander.processOperation(Operations.PAUSE);
            this.playButton.setText(PlayButtonGraphic.PLAY.getValue());
            this.model.setCurrentTime(playerCommander.getCurrentTime());
        }
    }

    public void playNextSong(ActionEvent event) {
        this.playerCommander.processOperation(Operations.PLAY_NEXT);
    }

    /**
     * handles the filter behaviour off the application
     */
    @FXML
    private void applyFilter(ActionEvent event) {
        FilterManager filterManager = new FilterManager(this.model, searchGraphic, infoLabel, searchButton, searchValue);
        filterManager.applyFilter(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.model = Model.getModel();
            this.playListSelectionListener=PlayListSelectionHandler.getInstance(this.model);
            this.volumeBinder = VolumeHandlerCommunication.getInstance(this.model);
            this.reloadable= ReloadableHandler.getInstance(this.model);
            volumeControl = new VolumeControl(volumeBinder);
            volumeControlContainer.getChildren().add(FIRST_INDEX, volumeControl.getButton());
            volumeControlContainer.getChildren().add(volumeControl.getVolumeValue());
            this.uiInitializer = UIInitializer.getInstance(this, this.model);
            this.uiInitializer.initializeSearchView(searchGraphic, searchButton, searchValue, infoLabel);
            allPlaylistTable = this.uiInitializer.initiatePlaylistTable(playlistContainer,playListSelectionListener);
            this.uiInitializer.initializeMoveButton(upButton, GraphicIdValues.UP);
            this.uiInitializer.initializeMoveButton(downButton, GraphicIdValues.DOWN);
            PlayOperations playOperations = PlayOperationsHandler.getInstance();
            this.dataSupplier = DataHandler.getInstance(this.model, playOperations);
            PlayerControl playerControl = Player.useMediaPlayer(dataSupplier);
            this.playerCommander = new PlayerCommander(dataSupplier, playerControl);
            this.playerCommander.bindMediaTimeToScreen(this.time);
            this.songSelectionListener = new PlaySongHandler(this.model, this.playerCommander, this.playButton);
            allSongsTable = this.uiInitializer.initiateTableSong(this.allSongsContainer, songSelectionListener);
            playListContainerInitializer = new PlaylistContainerController(this.playListSongsContainer, songSelectionListener, this.model.getCurrentPlayListSongs(), this.upButton, this.downButton);
            this.playlistReloadable=PlaylistOperationHandler.getInstance(this.model, playListContainerInitializer);
            this.currentPlayingSongName.textProperty().bind(this.model.currentSongPlayingNameProperty());

        } catch (MyTunesException e) {
            errorOccurred = true;
            ExceptionHandler.displayErrorAndWait(e.getMessage() + InformationalMessages.FAIL_MESSAGE_INSTRUCTIONS.getValue());
        }
    }






    @FXML
    private void openNewSongWindow(ActionEvent event) {
        try {
            Stage mainStage = Utility.getCurrentStage(event);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../newSongView/NewSongView.fxml"));
            Parent parent = loader.load();
            NewSongController news = loader.getController();
            news.setReloadableController(this.reloadable);
            Scene scene = new Scene(parent);
            Stage newSongStage = Utility.createPopupStage(mainStage, scene, Titles.ADD_NEW_SONG.getValue(), POPUP_WIDTH);
            newSongStage.show();
        } catch (IOException e) {
            ExceptionHandler.displayErrorAlert(InformationalMessages.FXML_MISSING);
        }
    }

    @FXML
    private void openEditWindow(ActionEvent event) {
        Song songToUpdate = getSelectedSong(this.allSongsTable);
        if (songToUpdate == null) {
            ExceptionHandler.displayWarningAlert(InformationalMessages.NO_SONG_SELECTED);
            return;
        }
        try {
            Stage mainStage = Utility.getCurrentStage(event);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../editSongView/EditSongView.fxml"));
            Parent parent = loader.load();
            EditSongController esc = loader.getController();
            esc.initializeEditView(songToUpdate);
            esc.setReloadableController(this.reloadable);
            Scene scene = new Scene(parent);
            Stage newSongStage = Utility.createPopupStage(mainStage, scene, Titles.EDIT_SONG.getValue(), POPUP_WIDTH);
            newSongStage.show();
        } catch (IOException e) {
            ExceptionHandler.displayErrorAlert(InformationalMessages.FXML_MISSING);
        }
    }

    /**
     * Deletes the selected song from the table
     */
    public void deleteSong(ActionEvent event) {
        Song songToDelete = getSelectedSong(this.allSongsTable);
        if (songToDelete == null) {
            ExceptionHandler.displayErrorAlert(InformationalMessages.NO_SONG_SELECTED);
            return;
        }
        DeleteController del = new DeleteController();
        del.setSongToDelete(songToDelete);
        del.setReloadable(this.reloadable);
        del.setPlaylistReloadable(playlistReloadable);
        del.initialize(null, null);
        Stage mainStage = Utility.getCurrentStage(event);
        if (del.getConfirmationWindow() != null) {
            Scene scene = new Scene(del.getConfirmationWindow());
            Stage confirmation = Utility.createPopupStage(mainStage, scene, Titles.DELETE_SONG.getValue(), POPUP_WIDTH);
            confirmation.show();
        } else {
            ExceptionHandler.displayErrorAlert(InformationalMessages.OPERATION_FAILED);
        }
    }


    private Song getSelectedSong(TableView<Song> table) {
        return table.getSelectionModel().getSelectedItem();
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
        pc.setReloadable(playlistReloadable);
        Scene scene = new Scene(root);
        Stage mainStage = Utility.getCurrentStage(event);
        Stage newPlayListStage = Utility.createPopupStage(mainStage, scene, Titles.CREATE_PLAYLIST.getValue(), POPUP_WIDTH);
        newPlayListStage.show();
    }


    /**
     * Opens a playlist editing window for a selected playlist.
     *
     * @param event The ActionEvent that triggered the opening of the window.
     */
    public void openEditPlaylistWindow(ActionEvent event) {
        PlayList playListToUpdate = getSelectedPlayList();
        if (playListToUpdate == null) {
            ExceptionHandler.displayWarningAlert(InformationalMessages.NO_PLAYLIST_SELECTED);
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../newEditDeletePlaylist/NewPlaylistView.fxml"));
            EditPlaylistController editController = new EditPlaylistController();
            loader.setController(editController);
            Parent root = loader.load();
            editController.setReloadable(playlistReloadable);
            editController.setPlaylistToEdit(playListToUpdate);
            Scene scene = new Scene(root);
            Stage mainStage = Utility.getCurrentStage(event);
            Stage newPlayListStage = Utility.createPopupStage(mainStage, scene, Titles.EDIT_PLAYLIST.getValue(), POPUP_WIDTH);
            newPlayListStage.show();
        } catch (IOException e) {
            ExceptionHandler.displayErrorAlert(InformationalMessages.FXML_MISSING);
        }
    }

    /**
     * Deletes the selected playlist and displays a confirmation window.
     *
     * @param event The ActionEvent triggering the operation.
     */
    public void deletePlaylist(ActionEvent event) {
        PlayList playlistToDelete = getSelectedPlayList();

        if (playlistToDelete == null) {
            ExceptionHandler.displayWarningAlert(InformationalMessages.NO_PLAYLIST_SELECTED);
            return;
        }

        if (isPlaylistCurrentlyPlaying(playlistToDelete)) {
            ExceptionHandler.displayInformationAlert(InformationalMessages.PLAYLIST_IN_USE);
            return;
        }

        DeletePlayListController deletePlayListController = createDeletePlayListController(playlistToDelete);
        if (deletePlayListController.getConfirmationWindow() != null) {
            Stage confirmationStage = createConfirmationStage(deletePlayListController, event);
            confirmationStage.show();
        } else {
            ExceptionHandler.displayErrorAlert(InformationalMessages.OPERATION_FAILED);
        }
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
        deletePlayListController.setReloadable(playlistReloadable);
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
        Stage mainStage = Utility.getCurrentStage(event);
        Scene scene = new Scene(deletePlayListController.getConfirmationWindow());
        return Utility.createPopupStage(mainStage, scene, Titles.DELETE_PLAYLIST.getValue(), POPUP_WIDTH);
    }


    /**
     * Adds a song to the current playlist and displays a confirmation window.
     *
     * @param event The ActionEvent triggering the operation.
     */
    public void addSongToPlaylist(ActionEvent event) {
        Song selectedSong = getSelectedSong(allSongsTable);

        if (selectedSong == null) {
            ExceptionHandler.displayWarningAlert(InformationalMessages.NO_SONG_SELECTED);
            return;
        }

        try {
            PlayList playListToAdd = model.getCurrentPlayList();

            if (playListToAdd == null) {
                ExceptionHandler.displayWarningAlert(InformationalMessages.NO_PLAYLIST_SELECTED);
                return;
            }
            AddToPlayListController addToPlayListController = new AddToPlayListController(PlayListModel.getInstance(), playListToAdd, selectedSong, playlistReloadable);
            if (addToPlayListController.getConfirmationWindow() != null) {
                Stage mainStage = Utility.getCurrentStage(event);
                Scene scene = new Scene(addToPlayListController.getConfirmationWindow());
                Stage confirmation = Utility.createPopupStage(mainStage, scene, Titles.ADD_SONG_PLAYLIST.getValue(), POPUP_WIDTH);
                confirmation.show();
            } else {
                ExceptionHandler.displayErrorAlert(InformationalMessages.OPERATION_FAILED);

            }
        } catch (MyTunesException e) {
            ExceptionHandler.displayErrorAlert(e.getMessage());
        }
    }

    /**
     * Deletes the selected song from the playlist
     */
    @FXML
    private void deleteSongFromPlayList(ActionEvent event) {
        Song songToDelete = playListContainerInitializer.getSelectedSongFromPlayList();
        if (songToDelete == null) {
            ExceptionHandler.displayWarningAlert(InformationalMessages.NO_SONG_SELECTED);
            return;
        }
        try {
            DeleteSongFromPlaylistController dsfpc = new DeleteSongFromPlaylistController(songToDelete, this.model.getCurrentPlayListSongs(), PlayListModel.getInstance(), playlistReloadable);
            if (dsfpc.getConfirmationWindow() != null) {
                Scene scene = new Scene(dsfpc.getConfirmationWindow());
                Stage stage = Utility.createPopupStage(Utility.getCurrentStage(event), scene, Titles.DELETE_SONG_PLAYLIST.getValue(), POPUP_WIDTH);
                stage.show();
            } else {
                ExceptionHandler.displayErrorAlert(InformationalMessages.OPERATION_FAILED);
            }
        } catch (MyTunesException e) {
            ExceptionHandler.displayErrorAlert(e.getMessage());
        }
    }


    /**
     * Handles the songs movement in the list , initializes the moveSongsController
     * that will perform all the operations
     */
    @FXML
    private void moveSongInPlaylist(ActionEvent event) {
        this.playListContainerInitializer.disableButtons();
        int selectedSong = playListContainerInitializer.getSelectedSongIndex();
        String operation = ((Node) event.getTarget()).getId();
        PlayList currentPlayList = null;
        try {
            currentPlayList = this.model.getCurrentPlayList();
        } catch (MyTunesException e) {
            ExceptionHandler.displayErrorAlert(e.getMessage());
        }
        MoveSongsController moveSongsController = new MoveSongsController(playlistReloadable, currentPlayList, selectedSong, operation, this.model.getCurrentPlayListSongs());
        playListContainerInitializer.setListMoveFocusAndSelect(moveSongsController);
        playListContainerInitializer.updateListFocus();
    }

    private PlayList getSelectedPlayList() {

        return this.allPlaylistTable.getSelectionModel().getSelectedItem();
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public boolean isError() {
        return this.errorOccurred;
    }
}
