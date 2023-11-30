package dk.easv.mytunes.gui.mainView;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.bll.MyTunesLogic;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.utility.PlayingLocation;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.util.Duration;

import java.util.List;

public class Model {
    private final MyTunesLogic myTunesLogic;
    private static Model instance;
    private boolean playMusic = false;

    private StringProperty  currentSongPlayingName = new SimpleStringProperty();





    private StringProperty  currentSongTimePlaying = new SimpleStringProperty("00:00:00");

    /** holds the current song time played*/
    private Duration currentTime = new Duration(0.0);
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
    private String currentTablePlaying ;

    /**
     * Holds all songs off the application , it is used to display songs in the all songs view table
     */
    private ObservableList<Song> allSongsObjects;
    /**
     * holds the current selected playlist Songs
     */
    private ObservableList<Song> playListSongs;
/** holds the value off the current playing song*/
    private Media currentPlayingMedia;
    /** holds the songs off the current list to be played from*/
    private ObservableList<Song> currentPlayingList;


    private Model() throws MyTunesException {
        myTunesLogic = MyTunesLogic.getMyTuneLogic();
        allSongsObjects = FXCollections.observableArrayList();
        populateAllSongsList();
        currentPlayingList=FXCollections.observableArrayList();
        playListSongs=FXCollections.observableArrayList();
        currentTablePlaying = PlayingLocation.ALL_SONGS.getValue();
        currentPlayingList.setAll(allSongsObjects);
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
        System.out.println(allSongsObjects.size());
        List<Song> songs = myTunesLogic.changeCurrentPlayingSongsList(this.currentTablePlaying,this.playListSongs,this.allSongsObjects);
        System.out.println(songs.size());
        this.currentPlayingList.setAll(songs);
        System.out.println(this.currentPlayingList.size());
        this.currentPlayingMedia = myTunesLogic.getMediaToBePlayed(this.currentIndexOffTheSong.getValue(),currentPlayingList);
        this.currentSongPlayingName.set(myTunesLogic.getCurrentSongName(this.currentIndexOffTheSong.getValue(), this.currentPlayingList));
        return this.currentPlayingMedia;
    }

/**decide the next song that needs to be played */
    private void getNextSongLocation() throws MyTunesException {
        generateNextIndex(this.currentIndexOffTheSong.getValue(), currentPlayingList.size());
        getSongToPlay();
    }

    /** generate the next index off the song to be played*/
    private void generateNextIndex(int indexToCheck, int songsSize) {
        this.currentIndexOffTheSong.set(myTunesLogic.processIndexUpp(indexToCheck, songsSize));
    }

   /**get the media song that needs to be played */
    private void getSongToPlay() throws MyTunesException {
        this.currentPlayingMedia = myTunesLogic.getMediaToBePlayed(this.currentIndexOffTheSong.getValue(),currentPlayingList);
    }
    /** returns the next song that the app can play*/
    public Media getNextSong() throws MyTunesException {
        getNextSongLocation();
        this.currentSongPlayingName.set(myTunesLogic.getCurrentSongName(this.currentIndexOffTheSong.getValue(), this.currentPlayingList));
        return this.currentPlayingMedia;
    }
/** returns the previous song that the app can play*/
    public Media getPreviousSong() throws MyTunesException {
        getPreviousSongLocation();
        this.currentSongPlayingName.set(myTunesLogic.getCurrentSongName(this.currentIndexOffTheSong.getValue(), this.currentPlayingList));
        return this.currentPlayingMedia;
    }

    //    generate the previous index and the previous song to be played
    private void getPreviousSongLocation() throws MyTunesException {
        generatePreviousIndex(this.currentIndexOffTheSong.getValue(),currentPlayingList.size());
        getSongToPlay();
    }

    //generate the previous index
    private void generatePreviousIndex(int indexToCheck, int songsSize) {
        this.currentIndexOffTheSong.set(myTunesLogic.processIndexDown(indexToCheck, songsSize));
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

    public void setCurrentTime(Duration currentTime) {
        this.currentTime = currentTime;
    }

    /** */
    public StringProperty currentSongPlayingNameProperty() {
        return currentSongPlayingName;
    }

    /** used to bind time label to the media current time */
    public StringProperty currentSongTimePlayingProperty() {
        return currentSongTimePlaying;
    }
}
