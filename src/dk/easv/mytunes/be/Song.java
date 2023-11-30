package dk.easv.mytunes.be;

import java.util.Objects;

public class Song {
    private int songId;
    private String songPath;
    private String title;
    private String artist;
    private String genre;
    private long length;

    public Song(String songPath, String title, String artist, String genre, long length) {
        this.songPath = songPath;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.length = length;
    }

    public Song(int songId, String songPath, String title, String artist, String genre, long length) {
        this.songId = songId;
        this.songPath = songPath;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.length = length;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }


    @Override
    public String toString() {
        return "Song{" +
                "songId=" + songId +
                ", title='" + title + '\'' +
                ", songPath='" + songPath + '\'' +
                ", artist='" + artist + '\'' +
                ", genre='" + genre + '\'' +
                ", length=" + length +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return songId == song.songId && length == song.length && Objects.equals(title, song.title) && Objects.equals(songPath, song.songPath) && Objects.equals(artist, song.artist) && Objects.equals(genre, song.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songId, title, songPath, artist, genre, length);
    }
}
