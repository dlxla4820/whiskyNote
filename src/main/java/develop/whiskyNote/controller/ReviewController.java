package develop.whiskyNote.controller;

import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.dto.ReviewCreateRequestDto;
import develop.whiskyNote.dto.UserRequestDto;
import develop.whiskyNote.service.ReviewService;
import develop.whiskyNote.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping(value = "/review")
    public ResponseEntity<ResponseDto<?>> register(@RequestParam ReviewCreateRequestDto requestBody){
        ResponseDto<?> response = reviewService.createReview(requestBody);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
}
