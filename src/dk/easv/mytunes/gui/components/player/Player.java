package dk.easv.mytunes.gui.components.player;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.listeners.DataSupplier;
import dk.easv.mytunes.utility.ExceptionHandler;
import dk.easv.mytunes.utility.ExceptionsMessages;
import dk.easv.mytunes.utility.Operations;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.xml.sax.ErrorHandler;

public class Player {
    private MediaPlayer mediaPlayer;
    private static Player instance;
    private Media song;
    private DataSupplier dataSupplier;
    private final StringProperty time = new SimpleStringProperty();


    private Player(DataSupplier dataSupplier) throws MyTunesException {
        this.dataSupplier = dataSupplier;
        checkMediaValid(dataSupplier.getMedia(Operations.INITIAL_SONG));
        playTrack(dataSupplier.isPlaying());
    }
    public static Player useMediaPlayer(DataSupplier dataSupplier) throws MyTunesException {
        if (instance == null) {
            instance = new Player(dataSupplier);
        }
        return instance;
    }
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /**
     * controls the play functionality if the app just started , music will not play
     **/
    private MediaPlayer playTrack(boolean play) {

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer.volumeProperty().unbind();
        }
        Media media = this.song;
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.volumeProperty().bind(Bindings.when(dataSupplier.isMute())
                .then(0.0)
                .otherwise(dataSupplier.getVolumeObservable()));

        bindDurationToLabel(time);
        dataSupplier.bindMediaTimeToScreen(time);

        mediaPlayer.setOnEndOfMedia(this::playContinuous);
        if (play) {
            mediaPlayer.play();
        }
        return mediaPlayer;
    }

    private void playNextTrack(boolean play) {
        this.mediaPlayer = playTrack(play);
    }

    private void playPreviousTrack(boolean play)  {
        this.mediaPlayer = playTrack(play);
    }
    public void playNextSong(Media media, boolean play)  {
        try {
            setSong(media);
        } catch (MyTunesException e) {
            ExceptionHandler.displayErrorAlert(e.getMessage());
            return;
        }
        playNextTrack(play);
    }

    public void playPreviousSong(Media media, boolean play)  {
        try {
            setSong(media);
        } catch (MyTunesException e) {
            ExceptionHandler.displayErrorAlert(e.getMessage());
            return;
        }
        playPreviousTrack(play);
    }

    private void playContinuous() {
        this.setSong(dataSupplier.getMedia(Operations.PLAY_NEXT), dataSupplier.isPlaying());

    }

    private void bindDurationToLabel(StringProperty stringToBind) {
        StringBinding currentTimeStringBinding = Bindings.createStringBinding(() -> formatDuration(mediaPlayer.getCurrentTime()), mediaPlayer.currentTimeProperty());
        stringToBind.bind(currentTimeStringBinding);
    }

    private String formatDuration(Duration duration) {
        int hours = (int) duration.toHours();
        int minutes = (int) (duration.toMinutes() % 60);
        int seconds = (int) (duration.toSeconds() % 60);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private  void checkMediaValid(Media media) throws MyTunesException {
        if(media==null){
            throw  new MyTunesException(ExceptionsMessages.READING_SONG_LOCAL);
        }else{
            this.song =media;
        }
    }




    public void setSong(Media media) throws MyTunesException {
        checkMediaValid(media);
//        this.song = media;
    }

    public void setSong(Media media, boolean play) {
        try {
            checkMediaValid(media);
        } catch (MyTunesException e) {
            ExceptionHandler.displayErrorAlert(e.getMessage());
            return;
        }
        playTrack(play);
    }

    public void setDataSupplier(DataSupplier dataSupplier) {
        this.dataSupplier = dataSupplier;
    }

}
