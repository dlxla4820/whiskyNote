package develop.whiskyNote.controller;

import develop.whiskyNote.dto.OtherReviewGetReqeustDto;
import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.service.OtherUserReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.redisson.api.RBucket;
//import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OtherUserReviewController {
    private final OtherUserReviewService otherUserReviewService;
//    private final RedissonClient redissonClient;


//    @PostMapping(value="/like/count")
//    public ResponseEntity<?> createReviewLikeCountEntity(@RequestBody ReviewCountRequestDto reviewCountRequestDto) {
//        ResponseDto<?> responseDto = otherUserReviewService.saveReviewCountForCurrentRequest(reviewCountRequestDto);
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }

    @PostMapping(value = "/like/{likeCountUuid}")
    public ResponseEntity<?> createReviewLikeEntity(@PathVariable  String likeCountUuid){
        ResponseDto<?> response = otherUserReviewService.createReviewLikeMapping(likeCountUuid);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/like/{likeCountUuid}")
    public ResponseEntity<?> deleteReviewLikeEntity(@PathVariable  String likeCountUuid) {
        ResponseDto<?> response = otherUserReviewService.deleteReviewLikeMapping(likeCountUuid);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value="/other/review")
    public ResponseEntity<ResponseDto<?>> searchOtherUserReviewAboutWhisky(
            @RequestParam(name = "main_search_word") String mainSearchWord,
            @RequestParam(name = "sub_search_word", required = false) String subSearchWord,
            @RequestParam(name = "like_order", required = false) String likeOrder,
            @RequestParam(name = "score_order", required = false) String scoreOrder,
            @RequestParam(name = "create_order", required = false) String createdOrder,
            @RequestParam(name = "name_order", required = false) String nameOrder,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        ResponseDto<?> response = otherUserReviewService.searchOtherUserReviewUsingKeyword(mainSearchWord, subSearchWord, likeOrder,scoreOrder, createdOrder,nameOrder, page, size);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

//    @GetMapping("/set")
//    public String setValue(@RequestParam String key, @RequestParam String value) {
//        RBucket<String> bucket = redissonClient.getBucket(key);
//        bucket.set(value);
//        return "Value set successfully";
//    }
//
//    @GetMapping("/get")
//    public String getValue(@RequestParam String key) {
//        RBucket<String> bucket = redissonClient.getBucket(key);
//        return bucket.get();
//    }
}
