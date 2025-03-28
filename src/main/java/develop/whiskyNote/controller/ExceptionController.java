package develop.whiskyNote.controller;

import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.enums.Description;
import develop.whiskyNote.enums.ErrorCode;
import develop.whiskyNote.exception.ForbiddenException;
//import develop.whiskyNote.exception.RedissonException;
import develop.whiskyNote.exception.ReviewLikeException;
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

//    @ExceptionHandler(RedissonException.class)
//    public ResponseEntity<?> handleRedisAskException(RedissonException ex) {
//        ErrorCode errorCode = ErrorCode.valueOf(ex.getMessage());
//        return new ResponseEntity<>(ResponseDto.builder()
//                .code(errorCode.getStatus())
//                .errorCode(errorCode.getErrorCode())
//                .description(Description.FAIL)
//                .errorDescription(errorCode.getErrorDescription())
//                .build(),HttpStatus.CONFLICT);//409 Conflict, 나중에 에러코드에 status를 저장하도록 수정
//    }

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
