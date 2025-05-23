package develop.whiskyNote.controller;

import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.dto.ResponseHeaderDto;
import develop.whiskyNote.dto.ReviewUpsertRequestDto;
import develop.whiskyNote.dto.UserWhiskyDto;
import develop.whiskyNote.service.FileService;
import develop.whiskyNote.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final FileService fileService;
    @PostMapping(value = "/review")
    public ResponseEntity<ResponseDto<?>> createReview(@RequestBody ReviewUpsertRequestDto requestBody)  {
        ResponseDto<?> response = reviewService.createReview(requestBody);
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
            , @RequestParam(required = false) String category, @RequestParam(required = false, name = "score_order") String scoreOrder
            , @RequestParam(required = false, name = "date_order") String dateOrder, @RequestParam(required = false, name = "open_date_order") String openDateOrder ){
        ResponseDto<?> response = reviewService.searchMyWhiskyList(name, category, scoreOrder, dateOrder,openDateOrder);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    //위스키 저장
    @PostMapping(value = "/my-whisky")
    public ResponseEntity<ResponseDto<?>> createWhisky(@RequestBody UserWhiskyDto requestBody) {
        ResponseDto<?> response = reviewService.createWhisky(requestBody);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    //위스키 변경
    @PutMapping(value = "/my-whisky/{userWhiskyUuid}")
    public ResponseEntity<ResponseDto<?>> updateWhisky(@PathVariable String userWhiskyUuid, @RequestBody UserWhiskyDto requestBody) {
        ResponseDto<?> response = reviewService.updateWhisky(userWhiskyUuid, requestBody);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
    //위스키 삭제
    @DeleteMapping(value = "/my-whisky/{userWhiskyUuid}")
    public ResponseEntity<ResponseDto<?>> deleteWhisky(@PathVariable String userWhiskyUuid) {
        ResponseDto<?> response = reviewService.deleteWhisky(userWhiskyUuid);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }


    @GetMapping(value = "/my-whisky/{userWhiskyUuid}/my-reviews")
    public ResponseEntity<ResponseDto<?>> readMyReviews(@PathVariable String userWhiskyUuid, @RequestParam(name = "order") String order){
        ResponseDto<?> response = reviewService.readMyReviews(userWhiskyUuid, order);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @PutMapping(value = "/review/{reviewUuid}")
    public ResponseEntity<ResponseDto<?>> updateReview(@PathVariable String reviewUuid, @RequestBody ReviewUpsertRequestDto requestBody) {
        ResponseDto<?> response = reviewService.updateReview(reviewUuid,requestBody);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }


    @DeleteMapping(value = "/review/{reviewUuid}")
    public ResponseEntity<ResponseDto<?>> deleteReview(@PathVariable String reviewUuid) {
        ResponseDto<?> response = reviewService.deleteReview(reviewUuid);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }


    @PostMapping(value = "/image-upload",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<?>> uploadFile(@RequestPart(name = "image", required = false) MultipartFile image){
        ResponseDto<?> response = fileService.uploadFile(image);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping("/image/{imageName}")
    public ResponseEntity<?> showAsset(@PathVariable String imageName) {
        ResponseDto<?> responseDto = fileService.showImage(imageName);
        ResponseHeaderDto responseHeaderDto = (ResponseHeaderDto) responseDto.getData();
        return new ResponseEntity<>(responseHeaderDto.getData(),responseHeaderDto.getHeaders(), HttpStatus.valueOf(responseDto.getCode()));
    }
}

//    @PostMapping("/whisky")
//    public ResponseEntity<ResponseDto<?>> insertWhisky() {
//        ResponseDto<?> response = reviewService.insertWhisky();
//        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
//    }

