package develop.whiskyNote.service;

import develop.whiskyNote.dto.BaseWhiskyFiveResponseDto;
import develop.whiskyNote.dto.BaseWhiskyRequestDto;
import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.entity.Whisky;
import develop.whiskyNote.enums.Description;
import develop.whiskyNote.repository.BaseWhiskyRepository;
import develop.whiskyNote.utils.CommonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static develop.whiskyNote.enums.ErrorCode.ALL_WHISKY_INFO_DUPLICATE;
import static develop.whiskyNote.enums.ErrorCode.CRAWLING_DATA_NOT_EXIST;

@Service
@Transactional
public class BaseWhiskyService {
    private final BaseWhiskyRepository baseWhiskyRepository;

    public BaseWhiskyService(BaseWhiskyRepository baseWhiskyRepository) {
        this.baseWhiskyRepository = baseWhiskyRepository;
    }
    public ResponseDto<?> searchFiveBaseWhiskyUsingKeyword(String keyword) {

        List<BaseWhiskyFiveResponseDto> result = CommonUtils.containsKorean(keyword)? baseWhiskyRepository.getWhiskyByKoreaName(keyword) : baseWhiskyRepository.getWhiskyByEnglishName(keyword);
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .data(result)
                .build();
    }

    public ResponseDto<?> searchOtherUserReviewUsingKeyword(String uuid, String keyword, int page) {
        UUID userUuid = CommonUtils.getUserUuidIfAdminOrUser();
        return ResponseDto.builder().build();
    }

    public ResponseDto<?> getAllBaseWhiskyInfos() {
        List<Whisky> result = baseWhiskyRepository.getAllBasicWhiskyInfos();
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .data(result)
                .build();
    }

    public ResponseDto<?> updateBaseWhisky(BaseWhiskyRequestDto baseWhiskyRequestDtoList) throws HttpServerErrorException {
        if(baseWhiskyRequestDtoList == null)
            return ResponseDto.builder()
                    .code(CRAWLING_DATA_NOT_EXIST.getStatus())
                    .description(Description.FAIL)
                    .errorCode(CRAWLING_DATA_NOT_EXIST.getErrorCode())
                    .errorDescription(CRAWLING_DATA_NOT_EXIST.getErrorDescription())
                    .build();
        Set<String> currentExistedWhisky = baseWhiskyRepository.findSameKoreaNameOrEnglishName(baseWhiskyRequestDtoList);

        baseWhiskyRequestDtoList.getWhiskyList().removeIf(inputWhiskyDTO ->
                currentExistedWhisky.contains(inputWhiskyDTO.getKoreaName()) &&
                currentExistedWhisky.contains(inputWhiskyDTO.getEnglishName())
        );
        if(baseWhiskyRequestDtoList.getWhiskyList().isEmpty())
            return ResponseDto.builder()
                    .code(ALL_WHISKY_INFO_DUPLICATE.getStatus())
                    .description(Description.FAIL)
                    .errorCode(ALL_WHISKY_INFO_DUPLICATE.getErrorCode())
                    .errorDescription(ALL_WHISKY_INFO_DUPLICATE.getErrorDescription())
                    .build();
        baseWhiskyRepository.saveWhiskies(baseWhiskyRequestDtoList);
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .build();
    }

}
