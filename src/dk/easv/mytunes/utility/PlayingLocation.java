package dk.easv.mytunes.utility;

/** to witch the songs list based on the clicked song*/
public enum PlayingLocation {
    ALL_SONGS("allSongs"),PLAYLIST_SONGS("playListSongs")
    ;
    private final String value;
    PlayingLocation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }



}
