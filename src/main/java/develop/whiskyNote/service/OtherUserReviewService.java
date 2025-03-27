package develop.whiskyNote.service;

import develop.whiskyNote.dto.OtherReviewGetReqeustDto;
import develop.whiskyNote.dto.OtherReviewGetResponseDto;
import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.entity.ReviewLikeMapping;
import develop.whiskyNote.enums.Description;
import develop.whiskyNote.exception.ForbiddenException;
import develop.whiskyNote.exception.ReviewLikeException;
import develop.whiskyNote.repository.OtherUserReviewRepository;
import develop.whiskyNote.utils.CommonUtils;
import develop.whiskyNote.utils.OrderParser;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public ResponseDto<?> createReviewLikeMapping(String reviewCountUuid){
        //현재 로그인 한 유저 가져오기
        UUID user = CommonUtils.getUserUuidIfAdminOrUser();
        //테이블에 있는 지 확인, 있을 시 에러 > 이미 생성
        if(otherUserReviewRepository.checkReviewLikeMapping(UUID.fromString(reviewCountUuid), user) != null){
            throw new ReviewLikeException("REVIEW_LIKE_ALREADY_ADD");//임시
        }
        Integer reviewCount = otherUserReviewRepository.saveLikeMappingAndGetReviewCount(UUID.fromString(reviewCountUuid), user);
        return ResponseDto.builder()
                .code(HttpStatus.OK.value())
                .description(Description.SUCCESS)
                .data(reviewCount)
                .build();
    }

    public ResponseDto<?> deleteReviewLikeMapping(String reviewCountUuid){
        UUID user = CommonUtils.getUserUuidIfAdminOrUser();
        //삭제, 없을 시 에러 > 이미 취소
        if(otherUserReviewRepository.checkReviewLikeMapping(UUID.fromString(reviewCountUuid), user) == null){
            throw new ReviewLikeException("REVIEW_LIKE_NOT_EXIST");
        }
        Integer reviewCount = otherUserReviewRepository.deleteLikeMappingAndGetReviewCount(UUID.fromString(reviewCountUuid), user);
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

        PageRequest pageable = PageRequest.of(page, size);
        Page<OtherReviewGetResponseDto> result = otherUserReviewRepository.findOtherUserReview(dto, currentUser, pageable);

        return ResponseDto.builder()
                .code(result.isEmpty() ? HttpStatus.NO_CONTENT.value() : HttpStatus.OK.value())
                .description(Description.SUCCESS)
                .data(result)
                .build();
    }
}
