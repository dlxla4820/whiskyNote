package develop.whiskyNote.controller;

import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.dto.SearchRequestDto;
import develop.whiskyNote.service.GlobalWhiskyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SearchController {
    private final GlobalWhiskyService globalWhiskyService;

    @GetMapping(value = "/search")
    public ResponseEntity<ResponseDto<?>> search(@RequestBody SearchRequestDto searchRequestDto) {
        ResponseDto<?> response = globalWhiskyService.searchWhisky(searchRequestDto);
        return new ResponseEntity<>(response,HttpStatus.valueOf(response.getCode()));
    }
}
