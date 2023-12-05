package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.dal.*;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.utility.PlayingLocation;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;

import java.util.*;
import java.util.stream.Collectors;

public class MyTunesLogic {
    private ISongReader songReader;
    private ISongsDao songsDao;
    private FileHandler fileHandler;
    private IPlaylistDao playlistDao;
    private static MyTunesLogic instance;


    private MyTunesLogic() throws MyTunesException {
        this.songReader = SongReader.getSongReader();
        this.songsDao = SongsDao.getSongsDao();
        this.playlistDao = PlaylistDao.getPlaylistDao();
        this.fileHandler=FileHandler.getInstance();
    }

    public static MyTunesLogic getMyTuneLogic() throws MyTunesException {
        if (instance == null) {
            instance = new MyTunesLogic();
        }
        return instance;
    }

    public Media getMediaToBePlayed(int index, List<Song> songs) throws MyTunesException {
        String path = songs.get(index).getSongPath();
        return this.songReader.getMedia(path);
    }

    /**
     * get the next index to be played in the list,if reach the end off the list goes back to 0
     **/
    public int processIndexUpp(int indexToCheck, int songsSize) {
        if (indexToCheck < songsSize - 1) {
            return indexToCheck + 1;
        }
        return 0;
    }

    /**
     * get the previous index to be played if it reaches the beginning off the list goes to the end off the list
     **/
    public int processIndexDown(int indexToCheck, int songsSize) {
        if (indexToCheck >= 1) {
            return indexToCheck - 1;
        }
        return songsSize - 1;
    }

//    public List<Song> changeCurrentPlayingSongsList(String currentNameLocation, List<Song> playlist, List<Song> allSongs) {
//        System.out.println(currentNameLocation + "cu" + allSongs.size());
//        if (currentNameLocation.equals(PlayingLocation.ALL_SONGS.getValue())) {
//            return allSongs;
//        }
//        return playlist;
//    }
public ObservableList<Song> changeCurrentPlayingSongsList(String currentNameLocation, ObservableList<Song> playlist, ObservableList<Song> allSongs) {
    System.out.println(currentNameLocation + "cu" + allSongs.size());
    if (currentNameLocation.equals(PlayingLocation.ALL_SONGS.getValue())) {
        return allSongs;
    }
    return playlist;
}

    public Song getCurrentSongToBePlayed(int index, List<Song> songs) {
        return songs.get(index);
    }

    public List<Song> getAllSongs() throws MyTunesException {
        return this.songsDao.getAllSongsFromCache();
    }

    /**
     * returns the name off the current playing song
     */
    public String getCurrentSongName(int index, List<Song> songs) {
        return songs.get(index).getTitle();
    }


    public List<Song> applyFilter(String filter, List<Song> toFilter) {
        if (filter == null || filter.isEmpty()) {
            return new ArrayList<>(toFilter);
        }

        String filterLower = filter.toLowerCase();

        return toFilter.stream()
                .filter(song -> (song.getTitle() != null && song.getTitle().toLowerCase().contains(filterLower)) ||
                        (song.getArtist() != null && song.getArtist().toLowerCase().contains(filterLower)) ||
                        (song.getGenre() != null && song.getGenre().toLowerCase().contains(filterLower)))
                .collect(Collectors.toList());
    }

/** reload all the songs from the data base*/
    public void reloadSongsFromDB() throws MyTunesException {
        songsDao.reloadSongsFromDB();
    }


    public List<PlayList> getAllPlaylists() throws MyTunesException {
        return this.playlistDao.getAllPlaylistsFromCache();
    }
    public PlayList getSelectedPlayList(int index) throws MyTunesException {
        return this.playlistDao.getAllPlaylistsFromCache().get(index);
    }


    public List<PlayList> reloadPlaylistsFromDB() throws MyTunesException {
       return this.playlistDao.reloadPlaylistsFromDb();
    }
}

