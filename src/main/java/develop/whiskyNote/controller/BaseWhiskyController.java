package develop.whiskyNote.controller;

import develop.whiskyNote.dto.BaseWhiskyRequestDto;
import develop.whiskyNote.dto.BaseWhiskySearchRequestDto;
import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.service.BaseWhiskyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BaseWhiskyController {
    private final BaseWhiskyService baseWhiskyService;

    @GetMapping(value = "/base")
    public ResponseEntity<ResponseDto<?>> searchBaseWhisky( BaseWhiskySearchRequestDto searchRequestDto) {
        ResponseDto<?> response = baseWhiskyService.getBaseWhiskyWithPage(searchRequestDto);
        return new ResponseEntity<>(response,HttpStatus.valueOf(response.getCode()));
    }
    @PostMapping(value = "/base")
    public ResponseEntity<ResponseDto<?>> updateBaseWhiskyInfo(@RequestBody BaseWhiskyRequestDto baseWhiskyRequestDtoList) throws IOException {
        ResponseDto<?> response = baseWhiskyService.updateBaseWhisky(baseWhiskyRequestDtoList);
        return new ResponseEntity<>(response,HttpStatus.valueOf(response.getCode()));
    }
}
