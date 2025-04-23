package develop.whiskyNote.service;

import static develop.whiskyNote.utils.CommonUtils.createErrorMessageResponseDtoByErrorMap;

import develop.whiskyNote.dto.ErrorMessageResponseDto;
import develop.whiskyNote.dto.OtherReviewGetReqeustDto;
import develop.whiskyNote.dto.OtherReviewGetResponseDto;
import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.enums.Description;
import develop.whiskyNote.enums.ErrorCode;
import develop.whiskyNote.exception.ReviewLikeException;
import develop.whiskyNote.repository.OtherUserReviewRepository;
import develop.whiskyNote.utils.CommonUtils;
import develop.whiskyNote.utils.OrderParser;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Transactional
@Service
public class OtherUserReviewService {
    private final OtherUserReviewRepository otherUserReviewRepository;

    public OtherUserReviewService(
            OtherUserReviewRepository otherUserReviewRepository
    ) {
        this.otherUserReviewRepository = otherUserReviewRepository;
    }
    public ResponseDto<?> createReviewLikeMapping(String inputUuid){
        ErrorMessageResponseDto<?,?> response = validateinputUuid(inputUuid);
        if(response != null)
            return ResponseDto.builder()
                .code(422)
                .data(response)
                .build();
        //현재 로그인 한 유저 가져오기
        UUID user = CommonUtils.getUserUuidIfAdminOrUser();
        UUID reviewUuid = UUID.fromString(inputUuid);
        //테이블에 있는 지 확인, 있을 시 에러 > 이미 생성
        if(otherUserReviewRepository.checkReviewLikeMapping(reviewUuid, user) != null){
            throw new ReviewLikeException("REVIEW_LIKE_ALREADY_ADD");//임시
        }
        otherUserReviewRepository.saveLikeMapping(reviewUuid, user);
        Integer reviewCount = otherUserReviewRepository.getLikeCount(reviewUuid);
        return ResponseDto.builder()
                .code(HttpStatus.OK.value())
                .description(Description.SUCCESS)
                .data(reviewCount)
                .build();
    }

    public ResponseDto<?> deleteReviewLikeMapping(String inputUuid){
        ErrorMessageResponseDto<?,?> response = validateinputUuid(inputUuid);
        if(response != null)
            return ResponseDto.builder()
                .code(422)
                .data(response)
                .build();
        UUID user = CommonUtils.getUserUuidIfAdminOrUser();
        UUID reviewUuid = UUID.fromString(inputUuid);
        //삭제, 없을 시 에러 > 이미 취소
        if(otherUserReviewRepository.checkReviewLikeMapping(reviewUuid, user) == null){
            throw new ReviewLikeException("REVIEW_LIKE_NOT_EXIST");
        }otherUserReviewRepository.deleteLikeMapping(reviewUuid, user);
        Integer reviewCount = otherUserReviewRepository.getLikeCount(reviewUuid);
        return ResponseDto.builder()
                .code(HttpStatus.OK.value())
                .description(Description.SUCCESS)
                .data(reviewCount)
                .build();
    }

    public ResponseDto<?> searchOtherUserReviewUsingKeyword(
            String mainSearchWord,
            String subSearchWord,
            String likeOrder,
            String scoreOrder,
            String createdOrder,
            String nameOrder,
            int page,
            int size
    ) {
        UUID currentUser = CommonUtils.getUserUuidIfAdminOrUser();
        boolean isMainKorean = CommonUtils.containsKorean(mainSearchWord);
        boolean isSubKorean = subSearchWord != null && CommonUtils.containsKorean(subSearchWord);
        //dto에 매핑
        OtherReviewGetReqeustDto dto = new OtherReviewGetReqeustDto();
        dto.setMainSearchWord(mainSearchWord);
        dto.setSubSearchWord(subSearchWord);
        dto.setMainKorean(isMainKorean);
        dto.setSubKorean(isSubKorean);
        dto.setLikeOrder(OrderParser.parse(likeOrder));
        dto.setScoreOrder(OrderParser.parse(scoreOrder));
        dto.setCreatedOrder(OrderParser.parse(createdOrder));
        dto.setNameOrder(OrderParser.parse(nameOrder));
        dto.setPage(page);
        dto.setSize(size);
        dto.setSearchFromBaseWhisky(otherUserReviewRepository.checkBaseWhiskyExist(dto));
        //page 객체 생성
        PageRequest pageable = PageRequest.of(page, size);
        Page<OtherReviewGetResponseDto> result = otherUserReviewRepository.findOtherUserReview(dto, currentUser, pageable);

        return ResponseDto.builder()
                .code(result.isEmpty() ? HttpStatus.NO_CONTENT.value() : HttpStatus.OK.value())
                .description(Description.SUCCESS)
                .data(result)
                .build();
    }

    private ErrorMessageResponseDto<?,?> validateinputUuid(String inputUuid){
        HashMap<String, List<String>> errorMap = new HashMap<>();
        if(inputUuid == null || inputUuid.isEmpty()) errorMap.put("inputUuid", Collections.singletonList(String.format(
            ErrorCode.PARAMETER_INVALID_SPECIFIC.getErrorDescription(), "inputUuid")));
        if (errorMap.isEmpty()) return null;
        return createErrorMessageResponseDtoByErrorMap(errorMap);
    }

}
