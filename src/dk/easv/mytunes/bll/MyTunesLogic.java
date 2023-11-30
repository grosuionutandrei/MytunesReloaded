package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.dal.*;
import dk.easv.mytunes.exceptions.MyTunesException;
import javafx.scene.media.Media;

import java.util.List;

public class MyTunesLogic {
    private ISongReader songReader;
    private ISongsDao songsDao;
    private IPlaylistDao playlistDao;
    private static MyTunesLogic instance;

    private MyTunesLogic() throws MyTunesException {
        this.songReader = SongReader.getSongReader();
        this.songsDao = SongsDao.getSongsDao();
        this.playlistDao = PlaylistDao.getPlaylistDao();
    }

    public static MyTunesLogic getMyTuneLogic() throws MyTunesException {
        if (instance == null) {
            instance = new MyTunesLogic();
        }
        return instance;
    }

    public Media getMediaToBePlayed(int index, List<Song>songs) throws MyTunesException {
//        Song song= songs.stream().filter(elem->elem.getSongId()==index).findFirst().orElse(null);
//        return this.songReader.getMedia(song.getSongPath());
        String path = songs.get(index).getSongPath();
        return this.songReader.getMedia(path);
    }


    public List<Song> getAllSongs() throws MyTunesException {
        return this.songsDao.getAllSongsFromCache();
    }


}

