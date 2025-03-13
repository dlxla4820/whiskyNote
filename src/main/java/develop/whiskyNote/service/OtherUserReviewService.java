package develop.whiskyNote.service;

import develop.whiskyNote.dto.GetOtherReviewListReqeustDto;
import develop.whiskyNote.dto.GetOtherReviewListResponseDto;
import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.enums.Description;
import develop.whiskyNote.repository.OtherUserReviewRepository;
import develop.whiskyNote.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class OtherUserReviewService {
    private final OtherUserReviewRepository otherUserReviewRepository;

    public OtherUserReviewService(
            OtherUserReviewRepository otherUserReviewRepository
    ) {
        this.otherUserReviewRepository = otherUserReviewRepository;
    }

    public ResponseDto<?> searchOtherUserReviewUsingKeyword(GetOtherReviewListReqeustDto getOtherReviewListReqeustDto) {
        //현재 접속 유저 찾기
        UUID currentUser = CommonUtils.getUserUuidIfAdminOrUser();
        //repository에 값 전달하기
        List<GetOtherReviewListResponseDto> result = otherUserReviewRepository.findOtherUserReview(getOtherReviewListReqeustDto);

        return ResponseDto.builder()
                .code(result.isEmpty()? HttpStatus.NO_CONTENT.value() : HttpStatus.OK.value())
                .description(Description.SUCCESS)
                .data(result)
                .build();
    }
}
