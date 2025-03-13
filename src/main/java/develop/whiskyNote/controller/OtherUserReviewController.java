package develop.whiskyNote.controller;

import develop.whiskyNote.dto.GetOtherReviewListReqeustDto;
import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.service.OtherUserReviewService;
import develop.whiskyNote.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OtherUserReviewController {
    private final OtherUserReviewService otherUserReviewService;
    @GetMapping(value="/base/review/{whiskyUuid}")
    public ResponseEntity<ResponseDto<?>> searchOtherUserReviewAboutWhisky(@ModelAttribute GetOtherReviewListReqeustDto getOtherReviewListReqeustDto) {
        ResponseDto<?> response = otherUserReviewService.searchOtherUserReviewUsingKeyword(getOtherReviewListReqeustDto);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
}
