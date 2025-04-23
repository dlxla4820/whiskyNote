package develop.whiskyNote.service;

import develop.whiskyNote.dto.*;
import develop.whiskyNote.entity.*;
import develop.whiskyNote.enums.Description;
import develop.whiskyNote.enums.ErrorCode;
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
import static develop.whiskyNote.utils.CommonUtils.createErrorMessageResponseDtoByErrorMap;
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
        ErrorMessageResponseDto<?,?> response = validateUpsertReview(requestBody);
        if(response != null)
            return ResponseDto.builder()
                    .code(422)
                    .data(response)
                    .build();
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


    public ResponseDto<?> searchMyWhiskyList(String name, String category, String scoreOrder, String dateOrder, String openDateOrder){
        UUID userUuid = CommonUtils.getUserUuidIfAdminOrUser();
        List<MyWhiskyListResponseDto> responseDto = reviewDetailRepository.findAllMyWhiskyListResponseDto(name, category, openDateOrder, scoreOrder, dateOrder, userUuid);
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .data(responseDto)
                .build();
    }


    public ResponseDto<?> updateReview(String reviewUuid, ReviewUpsertRequestDto requestBody) {
        User user = sessionUtils.getUser(RoleType.USER);
        ErrorMessageResponseDto<?,?> response = validateUpsertReview(requestBody);
        if(response != null)
            return ResponseDto.builder()
                    .code(422)
                    .data(response)
                    .build();
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
        ErrorMessageResponseDto<?,?> response = validateUserWhiskyDto(requestBody);
        if(response != null)
            return ResponseDto.builder()
                    .code(422)
                    .data(response)
                    .build();

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
        ErrorMessageResponseDto<?,?> response = validateUserWhiskyDto(requestBody);
        if(response != null)
            return ResponseDto.builder()
                    .code(422)
                    .data(response)
                    .build();
        UserWhisky userWhisky = userWhiskyRepository.findById(UUID.fromString(userWhiskyUuid)).orElseThrow(() -> new ForbiddenException("access deny"));
        ImageFile imageFile = imageFileRepository.findByName(requestBody.getImageName()).orElse(null);
        String imageName = (imageFile == null) ? null : requestBody.getImageName();
        if(imageName != null)
            imageFileDetailRepository.updateImageFileIsSavedByNameAndUserUuid(imageName, userUuid);

        long success = reviewDetailRepository.updateUserWhisky(requestBody,userWhisky.getUuid(), imageName);
        if(success != 0)
            return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .data(requestBody)
                .build();
        else {
            return ResponseDto.builder()
                    .description(Description.FAIL)
                    .code(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
    }

    public ResponseDto<?> deleteWhisky(String userWhiskyUuid){
        User user = sessionUtils.getUser(RoleType.USER);
        reviewDetailRepository.deleteUserWhisky(userWhiskyUuid, user.getUuid());
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


    private ErrorMessageResponseDto<?,?> validateUpsertReview(ReviewUpsertRequestDto requestBody) {
        HashMap<String, List<String>> errorMap = new HashMap<>();
        if(requestBody.getMyWhiskyUuid() == null || requestBody.getMyWhiskyUuid().trim().isEmpty())
            errorMap.put("my_whisky_uuid", Collections.singletonList(String.format(ErrorCode.PARAMETER_INVALID_SPECIFIC.getErrorDescription(), "my_whisky_uuid")));
        if(requestBody.getIsAnonymous() == null)
            errorMap.put("is_anonymous", Collections.singletonList(String.format(ErrorCode.PARAMETER_INVALID_SPECIFIC.getErrorDescription(), "is_anonymous")));
        if(requestBody.getOpenDate() == null)
            errorMap.put("open_date", Collections.singletonList(String.format(ErrorCode.PARAMETER_INVALID_SPECIFIC.getErrorDescription(), "open_date")));
        if(requestBody.getScore() == null)
            errorMap.put("score", Collections.singletonList(String.format(ErrorCode.PARAMETER_INVALID_SPECIFIC.getErrorDescription(), "score")));
        if(errorMap.isEmpty())
            return null;
        return createErrorMessageResponseDtoByErrorMap(errorMap);
    }

    private ErrorMessageResponseDto<?,?> validateUserWhiskyDto(UserWhiskyDto requestBody) {
        HashMap<String, List<String>> errorMap = new HashMap<>();
        if(requestBody.getWhiskyUuid() == null || requestBody.getWhiskyUuid().trim().isEmpty())
            errorMap.put("whisky_uuid", Collections.singletonList(String.format(ErrorCode.PARAMETER_INVALID_SPECIFIC.getErrorDescription(), "whisky_uuid")));
        if(requestBody.getKoreaName() == null || requestBody.getKoreaName().trim().isEmpty())
            errorMap.put("korea_name", Collections.singletonList(String.format(ErrorCode.PARAMETER_INVALID_SPECIFIC.getErrorDescription(), "korea_name")));
        if(requestBody.getEnglishName() == null || requestBody.getEnglishName().trim().isEmpty())
            errorMap.put("english_name", Collections.singletonList(String.format(ErrorCode.PARAMETER_INVALID_SPECIFIC.getErrorDescription(), "english_name")));
        if(requestBody.getCategory() == null || requestBody.getCategory().trim().isEmpty())
            errorMap.put("category", Collections.singletonList(String.format(ErrorCode.PARAMETER_INVALID_SPECIFIC.getErrorDescription(), "category")));
        if(requestBody.getStrength() == null )
            errorMap.put("strength", Collections.singletonList(String.format(ErrorCode.PARAMETER_INVALID_SPECIFIC.getErrorDescription(), "strength")));
        if(requestBody.getOpenDate() == null )
            errorMap.put("open_date", Collections.singletonList(String.format(ErrorCode.PARAMETER_INVALID_SPECIFIC.getErrorDescription(), "open_date")));
        if(errorMap.isEmpty())
            return null;
        return createErrorMessageResponseDtoByErrorMap(errorMap);
    }
}


