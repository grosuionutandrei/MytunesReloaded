package dk.easv.mytunes.be;

import java.util.ArrayList;
import java.util.List;

public class PlayList {
    private int id;
    private String name;
    private List<Song> playListSongs;


    public PlayList(int id, String name, List<Song> playListSongs) {
        this.id = id;
        this.name = name;
        if (playListSongs == null) {
            this.playListSongs = new ArrayList<>();
        } else {
            this.playListSongs = playListSongs;
        }

    }

    public PlayList(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getPlayListSongs() {
        return playListSongs;
    }

    public void setPlayListSongs(List<Song> playListSongs) {
        this.playListSongs = playListSongs;
    }

    public int getId() {
        return id;
    }

    public long getTotalTime() {
        return this.getPlayListSongs().stream().mapToLong(Song::getLength).sum();
    }
}
