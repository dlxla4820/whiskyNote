package develop.whiskyNote.service;

import develop.whiskyNote.dto.BaseWhiskyFiveResponseDto;
import develop.whiskyNote.dto.BaseWhiskyRequestDto;
import develop.whiskyNote.dto.ErrorMessageResponseDto;
import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.entity.Whisky;
import develop.whiskyNote.enums.Description;
import develop.whiskyNote.enums.ErrorCode;
import develop.whiskyNote.repository.BaseWhiskyRepository;
import develop.whiskyNote.utils.CommonUtils;
import java.util.Collections;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Set;

import static develop.whiskyNote.enums.ErrorCode.ALL_WHISKY_INFO_DUPLICATE;
import static develop.whiskyNote.utils.CommonUtils.createErrorMessageResponseDtoByErrorMap;

@Service
@Transactional
public class BaseWhiskyService {
    private final BaseWhiskyRepository baseWhiskyRepository;

    public BaseWhiskyService(BaseWhiskyRepository baseWhiskyRepository) {
        this.baseWhiskyRepository = baseWhiskyRepository;
    }
    public ResponseDto<?> searchFiveBaseWhiskyUsingKeyword(String keyword) {
        ErrorMessageResponseDto<?,?> response = validateKeyword(keyword);
        if(response != null)
            return ResponseDto.builder()
                .code(422)
                .data(response)
                .build();
        List<BaseWhiskyFiveResponseDto> result = CommonUtils.containsKorean(keyword)? baseWhiskyRepository.getWhiskyByKoreaName(keyword) : baseWhiskyRepository.getWhiskyByEnglishName(keyword);
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .data(result)
                .build();
    }

    public ResponseDto<?> updateBaseWhisky(BaseWhiskyRequestDto baseWhiskyRequestDtoList) throws HttpServerErrorException {
        ErrorMessageResponseDto<?,?> response = validateBaseWhiskyRequestDto(baseWhiskyRequestDtoList);
        if(response != null)
            return ResponseDto.builder()
                .code(422)
                .data(response)
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

    private ErrorMessageResponseDto<?,?> validateKeyword(String keyword){
        HashMap<String, List<String>> errorMap = new HashMap<>();
        if(keyword == null || keyword.isEmpty()) errorMap.put("keyword", Collections.singletonList(String.format(ErrorCode.PARAMETER_INVALID_SPECIFIC.getErrorDescription(), "keyword")));
        if (errorMap.isEmpty()) return null;
        return createErrorMessageResponseDtoByErrorMap(errorMap);
    }
    private ErrorMessageResponseDto<?,?> validateBaseWhiskyRequestDto(BaseWhiskyRequestDto requestBody){
        HashMap<String, List<String>> errorMap = new HashMap<>();
        if(requestBody.getWhiskyList() == null || requestBody.getWhiskyList().isEmpty())
            errorMap.put("whisky_list", Collections.singletonList(String.format(ErrorCode.PARAMETER_INVALID_SPECIFIC.getErrorDescription(), "whisky_list")));
        if(errorMap.isEmpty()) return null;
        return createErrorMessageResponseDtoByErrorMap(errorMap);
    }

}
