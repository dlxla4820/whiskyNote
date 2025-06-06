package develop.whiskyNote.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import develop.whiskyNote.dto.*;
import develop.whiskyNote.entity.Review;
import develop.whiskyNote.entity.User;
import develop.whiskyNote.entity.UserWhisky;
import develop.whiskyNote.entity.Whisky;
import develop.whiskyNote.enums.Order;
import develop.whiskyNote.utils.CommonUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static develop.whiskyNote.entity.QReview.review;
import static develop.whiskyNote.entity.QWhisky.whisky;
import static develop.whiskyNote.entity.QUserWhisky.userWhisky;
@Repository
public class ReviewDetailRepository {
    private final ReviewRepository reviewRepository;
    private final UserWhiskyRepository userWhiskyRepository;
    private final JPAQueryFactory queryFactory;

    public ReviewDetailRepository(ReviewRepository reviewRepository, UserWhiskyRepository userWhiskyRepository, JPAQueryFactory queryFactory) {
        this.reviewRepository = reviewRepository;
        this.userWhiskyRepository = userWhiskyRepository;
        this.queryFactory = queryFactory;
    }


    public void saveReview(ReviewUpsertRequestDto requestDto, User user, UserWhisky userWhisky){
        Review review = Review.builder()
                .userWhisky(userWhisky)
                .content(requestDto.getContent())
                .user(user)
                .imageNames(requestDto.getImageNames())
                .isAnonymous(requestDto.getIsAnonymous())
                .tags(requestDto.getTags())
                .openDate(requestDto.getOpenDate())
                .score(requestDto.getScore())
                .regDate(LocalDateTime.now())
                .build();
        reviewRepository.save(review);
    }

    public Review findReviewByReviewUuid(String reviewUuid){
        return queryFactory.selectFrom(review)
                .where(Expressions.stringTemplate("HEX({0})", review.uuid).eq(reviewUuid.replace("-", "")))
                .fetchOne();
    }
    public Review findReviewByUserUuid(UUID userUuid){
        return queryFactory.selectFrom(review)
                .where(review.user.uuid.eq(userUuid))
                .fetchOne();
    }
    public Review findReviewByWhiskyUuid(String userWhiskyUuid){
        return queryFactory.selectFrom(review)
                .where(Expressions.stringTemplate("HEX({0})", review.userWhisky.uuid).eq(userWhiskyUuid.replace("-", "")))
                .fetchOne();
    }

    public List<MyReviewListResponseDto> findMyReviewListByUserWhiskyUuid(String userWhiskyUuid,UUID userUuid, String order){
        return queryFactory.select(Projections.fields(MyReviewListResponseDto.class, review.uuid.as("reviewUuid"),review.imageNames.as("imageNames"), review.content, review.score, review.tags, review.openDate))
                .from(review)
                .where(Expressions.stringTemplate("HEX({0})", review.userWhisky.uuid).eq(userWhiskyUuid.replace("-", "")))
                .where(review.user.uuid.eq(userUuid))
                .orderBy(orderByRegDate(order))
                .fetch();
    }

    public void updateReviewByReviewUuid(ReviewUpsertRequestDto requestDto, String reviewUuid){
        queryFactory.update(review)
                .set(review.content, requestDto.getContent())
                .set(review.imageNames, requestDto.getImageNames())
                .set(review.isAnonymous, requestDto.getIsAnonymous())
                .set(review.openDate, requestDto.getOpenDate())
                .set(review.score, requestDto.getScore())
                .set(review.tags, requestDto.getTags())
                .set(review.modDate, LocalDateTime.now())
                .where(Expressions.stringTemplate("HEX({0})", review.uuid).eq(reviewUuid.replace("-", "")))
                .execute();
    }

    public long updateUserWhisky(UserWhiskyDto requestDto, UUID userWhiskyUuid, String imageName){
        return queryFactory.update(userWhisky)
                .set(userWhisky.koreaName, requestDto.getKoreaName())
                .set(userWhisky.englishName, requestDto.getEnglishName())
                .set(userWhisky.category, requestDto.getCategory())
                .set(userWhisky.strength, requestDto.getStrength())
                .set(userWhisky.country, requestDto.getCountry())
                .set(userWhisky.imageName, imageName)
                .set(userWhisky.bottledYear, requestDto.getBottledYear())
                .set(userWhisky.caskType, requestDto.getCaskType())
                .set(userWhisky.openDate, requestDto.getOpenDate())
                .set(userWhisky.memo, requestDto.getMemo())
                .set(userWhisky.modDate, LocalDateTime.now())
                .where(userWhisky.uuid.eq(userWhiskyUuid))
                .execute();
    }

    public long updateUserWhiskyLastRegReview(UUID userWhiskyUuid){
        return queryFactory.update(userWhisky)
                .set(userWhisky.lastRegReview, LocalDateTime.now())
                .where(userWhisky.uuid.eq(userWhiskyUuid))
                .execute();
    }


    public void deleteReviewByReviewUuid(String reviewUuid){
        queryFactory.delete(review)
                .where(Expressions.stringTemplate("HEX({0})", review.uuid).eq(reviewUuid.replace("-", "")))
                .execute();
    }

/*    public List<WhiskyListResponseDto> findAllNameLikeWhiskyName(String name, String category){
        return queryFactory.select(Projections.fields(WhiskyListResponseDto.class, (CommonUtils.containsKorean(name) ? whisky.koreaName.as("whiskyName") : whisky.englishName("whiskName")), whisky.uuid.as("whiskyUuid")
                        ,new CaseBuilder()
                        .when(review.uuid.isNull())  // review가 없으면
                        .then(false)  // false 반환
                        .otherwise(true)  // 있으면 true 반환
                        .as("isFirst")  // 컬럼 이름 설정))
                ))
                .from(whisky)
                .leftJoin(review)
                .on(whisky.uuid.eq(review.whisky.uuid))
                .where(likeWhiskyName(name))
                .where(eqWhiskyCategory(category))
                .orderBy(whisky.whiskyName.asc())
                .limit(5)
                .fetch();
    }*/
    public List<WhiskyDto> findAllNameListWhiskyName(String name, String category){
        return queryFactory.select(Projections.fields(WhiskyDto.class, whisky.uuid.as("whiskyUuid"), whisky.koreaName.as("koreaName"),
                whisky.englishName.as("englishName"), whisky.category.as("category"), whisky.strength.as("strength"), whisky.country.as("country")))
                .from(whisky)
                .where(likeWhiskyName(name))
                .where(eqWhiskyCategory(category))
                .orderBy(whisky.koreaName.asc())
                .orderBy(whisky.englishName.asc())
                .limit(5)
                .fetch();
    }



    public List<MyWhiskyListResponseDto> findAllMyWhiskyListResponseDto(String name, String category, String openDateOrder, String scoreOrder, String dateOrder, UUID userUuid){
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        OrderSpecifier<?> openDateOrderBy = orderByUserWhiskyOpenDateOrder(openDateOrder);
        OrderSpecifier<?> scoreOrderBy = orderByScore(scoreOrder);
        OrderSpecifier<?> lastRegReviewOrderBy = orderByLastRegReview(dateOrder);

        if (openDateOrderBy != null) orderSpecifiers.add(openDateOrderBy);
        if (scoreOrderBy != null) orderSpecifiers.add(scoreOrderBy);
        if (lastRegReviewOrderBy != null) orderSpecifiers.add(lastRegReviewOrderBy);

        orderSpecifiers.add(review.modDate.max().desc());


        return queryFactory.select(Projections.constructor(MyWhiskyListResponseDto.class,
                        userWhisky.uuid.as("whiskyUuid"),
                        userWhisky.koreaName.as("koreaName"),
                        userWhisky.englishName.as("englishName"),
                        new CaseBuilder()
                                .when(review.score.avg().isNull())
                                .then(0.0)
                                .otherwise(review.score.avg()).as("score"),
                        userWhisky.country.as("country"),
                        userWhisky.bottledYear.as("bottledYear"),
                        userWhisky.imageName.as("imageName"),
                        userWhisky.strength.as("strength"),
                        userWhisky.category.as("category"),
                        userWhisky.caskType.as("caskType"),
                        userWhisky.openDate.as("openDate"),
                        userWhisky.memo.as("memo"),
                        userWhisky.lastRegReview.as("lastRegReview")
                ))
                .from(userWhisky)
                .leftJoin(review).on(review.userWhisky.eq(userWhisky))
                .where(likeUserWhiskyName(name))
                .where(eqUserWhiskyCategory(category))
                .where(userWhisky.userUuid.eq(userUuid))
                .groupBy(
                        userWhisky.uuid,
                        userWhisky.koreaName,
                        userWhisky.englishName,
                        userWhisky.bottledYear,
                        userWhisky.imageName,
                        userWhisky.strength,
                        userWhisky.category,
                        userWhisky.caskType,
                        userWhisky.openDate,
                        userWhisky.memo,
                        userWhisky.lastRegReview
                )
                //.having(review.score.avg().isNotNull())
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .fetch();
    }

    public void deleteUserWhisky(String userWhiskyUuid, UUID userUuid){
        queryFactory.delete(userWhisky)
                .where(Expressions.stringTemplate("HEX({0})", userWhisky.uuid).eq(userWhiskyUuid.replace("-", "")))
                .where(userWhisky.userUuid.eq(userUuid))
                .execute();
    }

    private BooleanExpression likeWhiskyName(String name){
        if(name == null || name.isEmpty())
            return null;
        if(CommonUtils.containsKorean(name))
            return whisky.koreaName.like("%" + name + "%");
        return whisky.englishName.like("%" + name + "%");

    }
    private BooleanExpression likeUserWhiskyName(String name){
        if(name == null || name.isEmpty())
            return null;
        if(CommonUtils.containsKorean(name))
            return userWhisky.koreaName.like("%" + name + "%");
        return userWhisky.englishName.like("%" + name + "%");

    }
    private BooleanExpression eqWhiskyCategory(String category){
        if(category == null || category.isEmpty())
            return null;
        return whisky.category.eq(category);
    }
    private BooleanExpression eqUserWhiskyCategory(String category){
        if(category == null || category.isEmpty())
            return null;
        return userWhisky.category.eq(category);
    }
    private OrderSpecifier<?> orderByUserWhiskyOpenDateOrder(String order) {
        if (Order.ASC.getOrder().equals(order))
            return userWhisky.openDate.asc(); // ASC 정렬
        if (Order.DESC.getOrder().equals(order))
            return userWhisky.openDate.desc(); // DESC 정렬
        return null;
    }
    @Deprecated
    private OrderSpecifier<?> orderByUserWhiskyKoreaName(String order) {
        if (Order.ASC.getOrder().equals(order) || order.isEmpty())
            return userWhisky.koreaName.asc(); // ASC 정렬
        if (Order.DESC.getOrder().equals(order))
            return userWhisky.koreaName.desc(); // DESC 정렬
        return null;
    }
    @Deprecated
    private OrderSpecifier<?> orderByUserWhiskyEnglishName(String order) {
        if (Order.ASC.getOrder().equals(order) || order.isEmpty())
            return userWhisky.koreaName.asc(); // ASC 정렬
        if (Order.DESC.getOrder().equals(order))
            return userWhisky.koreaName.desc(); // DESC 정렬
        return null;
    }
    private OrderSpecifier<?> orderByScore(String order) {
        if (Order.ASC.getOrder().equals(order))
            return review.score.avg().asc();
        if (Order.DESC.getOrder().equals(order))
            return review.score.avg().desc(); // AVG(score) DESC 정렬
        return null;
    }
    private OrderSpecifier<?> orderByLastRegReview(String order) {
        if (Order.ASC.getOrder().equals(order))
            return userWhisky.lastRegReview.asc(); // ASC 정렬
        if (Order.DESC.getOrder().equals(order))
            return userWhisky.lastRegReview.desc(); // DESC 정렬
        return null;
    }
    private OrderSpecifier<?> orderByRegDate(String order) {
        if (Order.ASC.getOrder().equals(order))
            return review.regDate.asc(); // ASC 정렬
        if (Order.DESC.getOrder().equals(order))
            return review.regDate.desc(); // DESC 정렬
        return null;
    }

}
