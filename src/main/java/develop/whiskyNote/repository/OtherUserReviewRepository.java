package develop.whiskyNote.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import develop.whiskyNote.dto.OtherReviewGetReqeustDto;
import develop.whiskyNote.dto.OtherReviewGetResponseDto;
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
import java.util.UUID;


import static develop.whiskyNote.entity.QReview.review;
import static develop.whiskyNote.entity.QReviewLikeMapping.reviewLikeMapping;
import static develop.whiskyNote.entity.QUserWhisky.userWhisky;
import static develop.whiskyNote.entity.QWhisky.whisky;

@Repository
public class OtherUserReviewRepository {
    private final WhiskyRepository whiskyRepository;
    private final UserRepository userRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final ReviewLikeMappingRepository reviewLikeMappingRepository;



    public OtherUserReviewRepository(WhiskyRepository whiskyRepository, UserRepository userRepository, JPAQueryFactory jpaQueryFactory, ReviewLikeMappingRepository reviewLikeMappingRepository) {
        this.whiskyRepository = whiskyRepository;
        this.userRepository = userRepository;
        this.jpaQueryFactory = jpaQueryFactory;
        this.reviewLikeMappingRepository = reviewLikeMappingRepository;
    }

    public ReviewLikeMapping checkReviewLikeMapping(UUID reviewId, UUID userId) {
        return jpaQueryFactory.selectFrom(reviewLikeMapping)
                .where(reviewLikeMapping.reviewId.eq(reviewId))//review의 id
                .where(reviewLikeMapping.user.uuid.eq(userId))//user의 id
                .fetchOne();
    }

    //좋아요 누르기
    public void saveLikeMapping(UUID reviewId, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found for UUID: " + userId));
        ReviewLikeMapping reviewLikeMapping = ReviewLikeMapping.builder()
                .reviewId(reviewId)
                .user(user)
                .build();
        reviewLikeMappingRepository.save(reviewLikeMapping);
    }


    //좋아요 취소하기

    public void deleteLikeMapping(UUID reviewId, UUID userId) {
        jpaQueryFactory.delete(reviewLikeMapping)
                .where(reviewLikeMapping.user.uuid.eq(userId))
                .where(reviewLikeMapping.reviewId.eq(reviewId))
                .execute();
    }

    //base whisky에 main 검색어 존재하는지 확인
    public boolean checkBaseWhiskyExist(OtherReviewGetReqeustDto reqeustDto) {
        return reqeustDto.isMainKorean()? whiskyRepository.existsByKoreaNameContainingIgnoreCase(reqeustDto.getMainSearchWord()) : whiskyRepository.existsByEnglishNameContainingIgnoreCase(reqeustDto.getMainSearchWord());
    }

//    다른 유저 리뷰 읽어오기
public Page<OtherReviewGetResponseDto> findOtherUserReview(
        OtherReviewGetReqeustDto dto, UUID currentUser, Pageable pageable) {

    BooleanExpression mainSearchCondition = dto.isSearchFromBaseWhisky()
            ? (dto.isMainKorean()
            ? userWhisky.whisky.koreaName.containsIgnoreCase(dto.getMainSearchWord())
            : userWhisky.whisky.englishName.containsIgnoreCase(dto.getMainSearchWord()))
            : (dto.isMainKorean()
            ? userWhisky.koreaName.containsIgnoreCase(dto.getMainSearchWord())
            : userWhisky.englishName.containsIgnoreCase(dto.getMainSearchWord()));
    BooleanExpression subSearchCondition = null;
    if (dto.getSubSearchWord() != null && !dto.getSubSearchWord().isEmpty()) {
        subSearchCondition = dto.isSubKorean() ?
                review.userWhisky.koreaName.containsIgnoreCase(dto.getSubSearchWord()) :
                review.userWhisky.englishName.containsIgnoreCase(dto.getSubSearchWord());
    }

    NumberExpression<Long> likeCountExpr = reviewLikeMapping.reviewId.count().as("likeCount");

    // 정렬 리스트 생성
    List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

    OrderSpecifier<?> likeOrder = getOrderSpecifier(dto.getLikeOrder(), likeCountExpr);
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
                    review.content,
                    review.isAnonymous,
                    review.openDate,
                    review.score,
                    review.tags,
                    review.imageNames,
                    Expressions.dateTemplate(LocalDateTime.class, "GREATEST({0}, {1})", review.regDate, review.modDate).as("lastUpdateDate"),
                    // 좋아요 여부
                    reviewLikeMapping.user.uuid.eq(currentUser).count().goe(1L).as("likeState"),
                    // 좋아요 수 → Integer로 캐스팅
                    reviewLikeMapping.reviewId.count().intValue().as("likeCount")
            ))
            .from(review)
            .leftJoin(reviewLikeMapping).on(reviewLikeMapping.reviewId.eq(review.uuid))
            .join(review.userWhisky, userWhisky)
            .join(userWhisky.whisky, whisky)
            .where(
                    review.isAnonymous.isTrue()
                            .and(mainSearchCondition)
                            .and(subSearchCondition != null ? subSearchCondition : Expressions.TRUE)
            )
            .groupBy(review.uuid)
            .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    // 카운트 쿼리는 group by 제거한 기본 count
    Long total = jpaQueryFactory
            .select(review.countDistinct())
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


    public Integer getLikeCount(UUID reviewUuid) {
        Long count = jpaQueryFactory.select(reviewLikeMapping.count())
                .from(reviewLikeMapping)
                .where(reviewLikeMapping.reviewId.eq(reviewUuid))
                .fetchOne();

        return count != null ? count.intValue() : 0;
    }
}
