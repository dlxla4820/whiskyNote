package develop.whiskyNote.exception;

import develop.whiskyNote.enums.ErrorCode;

public class ReviewLikeException extends RuntimeException {
    public ReviewLikeException(String message) {
        super(message);
    }
}
