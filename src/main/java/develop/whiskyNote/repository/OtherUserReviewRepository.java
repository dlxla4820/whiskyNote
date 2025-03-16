package develop.whiskyNote.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import develop.whiskyNote.dto.OtherReviewGetReqeustDto;
import develop.whiskyNote.dto.OtherReviewGetResponseDto;
import develop.whiskyNote.entity.ReviewLikeCount;
import develop.whiskyNote.entity.ReviewLikeMapping;
import develop.whiskyNote.entity.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static develop.whiskyNote.entity.QReview.review;
import static develop.whiskyNote.entity.QReviewLikeCount.reviewLikeCount;
import static develop.whiskyNote.entity.QReviewLikeMapping.reviewLikeMapping;

@Repository
public class OtherUserReviewRepository {
    private final ReviewLikeCountRepository reviewLikeCountRepository;
    private final BaseWhiskyRepository baseWhiskyRepository;
    private final UserRepository userRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final ReviewLikeMappingRepository reviewLikeMappingRepository;


    public OtherUserReviewRepository(ReviewLikeCountRepository reviewLikeCountRepository, BaseWhiskyRepository baseWhiskyRepository, UserRepository userRepository, JPAQueryFactory jpaQueryFactory, ReviewLikeMappingRepository reviewLikeMappingRepository) {
        this.reviewLikeCountRepository = reviewLikeCountRepository;
        this.baseWhiskyRepository = baseWhiskyRepository;
        this.userRepository = userRepository;
        this.jpaQueryFactory = jpaQueryFactory;
        this.reviewLikeMappingRepository = reviewLikeMappingRepository;
    }
    //좋아요 갯수 생성하기
    public void save(UUID review) {
        ReviewLikeCount reviewLikeCount = ReviewLikeCount.builder().reviewId(review).build();
        reviewLikeCountRepository.save(reviewLikeCount);
    }

    public ReviewLikeMapping checkReviewLikeMapping(UUID reviewId, UUID userId) {
        return jpaQueryFactory.selectFrom(reviewLikeMapping)
                .where(reviewLikeMapping.reviewLikeCount.uuid.eq(reviewId))
                .where(reviewLikeMapping.user.uuid.eq(userId))
                .fetchOne();
    }

    //좋아요 누르기
    public Integer saveLikeMappingAndGetReviewCount(UUID reviewCountUuid, UUID userId) {
        ReviewLikeCount likeCount = reviewLikeCountRepository.findById(reviewCountUuid)
                .orElseThrow(() -> new IllegalArgumentException("ReviewLikeCount not found for UUID: " + reviewCountUuid));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found for UUID: " + userId));
        ReviewLikeMapping reviewLikeMapping = ReviewLikeMapping.builder()
                .reviewLikeCount(likeCount)
                .user(user)
                .build();
        reviewLikeMappingRepository.save(reviewLikeMapping);
        jpaQueryFactory.update(reviewLikeCount)
                .set(reviewLikeCount.likeCount, reviewLikeCount.likeCount.add(1))
                .where(reviewLikeCount.uuid.eq(reviewCountUuid))
                .execute();
        return jpaQueryFactory.select(reviewLikeCount.likeCount)
                .from(reviewLikeCount)
                .where(reviewLikeCount.uuid.eq(reviewCountUuid))
                .fetchOne();
    }


    //좋아요 취소하기

    public Integer deleteLikeMappingAndGetReviewCount(UUID reviewCountUuid, UUID userId) {
        jpaQueryFactory.delete(reviewLikeMapping)
                .where(reviewLikeMapping.user.uuid.eq(userId))
                .where(reviewLikeCount.uuid.eq(reviewCountUuid))
                .execute();
        jpaQueryFactory.update(reviewLikeCount)
                .set(reviewLikeCount.likeCount,reviewLikeCount.likeCount.add(-1))
                .where(reviewLikeCount.uuid.eq(reviewCountUuid))
                .execute();
        return jpaQueryFactory.select(reviewLikeCount.likeCount)
                .from(reviewLikeCount)
                .where(reviewLikeCount.uuid.eq(reviewCountUuid))
                .fetchOne();
    }

    //다른 유저 리뷰 읽어오기
    public List<OtherReviewGetResponseDto> findOtherUserReview(OtherReviewGetReqeustDto reqeustDto, UUID userId) {
        return jpaQueryFactory.select(
                Projections.fields(OtherReviewGetResponseDto.class,
                            review.uuid.as("reviewUuid"),
                            review.userWhisky.uuid.as("userWhiskyUuid"),
                            review.isAnonymous,//없어도 될거 같은데 득환이가 넣어달라함
                            review.openDate,
                            review.content,
                            review.score,
                            review.tags,
                            review.imageNames.as("imageNames"),
                            Expressions.dateTemplate(LocalDateTime.class, "GREATEST({0}, {1})", review.regDate, review.modDate)
                                .as("lastUpdateDate"),
                            Expressions.booleanTemplate("CASE WHEN {0} IS NOT NULL THEN true ELSE false END", reviewLikeMapping.user.uuid)
                                .as("likeState"),
                            reviewLikeCount.likeCount.as("likeCount"),
                            reviewLikeCount.uuid.as("reviewLikeCountUuid")
                        ))
                .from(review)
                .leftJoin(reviewLikeCount).on(reviewLikeCount.reviewId.eq(review.uuid))
                .leftJoin(reviewLikeMapping).on(reviewLikeMapping.reviewLikeCount.eq(reviewLikeCount).and(reviewLikeMapping.user.uuid.eq(userId)))
                .where(
                        review.userWhisky.whisky.uuid.eq(UUID.fromString(reqeustDto.getBaseWhiskyUuid())),//좋아요 여부 확인
                        review.isAnonymous.eq(true),//공개 여부 확인
                        getUserWhiskyNameCondition(reqeustDto),//한글 이름 또는 영어 이름 검색
                        getLastIndexCondition(reqeustDto)
                )
                .orderBy(
                        //좋아요, 이름, 점수, 작성일 순 정렬
                        getLikeOrder(reqeustDto),
                        getwhiskyNameOrder(reqeustDto),
                        getScoreOrder(reqeustDto),
                        getCreatedAtOrder(reqeustDto)
                )
                .limit(10)
                .fetch();
    }

    private BooleanExpression getLastIndexCondition(OtherReviewGetReqeustDto reqeustDto) {
        if(reqeustDto.getLastIndex() == null || reqeustDto.getLastIndex().isEmpty()){
            return null;//첫페이지
        }
        return review.uuid.gt(UUID.fromString(reqeustDto.getLastIndex()));
    }

    private BooleanExpression getUserWhiskyNameCondition(OtherReviewGetReqeustDto reqeustDto) {
        if(reqeustDto.getSearchWord() == null || reqeustDto.getSearchWord().isEmpty()){
            return null;
        }
        if(reqeustDto.isKorean()){
            return review.userWhisky.koreaName.containsIgnoreCase(reqeustDto.getSearchWord());
        }else{
            return review.userWhisky.englishName.containsIgnoreCase(reqeustDto.getSearchWord());
        }
    }

    private OrderSpecifier<?> getLikeOrder(OtherReviewGetReqeustDto reqeustDto) {
        return reqeustDto.isLikeAsc() ? reviewLikeCount.likeCount.asc() : reviewLikeCount.likeCount.desc();
    }

    private OrderSpecifier<?> getScoreOrder(OtherReviewGetReqeustDto reqeustDto) {
        return reqeustDto.isScoreAsc() ? review.score.asc() : review.score.desc();
    }

    private OrderSpecifier<?> getCreatedAtOrder(OtherReviewGetReqeustDto requestDto) {
        return requestDto.isCreatedAtAsc()
                ? Expressions.dateTemplate(LocalDateTime.class, "GREATEST({0}, {1})", review.regDate, review.modDate).asc()
                : Expressions.dateTemplate(LocalDateTime.class, "GREATEST({0}, {1})", review.regDate, review.modDate).desc();
    }
    private OrderSpecifier<?> getwhiskyNameOrder(OtherReviewGetReqeustDto reqeustDto) {
        if(reqeustDto.isKorean()){
            return reqeustDto.isWhiskyNameAsc() ? review.userWhisky.koreaName.asc() : review.userWhisky.koreaName.desc();
        }
        else{
            return reqeustDto.isWhiskyNameAsc() ? review.userWhisky.koreaName.asc() : review.userWhisky.koreaName.desc();
        }
    }

}
