package develop.whiskyNote.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class UnprocessableEntityException extends RuntimeException {
    private final Map<String, Object> error;
    public UnprocessableEntityException(String message, Map<String, Object> error ) {
        super(message);
        this.error = error;
    }
}
