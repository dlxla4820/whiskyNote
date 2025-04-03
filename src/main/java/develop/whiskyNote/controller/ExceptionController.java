package develop.whiskyNote.controller;

import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.enums.Description;
import develop.whiskyNote.enums.ErrorCode;
import develop.whiskyNote.exception.ForbiddenException;
//import develop.whiskyNote.exception.RedissonException;
import develop.whiskyNote.exception.ReviewLikeException;
import develop.whiskyNote.exception.UnauthenticatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@Slf4j
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

    @ExceptionHandler(ReviewLikeException.class)
    public ResponseEntity<?> handleReviewLikeException(ReviewLikeException ex) {
        ErrorCode errorCode = ErrorCode.valueOf(ex.getMessage());
        return new ResponseEntity<>(ResponseDto.builder()
                .code(errorCode.getStatus())
                .errorCode(errorCode.getErrorCode())
                .description(Description.FAIL)
                .errorDescription(errorCode.getErrorDescription())
                .build(), HttpStatus.CONFLICT);
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
    //배포 환경에서 너무 많은 로그가 쌓여서 임시로 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(ResponseDto.builder().code(ErrorCode.SYSTEM_ERROR.getStatus())
        .errorCode(ErrorCode.SYSTEM_ERROR.getErrorCode())
            .description(Description.FAIL)
            .errorDescription(ex.getMessage())
            .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
