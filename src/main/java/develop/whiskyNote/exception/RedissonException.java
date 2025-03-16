package develop.whiskyNote.exception;

public class RedissonException extends RuntimeException {
    public RedissonException(String message) {
        super(message);
    }
}
