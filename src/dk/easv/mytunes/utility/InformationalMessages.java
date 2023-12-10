package dk.easv.mytunes.utility;

public enum InformationalMessages {
    FILTER_EMPTY("Filter is empty");
    private final String value;

    InformationalMessages(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}

