package dk.easv.mytunes.utility;

public enum ExceptionsMessages implements Messages {
    READING_SONGS_FAILED("Reading songs from the data base gone wrong." + "\n" + InformationalMessages.NO_INTERNET_CONNECTION.getValue()),
    READING_SONG_LOCAL("Reading file from local storage went wrong");
    private final String value;

    ExceptionsMessages(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
