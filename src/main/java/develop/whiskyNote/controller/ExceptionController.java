package develop.whiskyNote.controller;

import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.enums.Description;
import develop.whiskyNote.enums.ErrorCode;
import develop.whiskyNote.exception.ForbiddenException;
import develop.whiskyNote.exception.UnauthenticatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<?> handleUnauthenticatedException(UnauthenticatedException ex) {
        return new ResponseEntity<>(ResponseDto.builder()
                .code(ErrorCode.TOKEN_UNAUTHORIZED.getStatus())
                .errorCode(ErrorCode.TOKEN_UNAUTHORIZED.getErrorCode())
                .description(Description.FAIL)
                .errorDescription(ErrorCode.TOKEN_UNAUTHORIZED.getErrorDescription())
                .build(), HttpStatus.UNAUTHORIZED);  // 401 Unauthorized
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbiddenException(ForbiddenException ex) {
        return new ResponseEntity<>(ResponseDto.builder()
                .code(ErrorCode.TOKEN_FORBIDDEN.getStatus())
                .errorCode(ErrorCode.TOKEN_FORBIDDEN.getErrorCode())//
                .description(Description.FAIL)
                .errorDescription(ErrorCode.TOKEN_FORBIDDEN.getErrorDescription())
                .build(), HttpStatus.FORBIDDEN);  // 403 Forbidden
    }
}
