package develop.whiskyNote.controller;

import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.dto.ReviewCreateRequestDto;
import develop.whiskyNote.dto.UserRequestDto;
import develop.whiskyNote.service.ReviewService;
import develop.whiskyNote.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping(value = "/review")
    public ResponseEntity<ResponseDto<?>> saveReview(@RequestParam ReviewCreateRequestDto requestBody) throws IOException {
        ResponseDto<?> response = reviewService.createReview(requestBody);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping(value = "/review")
    public ResponseEntity<ResponseDto<?>> readReview() {
        ResponseDto<?> response = reviewService.readReview();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
}
