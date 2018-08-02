package by.epam.web.util.request;

public class NoSuchRequestParameterException extends Exception {
    public NoSuchRequestParameterException() {
    }

    public NoSuchRequestParameterException(String message) {
        super(message);
    }

    public NoSuchRequestParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchRequestParameterException(Throwable cause) {
        super(cause);
    }

    public NoSuchRequestParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
