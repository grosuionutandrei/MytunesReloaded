package dk.easv.mytunes.gui.listeners;

import javafx.scene.control.Button;

public interface SongSelectionListener {
  void playSelectedSong(int index, String tableId, boolean play);
}
