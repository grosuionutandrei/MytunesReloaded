package dk.easv.mytunes.gui.components.player;

import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.listeners.DataSupplier;
import dk.easv.mytunes.utility.ExceptionHandler;
import dk.easv.mytunes.utility.ExceptionsMessages;
import dk.easv.mytunes.utility.Operations;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Player implements PlayerControl {
    private MediaPlayer mediaPlayer;
    private static Player instance;
    private Media song;
    private DataSupplier dataSupplier;
    private final StringProperty time = new SimpleStringProperty();


    private Player(DataSupplier dataSupplier) throws MyTunesException {
        this.dataSupplier = dataSupplier;
        checkMediaValid(dataSupplier.getMedia(Operations.GET_CURRENT_SONG));
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
        mediaPlayer.setOnEndOfMedia(this::playContinuous);
        if (play) {
            mediaPlayer.play();
        }
        return mediaPlayer;
    }

    private void playNextTrack(boolean play) {
        this.mediaPlayer = playTrack(play);
    }


    private void playContinuous() {
        playCurrent(dataSupplier.getMedia(Operations.GET_NEXT),dataSupplier.isPlaying());
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

    private void checkMediaValid(Media media) throws MyTunesException {
        if (media == null) {
            throw new MyTunesException(ExceptionsMessages.READING_SONG_LOCAL);
        } else {
            this.song = media;
        }
    }


    public void setSong(Media media) throws MyTunesException {
        checkMediaValid(media);
    }

    public void setDataSupplier(DataSupplier dataSupplier) {
        this.dataSupplier = dataSupplier;
    }


    @Override
    public void play() {
        this.mediaPlayer.play();
    }

    @Override
    public void pause() {
        this.mediaPlayer.pause();
    }

    @Override
    public void playNext(Media media, boolean play) {
        try {
            setSong(media);
        } catch (MyTunesException e) {
            ExceptionHandler.displayErrorAlert(e.getMessage());
            return;
        }
        playNextTrack(play);
    }

    @Override
    public void playPrevious(Media media, boolean play) {
        try {
            setSong(media);
        } catch (MyTunesException e) {
            ExceptionHandler.displayErrorAlert(e.getMessage());
            return;
        }
        playNextTrack(play);
    }

    @Override
    public Duration getCurrentTime() {
        return getMediaPlayer().getCurrentTime();
    }

    @Override
    public void playCurrent(Media media, boolean play) {
        try {
            checkMediaValid(media);
        } catch (MyTunesException e) {
            ExceptionHandler.displayErrorAlert(e.getMessage());
            return;
        }
        playTrack(play);
    }

    @Override
    public void bindMediaTimeToScreen(Label label) {
        label.textProperty().bind(time);
    }


}
