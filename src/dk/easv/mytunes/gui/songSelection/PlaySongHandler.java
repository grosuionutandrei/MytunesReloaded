package dk.easv.mytunes.gui.songSelection;
import dk.easv.mytunes.gui.components.player.PlayerCommander;
import dk.easv.mytunes.gui.listeners.SongSelectionListener;
import dk.easv.mytunes.gui.mainView.Model;
import dk.easv.mytunes.utility.Operations;
import dk.easv.mytunes.utility.PlayButtonGraphic;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PlaySongHandler implements SongSelectionListener {
    private final Model model;
    private final PlayerCommander playerCommander;
    @FXML
    private Button button;

    public PlaySongHandler(Model model, PlayerCommander playerCommander, Button button) {
        this.model = model;
        this.playerCommander = playerCommander;
        this.button = button;
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

    /**
     * Play the selected song when it is double-clicked.
     *
     * @param index   The index of the selected song.
     * @param tableId The ID of the table containing the song.
     * @param play    Whether to play the song.
     */
    @Override
    public void playSelectedSong(int index, String tableId, boolean play) {
        setSongToPlay(index, tableId, play);
        playerCommander.processOperation(Operations.PLAY_CURRENT);
        changeButtonGraphic(this.button);
    }
}
