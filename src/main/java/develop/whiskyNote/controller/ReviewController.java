package develop.whiskyNote.controller;

import develop.whiskyNote.dto.MYWhiskyCreateRequestDto;
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
    // 위스키 불러오기
    @GetMapping(value = "/whiskys")
    public ResponseEntity<ResponseDto<?>> searchWhiskys(@RequestParam(required = false) String name, @RequestParam(required = false) String category){
        ResponseDto<?> response = reviewService.searchWhiskyList(name, category);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    //내 위스키 불러오기
    @GetMapping(value = "/my-whiskys")
    public ResponseEntity<ResponseDto<?>> searchMyWhiskys(@RequestParam(required = false) String name
            , @RequestParam(required = false) String category, @RequestParam(name = "score_order") String scoreOrder
            , @RequestParam(name = "date_order") String dateOrder, @RequestParam(name = "name_order") String nameOrder ){
        ResponseDto<?> response = reviewService.searchMyWhiskyList(name, category, nameOrder, scoreOrder, dateOrder);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping(value = "/my-reviews/my-whisky/{userWhiskyUuid}")
    public ResponseEntity<ResponseDto<?>> readMyReviews(@PathVariable String userWhiskyUuid, @RequestParam(name = "order") String order){
        ResponseDto<?> response = reviewService.readMyReviews(userWhiskyUuid, order);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
    //위스키 저장
    @PostMapping(value = "/my-whisky", consumes ={MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseDto<?>> createWhisky(@RequestPart(name = "data") MYWhiskyCreateRequestDto requestBody, @RequestPart(name = "image", required = false) MultipartFile image) throws IOException {
        ResponseDto<?> response = reviewService.createWhisky(requestBody, image);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
/*    @GetMapping(value = "/reviews/bottle/{bottleNum}")
    public ResponseEntity<ResponseDto<?>> createWhisky(@PathVariable Integer bottleNum ) {
        ResponseDto<?> response = reviewService.readReviews(sreviewUuid);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }*/
//    @GetMapping(value = "/whisky/{whiskyUuid}/reviews")
//    public ResponseEntity<ResponseDto<?>> searchReviewList(){
//
//    }

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


//    @PostMapping("/whisky")
//    public ResponseEntity<ResponseDto<?>> insertWhisky() {
//        ResponseDto<?> response = reviewService.insertWhisky();
//        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
//    }

}
