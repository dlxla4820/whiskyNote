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


    //위스키 기본 이름 5개 제공
    @GetMapping(value = "/base")
    public ResponseEntity<ResponseDto<?>> searchFiveBaseWhiskyName(@RequestParam String keyword) {
        ResponseDto<?> response = baseWhiskyService.searchFiveBaseWhiskyUsingKeyword(keyword);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }


    //위스키 관련 리뷰 (global) 검색 with detail


    @PostMapping(value = "/base")
    public ResponseEntity<ResponseDto<?>> updateBaseWhiskyInfo(@RequestBody BaseWhiskyRequestDto baseWhiskyRequestDtoList) throws Throwable {
        ResponseDto<?> response = baseWhiskyService.updateBaseWhisky(baseWhiskyRequestDtoList);
        return new ResponseEntity<>(response,HttpStatus.valueOf(response.getCode()));
    }
}
