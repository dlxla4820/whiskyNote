package develop.whiskyNote.controller;

import develop.whiskyNote.dto.GetOtherReviewListReqeustDto;
import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.service.OtherUserReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OtherUserReviewController {
    private final OtherUserReviewService otherUserReviewService;
    private final RedissonClient redissonClient;

    @GetMapping(value="/base/review/{whiskyUuid}")
    public ResponseEntity<ResponseDto<?>> searchOtherUserReviewAboutWhisky(@ModelAttribute GetOtherReviewListReqeustDto getOtherReviewListReqeustDto) {
        ResponseDto<?> response = otherUserReviewService.searchOtherUserReviewUsingKeyword(getOtherReviewListReqeustDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping("/set")
    public String setValue(@RequestParam String key, @RequestParam String value) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value);
        return "Value set successfully";
    }

    @GetMapping("/get")
    public String getValue(@RequestParam String key) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }
}
