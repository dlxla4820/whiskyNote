package develop.whiskyNote.controller;

import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.dto.ReviewUpsertRequestDto;
import develop.whiskyNote.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping(value = "/review", consumes ={MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseDto<?>> createReview(@RequestPart(name = "data") ReviewUpsertRequestDto requestBody, @RequestPart(name = "images", required = false) List<MultipartFile> images ) throws IOException {
        ResponseDto<?> response = reviewService.createReview(requestBody,images);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping(value = "/review/{reviewUuid}")
    public ResponseEntity<ResponseDto<?>> readReview(@PathVariable String reviewUuid){
        ResponseDto<?> response = reviewService.readReview(reviewUuid);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @PutMapping(value = "/review/{reviewUuid}", consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseDto<?>> updateReview(@PathVariable String reviewUuid, @RequestPart(name = "data") ReviewUpsertRequestDto requestBody, @RequestPart(name = "images", required = false) List<MultipartFile> images) throws IOException{
        ResponseDto<?> response = reviewService.updateReview(reviewUuid,requestBody,images);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }


    @DeleteMapping(value = "/review/{reviewUuid}")
    public ResponseEntity<ResponseDto<?>> deleteReview(@PathVariable String reviewUuid) {
        ResponseDto<?> response = reviewService.deleteReview(reviewUuid);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

}
