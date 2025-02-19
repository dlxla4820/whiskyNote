package develop.whiskyNote.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import develop.whiskyNote.config.LambdaConfig;
import develop.whiskyNote.dto.BaseWhiskyRequestDto;
import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.dto.BaseWhiskySearchRequestDto;
import develop.whiskyNote.entity.Whisky;
import develop.whiskyNote.enums.Description;
import develop.whiskyNote.repository.BaseWhiskyRepository;
import develop.whiskyNote.utils.SessionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

import static develop.whiskyNote.enums.ErrorCode.ALL_WHISKY_INFO_DUPLICATE;
import static develop.whiskyNote.enums.ErrorCode.CRAWLING_DATA_NOT_EXIST;

@Service
@Transactional
public class BaseWhiskyService {
    private final BaseWhiskyRepository baseWhiskyRepository;
    private final SessionUtils sessionUtils;
    private final LambdaConfig lambdaConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public BaseWhiskyService(BaseWhiskyRepository baseWhiskyRepository, SessionUtils sessionUtils, LambdaConfig lambdaConfig, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.baseWhiskyRepository = baseWhiskyRepository;
        this.sessionUtils = sessionUtils;
        this.lambdaConfig = lambdaConfig;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public ResponseDto<?> getBaseWhiskyWithPage(BaseWhiskySearchRequestDto requestBody) {
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
