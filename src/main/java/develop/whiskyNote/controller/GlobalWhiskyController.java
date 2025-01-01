package develop.whiskyNote.controller;

import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.dto.SearchRequestDto;
import develop.whiskyNote.service.GlobalWhiskyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class GlobalWhiskyController {
    private final GlobalWhiskyService globalWhiskyService;

    @GetMapping(value = "/global/list")
    public ResponseEntity<ResponseDto<?>> search( SearchRequestDto searchRequestDto) {
        ResponseDto<?> response = globalWhiskyService.searchWhiskyInGlobalWhisky(searchRequestDto);
        return new ResponseEntity<>(response,HttpStatus.valueOf(response.getCode()));
    }
    @PostMapping(value = "/global")
    public ResponseEntity<ResponseDto<?>> getCrawlingData() throws IOException {
        ResponseDto<?> response = globalWhiskyService.updateGlobalWhisky();
        return new ResponseEntity<>(response,HttpStatus.valueOf(response.getCode()));
    }
}
