package dk.easv.mytunes.gui.components.player;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.listeners.DataSupplier;
import dk.easv.mytunes.utility.Operations;
import javafx.scene.control.Label;
import javafx.util.Duration;


public class PlayerCommander {
    private final PlayerControl playerControl;
    private final DataSupplier dataSupplier;

    public PlayerCommander(DataSupplier dataSupplier , PlayerControl playerControl) throws MyTunesException {
        this.dataSupplier = dataSupplier;
        this.playerControl = playerControl;
    }

    public void processOperation(Operations operation) {
        switch (operation) {
            case Operations.PLAY_NEXT:
                playerControl.playNext(dataSupplier.getMedia(Operations.GET_NEXT), dataSupplier.isPlaying());
                break;
            case Operations.PLAY_PREVIOUS:
                playerControl.playNext(dataSupplier.getMedia(Operations.GET_PREVIOUS), dataSupplier.isPlaying());
                break;
            case Operations.PAUSE:
                playerControl.pause();
                break;
            case PLAY_CURRENT:
                playerControl.playCurrent(dataSupplier.getMedia(Operations.GET_CURRENT_SONG),dataSupplier.isPlaying());
                break;
            default:
                playerControl.play();
        }

    }

    public Duration getCurrentTime() {
        return playerControl.getCurrentTime();
    }

    public void bindMediaTimeToScreen(Label label){
        playerControl.bindMediaTimeToScreen(label);
    }

}
