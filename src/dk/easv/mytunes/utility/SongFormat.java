package dk.easv.mytunes.utility;

public enum SongFormat {
    MP3("mp3"),WAV("wav");
private final String value;
    SongFormat(String value) {
        this.value=value;
    }
    public String getValue() {
        return value;
    }
}
