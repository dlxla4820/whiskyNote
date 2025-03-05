package develop.whiskyNote.controller;

import develop.whiskyNote.dto.BaseWhiskyRequestDto;
import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.service.BaseWhiskyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class BaseWhiskyController {
    private final BaseWhiskyService baseWhiskyService;


    //위스키 기본 정보 5개 제공
    @GetMapping(value = "/base")
    public ResponseEntity<ResponseDto<?>> searchFiveBaseWhiskyName(@RequestParam String keyword) {
        ResponseDto<?> response = baseWhiskyService.searchFiveBaseWhiskyUsingKeyword(keyword);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }


    //이것들은 나중에 public-review로 묶을 것
    //위스키 관련 리뷰 (global) 검색
    @GetMapping(value="/base/review/{whiskyUuid}")
    public ResponseEntity<ResponseDto<?>> searchOtherUserReviewAboutWhisky(@PathVariable String whiskyUuid, @RequestParam String searchword, @RequestParam int page) {
        ResponseDto<?> response = baseWhiskyService.searchOtherUserReviewUsingKeyword(whiskyUuid, searchword , page);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    //위스키 관련 리뷰 (global) 검색 with detail


    @PostMapping(value = "/base")
    public ResponseEntity<ResponseDto<?>> updateBaseWhiskyInfo(@RequestBody BaseWhiskyRequestDto baseWhiskyRequestDtoList) throws IOException {
        ResponseDto<?> response = baseWhiskyService.updateBaseWhisky(baseWhiskyRequestDtoList);
        return new ResponseEntity<>(response,HttpStatus.valueOf(response.getCode()));
    }
}
