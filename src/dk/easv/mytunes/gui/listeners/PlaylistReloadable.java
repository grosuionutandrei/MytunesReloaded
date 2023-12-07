package dk.easv.mytunes.gui.listeners;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;

public interface PlaylistReloadable {
    public void reloadPlaylistsFromDb();
    public void reloadSongs(Song song);
}
