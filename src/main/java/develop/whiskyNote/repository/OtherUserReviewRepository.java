package develop.whiskyNote.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import develop.whiskyNote.dto.OtherReviewGetReqeustDto;
import develop.whiskyNote.dto.OtherReviewGetResponseDto;
import develop.whiskyNote.entity.ReviewLikeCount;
import develop.whiskyNote.entity.ReviewLikeMapping;
import develop.whiskyNote.entity.User;
import develop.whiskyNote.enums.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static develop.whiskyNote.entity.QReview.review;
import static develop.whiskyNote.entity.QReviewLikeCount.reviewLikeCount;
import static develop.whiskyNote.entity.QReviewLikeMapping.reviewLikeMapping;
import static develop.whiskyNote.entity.QUserWhisky.userWhisky;
import static develop.whiskyNote.entity.QWhisky.whisky;

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


//    다른 유저 리뷰 읽어오기
    public Page<OtherReviewGetResponseDto> findOtherUserReview(
            OtherReviewGetReqeustDto dto, UUID currentUser, Pageable pageable) {

        BooleanExpression mainSearchCondition = dto.isMainKorean() ?
                review.userWhisky.whisky.koreaName.containsIgnoreCase(dto.getMainSearchWord()) :
                review.userWhisky.whisky.englishName.containsIgnoreCase(dto.getMainSearchWord());

        BooleanExpression subSearchCondition = null;
        if (dto.getSubSearchWord() != null && !dto.getSubSearchWord().isEmpty()) {
            subSearchCondition = dto.isSubKorean() ?
                    review.userWhisky.koreaName.containsIgnoreCase(dto.getSubSearchWord()) :
                    review.userWhisky.englishName.containsIgnoreCase(dto.getSubSearchWord());
        }

        // 정렬 리스트 생성
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        OrderSpecifier<?> likeOrder = getOrderSpecifier(dto.getLikeOrder(), reviewLikeCount.likeCount);
        if (likeOrder != null) orderSpecifiers.add(likeOrder);

        OrderSpecifier<?> scoreOrder = getOrderSpecifier(dto.getScoreOrder(), review.score);
        if (scoreOrder != null) orderSpecifiers.add(scoreOrder);

        OrderSpecifier<?> createdOrder = getOrderSpecifier(dto.getCreatedOrder(),
                Expressions.dateTemplate(LocalDateTime.class, "GREATEST({0}, {1})", review.regDate, review.modDate));
        if (createdOrder != null) orderSpecifiers.add(createdOrder);

        OrderSpecifier<?> nameOrder = getOrderSpecifier(dto.getNameOrder(),
                dto.isSubKorean() ? review.userWhisky.koreaName : review.userWhisky.englishName);
        if (nameOrder != null) orderSpecifiers.add(nameOrder);

        // 본 쿼리
        List<OtherReviewGetResponseDto> content = jpaQueryFactory
                .select(Projections.fields(OtherReviewGetResponseDto.class,
                        review.uuid.as("reviewUuid"),
                        review.userWhisky.uuid.as("userWhiskyUuid"),
                        review.isAnonymous,
                        review.openDate,
                        review.content,
                        review.score,
                        review.tags,
                        review.imageNames.as("imageNames"),
                        Expressions.dateTemplate(LocalDateTime.class, "GREATEST({0}, {1})", review.regDate, review.modDate).as("lastUpdateDate"),
                        Expressions.booleanTemplate("CASE WHEN {0} IS NOT NULL THEN true ELSE false END", reviewLikeMapping.user.uuid).as("likeState"),
                        reviewLikeCount.likeCount.as("likeCount"),
                        reviewLikeCount.uuid.as("reviewLikeCountUuid")
                ))
                .from(review)
                .join(review.userWhisky, userWhisky)
                .join(userWhisky.whisky, whisky)
                .leftJoin(reviewLikeCount).on(reviewLikeCount.reviewId.eq(review.uuid))
                .leftJoin(reviewLikeMapping).on(reviewLikeMapping.reviewLikeCount.eq(reviewLikeCount)
                        .and(reviewLikeMapping.user.uuid.eq(currentUser)))
                .where(
                        review.isAnonymous.isTrue()
                                .and(mainSearchCondition)
                                .and(subSearchCondition != null ? subSearchCondition : Expressions.TRUE)
                )
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트
        Long total = jpaQueryFactory
                .select(review.count())
                .from(review)
                .join(review.userWhisky, userWhisky)
                .join(userWhisky.whisky, whisky)
                .where(
                        review.isAnonymous.isTrue()
                                .and(mainSearchCondition)
                                .and(subSearchCondition != null ? subSearchCondition : Expressions.TRUE)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    private <T extends Comparable<?>> OrderSpecifier<T> getOrderSpecifier(Order order, Expression<T> field) {
        if (order == null) return null;
        return order == Order.ASC ? new OrderSpecifier<>(com.querydsl.core.types.Order.ASC, field)
                : new OrderSpecifier<>(com.querydsl.core.types.Order.DESC, field);
    }



}
