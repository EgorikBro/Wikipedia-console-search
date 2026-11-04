package wikisearch.exception;

public class WikiSearchException extends RuntimeException {

    public WikiSearchException(String message) {
        super(message);
    }

    public WikiSearchException(String message, Throwable cause) {
        super(message, cause);
    }
}