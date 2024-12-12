package develop.whiskyNote.service;

import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.dto.ReviewUpsertRequestDto;
import develop.whiskyNote.dto.ReviewResponseDto;
import develop.whiskyNote.entity.Review;
import develop.whiskyNote.entity.User;
import develop.whiskyNote.enums.Description;
import develop.whiskyNote.enums.RoleType;
import develop.whiskyNote.exception.ForbiddenException;
import develop.whiskyNote.repository.ReviewDetailRepository;
import develop.whiskyNote.utils.ImageHandler;
import develop.whiskyNote.utils.SessionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

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

    public ResponseDto<?> createReview(ReviewUpsertRequestDto requestBody) throws IOException {
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
        reviewDetailRepository.saveReview(requestBody,  user, imageUrls);
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .build();
    }
    public ResponseDto<?> readReview(String reviewUuid) {
        User user = sessionUtils.getUser(RoleType.USER);
        Review review = reviewDetailRepository.findReviewByReviewUuid(reviewUuid);
        if(review != null && review.getUser().getUuid() != user.getUuid())
            throw new ForbiddenException("Access Denied");

        ReviewResponseDto responseDto = review == null ? null : ReviewResponseDto.builder()
                .content(review.getContent())
                .imageUrl(review.getImageUrl())
                .isAnonymous(review.getIsAnonymous())
                .openDate(review.getOpenDate())
                .tags(review.getTags())
                .score(review.getScore())
                .build();

        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .data(responseDto)
                .build();
    }

    public ResponseDto<?> updateReview(String reviewUuid, ReviewUpsertRequestDto requestBody) throws IOException {
        User user = sessionUtils.getUser(RoleType.USER);
        MultipartFile[] images = requestBody.getImages();
        Review review = reviewDetailRepository.findReviewByReviewUuid(reviewUuid);
        if(review != null && review.getUser().getUuid() != user.getUuid())
            throw new ForbiddenException("Access Denied");

        if(Arrays.stream(images).count() > 3)
            return ResponseDto.builder()
                    .code(MAX_PHOTO_OVER.getStatus())
                    .description(Description.FAIL)
                    .errorCode(MAX_PHOTO_OVER.getErrorCode())
                    .errorDescription(MAX_PHOTO_OVER.getErrorDescription())
                    .build();
        Map<Long, String> imageUrls = imageHandler.save(images, user.getUuid());

        reviewDetailRepository.updateReviewByReviewUuid(requestBody, reviewUuid, imageUrls);
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .build();
    }

    public ResponseDto<?> deleteReview(String reviewUuid) {
        User user = sessionUtils.getUser(RoleType.USER);
        Review review = reviewDetailRepository.findReviewByReviewUuid(reviewUuid);
        if(review != null && review.getUser().getUuid() != user.getUuid())
            throw new ForbiddenException("Access Denied");

        reviewDetailRepository.deleteReviewByReviewUuid(reviewUuid);

        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .build();
    }

}
