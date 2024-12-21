package develop.whiskyNote.service;

import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.dto.SearchRequestDto;
import develop.whiskyNote.entity.Whisky;
import develop.whiskyNote.enums.Description;
import develop.whiskyNote.repository.SearchRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SearchService {
    private final SearchRepository searchRepository;

    public SearchService(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public ResponseDto<?> searchWhisky(SearchRequestDto requestBody) {
        String searchName = requestBody.getWhiskyName() == null || requestBody.getWhiskyName().isEmpty() ? "": requestBody.getWhiskyName();
        Integer pageNum = requestBody.getPageNum() == null || requestBody.getPageNum()>=0 ? 1 : requestBody.getPageNum();
        List<Whisky> result = searchRepository.getWhiskeis(searchName, pageNum);
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .data(result)
                .build();
    }

}
