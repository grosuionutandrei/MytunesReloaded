package dk.easv.mytunes.gui.components.player;

import dk.easv.mytunes.gui.listeners.DataSupplier;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Player {
    private MediaPlayer mediaPlayer;
    private static Player instance;
    private Media song;
    private DataSupplier dataSupplier;
    private final StringProperty time = new SimpleStringProperty();


    private Player(DataSupplier dataSupplier) {
        this.dataSupplier = dataSupplier;
        this.song = dataSupplier.getMedia();
        playTrack(dataSupplier.isPlaying());
    }


    public static Player useMediaPlayer(DataSupplier dataSupplier) {
        if (instance == null) {
            instance = new Player(dataSupplier);
        }
        return instance;
    }


    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void playNextSong(Media media, boolean play) {
        setSong(media);
        playNextTrack(play);
    }

    public void playPreviousSong(Media media, boolean play) {
        setSong(media);
        playPreviousTrack(play);
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

    private void playPreviousTrack(boolean play) {
        this.mediaPlayer = playTrack(play);
    }

    private void playContinuous() {
        this.setSong(dataSupplier.getNextSong(), dataSupplier.isPlaying());

    }

//    public void bindViewWithTimeDuration(Label label, StringProperty binder) {
//        label.textProperty().bind(binder);
//    }

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

//    public void bindVolumeToModel(Model model) {
//        model.volumeProperty().addListener((obs, oldValue, newValue) -> {
//            this.mediaPlayer.setVolume(newValue.doubleValue());
//        });
//    }


    public void setSong(Media media) {
        this.song = media;
    }

    public void setSong(Media media, boolean play) {
        this.song = media;
        playTrack(play);
    }

    public void setDataSupplier(DataSupplier dataSupplier) {
        this.dataSupplier = dataSupplier;
    }

}
