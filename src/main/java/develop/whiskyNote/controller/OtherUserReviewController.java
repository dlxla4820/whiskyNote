package develop.whiskyNote.controller;

import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.service.OtherUserReviewService;
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
    @PostMapping(value = "/like/{reviewUuid}")
    public ResponseEntity<?> createReviewLikeEntity(@PathVariable  String reviewUuid) {
        ResponseDto<?> response = otherUserReviewService.createReviewLikeMapping(reviewUuid);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/like/{reviewUuid}")
    public ResponseEntity<?> deleteReviewLikeEntity(@PathVariable  String reviewUuid) {
        ResponseDto<?> response = otherUserReviewService.deleteReviewLikeMapping(reviewUuid);
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
}
