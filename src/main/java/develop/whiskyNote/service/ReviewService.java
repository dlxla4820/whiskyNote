package develop.whiskyNote.service;

import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.dto.ReviewCreateRequestDto;
import develop.whiskyNote.dto.ReviewResponseDto;
import develop.whiskyNote.dto.UserRequestDto;
import develop.whiskyNote.entity.User;
import develop.whiskyNote.enums.Description;
import develop.whiskyNote.enums.RoleType;
import develop.whiskyNote.repository.ReviewDetailRepository;
import develop.whiskyNote.repository.ReviewRepository;
import develop.whiskyNote.utils.CommonUtils;
import develop.whiskyNote.utils.ImageHandler;
import develop.whiskyNote.utils.SessionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import static develop.whiskyNote.enums.ErrorCode.MAX_PHOTO_OVER;

@Service
@Transactional
public class ReviewService {
    private final ReviewDetailRepository reviewDetailRepository;
    private final SessionUtils sessionUtils;
    private final ImageHandler imageHandler;

    public ReviewService(ReviewDetailRepository reviewDetailRepository, SessionUtils sessionUtils, ImageHandler imageHandler) {
        this.reviewDetailRepository = reviewDetailRepository;
        this.sessionUtils = sessionUtils;
        this.imageHandler = imageHandler;
    }

    public ResponseDto<?> createReview(ReviewCreateRequestDto requestBody) throws IOException {
        User user = sessionUtils.getUser(RoleType.USER);
        MultipartFile[] images = requestBody.getImages();
        if(Arrays.stream(images).count() > 3)
            return ResponseDto.builder()
                    .code(MAX_PHOTO_OVER.getStatus())
                    .description(Description.FAIL)
                    .errorCode(MAX_PHOTO_OVER.getErrorCode())
                    .errorDescription(MAX_PHOTO_OVER.getErrorDescription())
                    .build();

        Map<Long, String> imageUrls =   imageHandler.save(images, user.getUuid());
        reviewDetailRepository.saveReview(requestBody, user, imageUrls);
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .build();
    }
    public ResponseDto<?> readReview() {
        User user = sessionUtils.getUser(RoleType.USER);
        ReviewResponseDto responseDto = reviewDetailRepository.findReviewByUserUuid(user.getUuid());
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .data(responseDto)
                .build();
    }
}
