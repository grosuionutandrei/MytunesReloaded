package dk.easv.mytunes.gui.songSelection;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.components.player.Player;
import dk.easv.mytunes.gui.mainView.Model;
import dk.easv.mytunes.utility.PlayButtonGraphic;
import dk.easv.mytunes.utility.Utility;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class PlaySong  {
    private final Model model;
    private final Player player;


    public PlaySong(Model model, Player player) {
        this.model = model;
        this.player = player;
    }
    /**
     * Play the selected song when it is double-clicked.
     *
     * @param rowIndex The index of the selected song.
     * @param tableId  The ID of the table containing the song.
     * @param play     Whether to play the song.
     * @param button   The button used for playback control.
     */
    public void playSelectedSong(int rowIndex, String tableId, boolean play, Button button) {
        try {
            setSongToPlay(rowIndex, tableId, play);
            player.setSong(model.getCurrentSongToBePlayed(), model.isPlayMusic());
            changeButtonGraphic(button);
        } catch (MyTunesException e) {
            Utility.displayInformation(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    private void setSongToPlay(int index, String tableId, boolean play) {
        model.currentIndexOffTheSongProperty().set(index);
        model.setCurrentTablePlaying(tableId);
        model.setPlayMusic(play);
    }

    private void changeButtonGraphic(Button button) {
        if (isGraphicPlaying(button)) {
            button.setText(PlayButtonGraphic.STOP.getValue());
        }
    }

    private boolean isGraphicPlaying(Button button) {
        return button.getText().equals(PlayButtonGraphic.PLAY.getValue());
    }

}
