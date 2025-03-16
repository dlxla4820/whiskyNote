package develop.whiskyNote.service;

import develop.whiskyNote.dto.*;
import develop.whiskyNote.entity.*;
import develop.whiskyNote.enums.Description;
import develop.whiskyNote.enums.RoleType;
import develop.whiskyNote.exception.ForbiddenException;
import develop.whiskyNote.exception.ModelNotFoundException;
import develop.whiskyNote.repository.*;
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
    private final ReviewRepository reviewRepository;
    private final UserWhiskyRepository userWhiskyRepository;
    private final ImageFileRepository imageFileRepository;
    private final ImageFileDetailRepository imageFileDetailRepository;
    private final SessionUtils sessionUtils;
    private final ImageHandler imageHandler;
    private final OtherUserReviewRepository otherUserReviewRepository;

    public ReviewService(ReviewDetailRepository reviewDetailRepository, WhiskyRepository whiskyRepository, ReviewRepository reviewRepository, UserWhiskyRepository userWhiskyRepository, ImageFileRepository imageFileRepository, ImageFileDetailRepository imageFileDetailRepository, SessionUtils sessionUtils, ImageHandler imageHandler, OtherUserReviewRepository otherUserReviewRepository) {
        this.reviewDetailRepository = reviewDetailRepository;
        this.whiskyRepository = whiskyRepository;
        this.reviewRepository = reviewRepository;
        this.userWhiskyRepository = userWhiskyRepository;
        this.imageFileRepository = imageFileRepository;
        this.imageFileDetailRepository = imageFileDetailRepository;
        this.sessionUtils = sessionUtils;
        this.imageHandler = imageHandler;
        this.otherUserReviewRepository = otherUserReviewRepository;
    }

    public ResponseDto<?> createReview(ReviewUpsertRequestDto requestBody) {
        User user = sessionUtils.getUser(RoleType.USER);
        UserWhisky userWhisky = userWhiskyRepository.findById(UUID.fromString(requestBody.getMyWhiskyUuid())).orElseThrow(() -> new ModelNotFoundException(WHISKY_NOT_FOUND));
        if(requestBody.getImageNames() != null && requestBody.getImageNames().size() > 3)
            return ResponseDto.builder()
                    .code(MAX_PHOTO_OVER.getStatus())
                    .description(Description.FAIL)
                    .errorCode(MAX_PHOTO_OVER.getErrorCode())
                    .errorDescription(MAX_PHOTO_OVER.getErrorDescription())
                    .build();
        Review review = requestBody.toReview(userWhisky, user);
        reviewRepository.save(review);
        //ReviewLikeCount 데이터 생성
        if(requestBody.getIsAnonymous()) otherUserReviewRepository.save(review.getUuid());
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
                .imageNames(review.getImageNames())
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

    public ResponseDto<?> readMyReviews(String userWhiskyUuid, String order){
        UUID userUuid = CommonUtils.getUserUuidIfAdminOrUser();
        List<MyReviewListResponseDto> responseDtoList = reviewDetailRepository.findMyReviewListByUserWhiskyUuid(userWhiskyUuid, userUuid, order);
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .data(responseDtoList)
                .build();
    }
    public ResponseDto<?> searchWhiskyList(String name, String category) {
        User user = sessionUtils.getUser(RoleType.USER);

        List<WhiskyDto> responseDtoList = reviewDetailRepository.findAllNameListWhiskyName(name, category);
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

    public ResponseDto<?> updateReview(String reviewUuid, ReviewUpsertRequestDto requestBody) {
        User user = sessionUtils.getUser(RoleType.USER);
        Review review = reviewDetailRepository.findReviewByReviewUuid(reviewUuid);

        if(review != null && review.getUser().getUuid() != user.getUuid())
            throw new ForbiddenException("Access Denied");

        if(requestBody.getImageNames() != null && requestBody.getImageNames().size() > 3)
            return ResponseDto.builder()
                    .code(MAX_PHOTO_OVER.getStatus())
                    .description(Description.FAIL)
                    .errorCode(MAX_PHOTO_OVER.getErrorCode())
                    .errorDescription(MAX_PHOTO_OVER.getErrorDescription())
                    .build();

        reviewDetailRepository.updateReviewByReviewUuid(requestBody, reviewUuid);
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

    public ResponseDto<?> createWhisky(UserWhiskyDto requestBody){
        UUID userUuid = CommonUtils.getUserUuidIfAdminOrUser();
        Whisky whisky = whiskyRepository.findById(UUID.fromString(requestBody.getWhiskyUuid())).orElseThrow(() -> new ModelNotFoundException("Whisky Not Found"));
        ImageFile imageFile = imageFileRepository.findByName(requestBody.getImageName()).orElse(null);
        String imageName = (imageFile == null) ? null : requestBody.getImageName();
        userWhiskyRepository.save(requestBody.toUserWhisky(whisky, userUuid, imageName));
        if(imageName != null)
            imageFileDetailRepository.updateImageFileIsSavedByNameAndUserUuid(imageName, userUuid);
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .build();
    }

    public ResponseDto<?> updateWhisky(String userWhiskyUuid, UserWhiskyDto requestBody) {
        UUID userUuid = CommonUtils.getUserUuidIfAdminOrUser();
        UserWhisky userWhisky = userWhiskyRepository.findById(UUID.fromString(userWhiskyUuid)).orElseThrow(() -> new ForbiddenException("access deny"));

        ImageFile imageFile = imageFileRepository.findByName(requestBody.getImageName()).orElse(null);
        String imageName = (imageFile == null) ? null : requestBody.getImageName();
        if(imageName != null)
            imageFileDetailRepository.updateImageFileIsSavedByNameAndUserUuid(imageName, userUuid);

        reviewDetailRepository.updateUserWhisky(requestBody,userWhisky.getUserUuid(), imageName);
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .build();
    }

//    public ResponseDto<?> insertWhisky()  {
//        Whisky whisky = Whisky.builder()
//                .koreaName("글렌리벳 12")
//                .englishName("glenrivet 12")
//                .category("slinglemolt")
//                .strength(40.0)
//                .country("scotland")
//                .build();
//        whiskyRepository.save(whisky);
//        return ResponseDto.builder()
//                .description(Description.SUCCESS)
//                .code(HttpStatus.OK.value())
//                .build();
//    }
}


