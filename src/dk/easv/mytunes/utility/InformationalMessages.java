package dk.easv.mytunes.utility;

public enum InformationalMessages {
    FILTER_EMPTY("Filter is empty"),
    NO_INTERNET_CONNECTION("Check your internet connection"),
    FXML_MISSING("Application error, file system corrupt, FXML resource is missing"),
    NO_EMPTY_INPUT("Title and Location can not be empty"),
    NO_SONG_SELECTED("No song selected"),
    OPERATION_FAILED("Operation failed")
    ;
    private final String value;

    InformationalMessages(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}

