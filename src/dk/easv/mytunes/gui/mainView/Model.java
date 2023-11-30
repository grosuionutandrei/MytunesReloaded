package dk.easv.mytunes.gui.mainView;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.bll.MyTunesLogic;
import dk.easv.mytunes.exceptions.MyTunesException;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;

public class Model {
    private final MyTunesLogic myTunesLogic;
    private static Model instance;
    private boolean playMusic = false;


    /**
     * current index off the song that is being played
     */
    private IntegerProperty currentIndexOffTheSong = new SimpleIntegerProperty(0);
    /**
     * holds the current volume off the appliation
     */
    private DoubleProperty volumeLevel = new SimpleDoubleProperty();





    /**controls if the application  volume is mute */
    private BooleanProperty isMute = new SimpleBooleanProperty();

    /**
     * holds the id off the current table that is being played
     */
    private String currentTablePlaying;


    /**
     * Holds all songs off the application , it is used to display songs in the all songs view table
     */
    private ObservableList<Song> allSongsObjects;
    /**
     * holds the current selected playlist Songs
     */
    private ObservableList<Song> playListSongs;

    private Media currentPlayingMedia;


    private Model() throws MyTunesException {
        myTunesLogic = MyTunesLogic.getMyTuneLogic();
        allSongsObjects = FXCollections.observableArrayList();
        populateAllSongsList();
        currentPlayingMedia = myTunesLogic.getMediaToBePlayed(currentIndexOffTheSong.getValue(), allSongsObjects);
        volumeLevel.setValue(100);
        isMute.setValue(false);
    }

    public static Model getModel() throws MyTunesException {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }


    private void populateAllSongsList() throws MyTunesException {
        this.allSongsObjects.addAll(myTunesLogic.getAllSongs());
    }

    /**
     * gets the current song that needs to be played
     */
    public Media getCurrentSongToBePlayed() throws MyTunesException {
        this.currentPlayingMedia = myTunesLogic.getMediaToBePlayed(this.currentIndexOffTheSong.getValue(), allSongsObjects);
        return this.currentPlayingMedia;
    }


    /**
     * return  ObservableList off songs
     */
    public ObservableList<Song> getAllSongsObjects() {
        return allSongsObjects;
    }

    /**
     * used to get the current index off the song that is selected
     */
    public IntegerProperty currentIndexOffTheSongProperty() {
        return currentIndexOffTheSong;
    }

    /**
     * retrieve the value off the current table that is playing
     */
    public String getCurrentTablePlaying() {
        return currentTablePlaying;
    }

    /**
     * set the current table that is being played
     */
    public void setCurrentTablePlaying(String currentTablePlaying) {
        this.currentTablePlaying = currentTablePlaying;
    }

    /**
     * controls if the music can be played
     */
    public boolean isPlayMusic() {
        return playMusic;
    }

    /**
     * controls if the music can be played
     */
    public void setPlayMusic(boolean playMusic) {
        this.playMusic = playMusic;
    }

    /**
     * gets the volumeLevel instance
     */
    public DoubleProperty volumeLevelProperty() {
        return volumeLevel;
    }
    /** get the is mute property*/
    public BooleanProperty isMuteProperty() {
        return isMute;
    }
}
