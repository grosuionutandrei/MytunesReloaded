package dk.easv.mytunes.gui.listeners;

public interface SongSelectionListener {
    public void onSongSelect(int index,String tableId,boolean play);
    public void  updateTemporarySelection(int index, String tableId);
}
