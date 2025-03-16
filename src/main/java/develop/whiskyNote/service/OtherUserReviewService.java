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
import develop.whiskyNote.utils.RedissonLock;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Transactional
@Service
public class OtherUserReviewService {
    private final OtherUserReviewRepository otherUserReviewRepository;
    private final RedissonClient redisson;

    public OtherUserReviewService(
            OtherUserReviewRepository otherUserReviewRepository,
            RedissonClient redisson
    ) {
        this.otherUserReviewRepository = otherUserReviewRepository;
        this.redisson = redisson;
    }
    
    @RedissonLock(key = "#reviewCountUuid")
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
    @RedissonLock(key = "#reviewCountUuid")
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



    public ResponseDto<?> searchOtherUserReviewUsingKeyword(OtherReviewGetReqeustDto otherReviewGetReqeustDto) {
        //현재 접속 유저 찾기
        UUID currentUser = CommonUtils.getUserUuidIfAdminOrUser();
        //repository에 값 전달하기
        List<OtherReviewGetResponseDto> result = otherUserReviewRepository.findOtherUserReview(otherReviewGetReqeustDto, currentUser);

        return ResponseDto.builder()
                .code(result.isEmpty()? HttpStatus.NO_CONTENT.value() : HttpStatus.OK.value())
                .description(Description.SUCCESS)
                .data(result)
                .build();
    }

}
