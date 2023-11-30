package dk.easv.mytunes.exceptions;

public class MyTunesException extends Exception {
    public MyTunesException() {
    }

    public MyTunesException(String message) {
        super(message);
    }

    public MyTunesException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyTunesException(Throwable cause) {
        super(cause);
    }

    public MyTunesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
