package develop.whiskyNote.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import develop.whiskyNote.config.LambdaConfig;
import develop.whiskyNote.dto.GlobalWhiskyCrawlingDataDto;
import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.dto.SearchRequestDto;
import develop.whiskyNote.entity.User;
import develop.whiskyNote.entity.Whisky;
import develop.whiskyNote.enums.Description;
import develop.whiskyNote.enums.RoleType;
import develop.whiskyNote.repository.GlobalWhiskyRepository;
import develop.whiskyNote.utils.SessionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.zip.ZipInputStream;

import static develop.whiskyNote.enums.ErrorCode.CRAWLING_DATA_NOT_EXIST;

@Service
@Transactional
public class GlobalWhiskyService {
    private final GlobalWhiskyRepository globalWhiskyRepository;
    private final SessionUtils sessionUtils;
    private final LambdaConfig lambdaConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GlobalWhiskyService(GlobalWhiskyRepository globalWhiskyRepository, SessionUtils sessionUtils, LambdaConfig lambdaConfig, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.globalWhiskyRepository = globalWhiskyRepository;
        this.sessionUtils = sessionUtils;
        this.lambdaConfig = lambdaConfig;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public ResponseDto<?> searchWhiskyInGlobalWhisky(SearchRequestDto requestBody) {
        String searchName = requestBody.getWhiskyName() == null || requestBody.getWhiskyName().isEmpty() ? "": requestBody.getWhiskyName();
        Integer pageNum = requestBody.getPageNum() == null || requestBody.getPageNum()>=0 ? 1 : requestBody.getPageNum();
        List<Whisky> result = globalWhiskyRepository.getWhiskeis(searchName, pageNum);
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .data(result)
                .build();
    }

    public ResponseDto<?> updateGlobalWhisky() throws IOException, HttpServerErrorException {
        ResponseEntity<byte[]> response = restTemplate.getForEntity(lambdaConfig.getUrl(), byte[].class);
        byte[] responseBody = response.getBody();
        InputStream inputStream = new ByteArrayInputStream(responseBody);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        String jsonContent = null;
        //zip파일 유효성 검증(매직넘버)
//        if (!(zipData[0] == 'P' && zipData[1] == 'K' && zipData[2] == 3 && zipData[3] == 4)) {
//            throw new IllegalArgumentException("Invalid ZIP file format.");
//        }
        while(zipInputStream.getNextEntry() != null){
            byte[] buffer = zipInputStream.readAllBytes();
            jsonContent = new String(buffer,StandardCharsets.UTF_8);
            break;
        }
        zipInputStream.close();
        if(jsonContent == null)
            return ResponseDto.builder()
                    .code(CRAWLING_DATA_NOT_EXIST.getStatus())
                    .description(Description.FAIL)
                    .errorCode(CRAWLING_DATA_NOT_EXIST.getErrorCode())
                    .errorDescription(CRAWLING_DATA_NOT_EXIST.getErrorDescription())
                    .build();
        List<GlobalWhiskyCrawlingDataDto> whiskyCrawlingData= objectMapper.readValue(jsonContent, objectMapper.getTypeFactory().constructCollectionType(List.class, GlobalWhiskyCrawlingDataDto.class));
        globalWhiskyRepository.saveWhiskies(whiskyCrawlingData);
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .build();
    }

}
