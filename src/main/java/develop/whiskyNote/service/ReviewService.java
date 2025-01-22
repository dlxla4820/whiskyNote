package develop.whiskyNote.service;

import develop.whiskyNote.dto.*;
import develop.whiskyNote.entity.Review;
import develop.whiskyNote.entity.User;
import develop.whiskyNote.entity.Whisky;
import develop.whiskyNote.enums.Description;
import develop.whiskyNote.enums.RoleType;
import develop.whiskyNote.exception.ForbiddenException;
import develop.whiskyNote.exception.ModelNotFoundException;
import develop.whiskyNote.repository.ReviewDetailRepository;
import develop.whiskyNote.repository.WhiskyRepository;
import develop.whiskyNote.utils.CommonUtils;
import develop.whiskyNote.utils.ImageHandler;
import develop.whiskyNote.utils.SessionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static develop.whiskyNote.enums.ErrorCode.MAX_PHOTO_OVER;
import static develop.whiskyNote.utils.Constant.WHISKY_NOT_FOUND;

@Service
@Transactional
public class ReviewService {
    private final ReviewDetailRepository reviewDetailRepository;
    private final WhiskyRepository whiskyRepository;
    private final SessionUtils sessionUtils;
    private final ImageHandler imageHandler;

    public ReviewService(ReviewDetailRepository reviewDetailRepository, WhiskyRepository whiskyRepository, SessionUtils sessionUtils, ImageHandler imageHandler) {
        this.reviewDetailRepository = reviewDetailRepository;
        this.whiskyRepository = whiskyRepository;
        this.sessionUtils = sessionUtils;
        this.imageHandler = imageHandler;
    }

    public ResponseDto<?> createReview(ReviewUpsertRequestDto requestBody, List<MultipartFile> images) throws IOException {
        User user = sessionUtils.getUser(RoleType.USER);
        Whisky whisky = whiskyRepository.findById(UUID.fromString(requestBody.getWhiskyUuid())).orElseThrow(() -> new ModelNotFoundException(WHISKY_NOT_FOUND));
        if(images != null && images.size() > 3)
            return ResponseDto.builder()
                    .code(MAX_PHOTO_OVER.getStatus())
                    .description(Description.FAIL)
                    .errorCode(MAX_PHOTO_OVER.getErrorCode())
                    .errorDescription(MAX_PHOTO_OVER.getErrorDescription())
                    .build();

        Map<Long, String> imageUrls = images != null ? imageHandler.save(images, user.getUuid()) : new HashMap<>();
        reviewDetailRepository.saveReview(requestBody,  user, whisky, imageUrls);
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
//    public ResponseDto<?> readMyReview(String whiskyUuid){
//
//    }

    public ResponseDto<?> readMyReviews(String whiskyUuid, int bottleNumber){
        UUID userUuid = CommonUtils.getUserUuidIfAdminOrUser();
        List<MyReviewListResponseDto> responseDtoList = reviewDetailRepository.findMyReviewListByWhiskyUuidAndBottleNumber(whiskyUuid, bottleNumber, userUuid);
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .data(responseDtoList)
                .build();
    }
    public ResponseDto<?> searchWhiskyList(String name, String category) {
        User user = sessionUtils.getUser(RoleType.USER);

        List<WhiskyListResponseDto> responseDtoList = reviewDetailRepository.findAllNameLikeWhiskyName(name, category);
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .data(responseDtoList)
                .build();
    }
/*    public ResponseDto<?> readReviews(String reviewUuid, Integer bottleNum) {
        User user = sessionUtils.getUser(RoleType.USER);
        if(bottleNum == null)
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
    }*/

    public ResponseDto<?> searchMyWhiskyList(String name, String category, String scoreOrder, String dateOrder, String nameOrder){
        UUID userUuid = CommonUtils.getUserUuidIfAdminOrUser();
        List<MyWhiskyListResponseDto> responseDto = reviewDetailRepository.findAllMyWhiskyListResponseDto(name, category, nameOrder, scoreOrder, dateOrder, userUuid);
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .data(responseDto)
                .build();
    }

//    public ResponseDto<?> readReviewList(){
//        User user = sessionUtils.getUser(RoleType.USER);
//        Review review =
//    }

    public ResponseDto<?> updateReview(String reviewUuid,  ReviewUpsertRequestDto requestBody, List<MultipartFile> images) throws IOException {
        User user = sessionUtils.getUser(RoleType.USER);
        Review review = reviewDetailRepository.findReviewByReviewUuid(reviewUuid);

        if(review != null && review.getUser().getUuid() != user.getUuid())
            throw new ForbiddenException("Access Denied");

        if(images != null && images.size() > 3)
            return ResponseDto.builder()
                    .code(MAX_PHOTO_OVER.getStatus())
                    .description(Description.FAIL)
                    .errorCode(MAX_PHOTO_OVER.getErrorCode())
                    .errorDescription(MAX_PHOTO_OVER.getErrorDescription())
                    .build();

        Map<Long, String> imageUrls = images != null ? imageHandler.save(images, user.getUuid()) : new HashMap<>();
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

    public ResponseDto<?> createWhisky(WhiskyCreateRequestDto requestBody, MultipartFile image) throws IOException {
        UUID userUuid = CommonUtils.getUserUuidIfAdminOrUser();
        String imageUrl = image == null ? null : imageHandler.save(image, userUuid);
        reviewDetailRepository.saveWhisky(requestBody, userUuid, imageUrl);

        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .build();
    }

}
