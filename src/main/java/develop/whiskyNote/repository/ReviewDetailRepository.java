package develop.whiskyNote.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import develop.whiskyNote.dto.ReviewUpsertRequestDto;
import develop.whiskyNote.dto.WhiskyCreateRequestDto;
import develop.whiskyNote.dto.WhiskyListResponseDto;
import develop.whiskyNote.dto.MyWhiskyListResponseDto;
import develop.whiskyNote.entity.Review;
import develop.whiskyNote.entity.User;
import develop.whiskyNote.entity.Whisky;
import develop.whiskyNote.enums.Order;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static develop.whiskyNote.entity.QReview.review;
import static develop.whiskyNote.entity.QWhisky.whisky;
@Repository
public class ReviewDetailRepository {
    private final ReviewRepository reviewRepository;
    private final WhiskyRepository whiskyRepository;
    private final JPAQueryFactory queryFactory;

    public ReviewDetailRepository(ReviewRepository reviewRepository, WhiskyRepository whiskyRepository, JPAQueryFactory queryFactory) {
        this.reviewRepository = reviewRepository;
        this.whiskyRepository = whiskyRepository;
        this.queryFactory = queryFactory;
    }


    public void saveReview(ReviewUpsertRequestDto requestDto, User user, Map<Long, String> imageUrls){
        Review review = Review.builder()
                .content(requestDto.getContent())
                .number(requestDto.getBottleNumber() == null ? 1 : requestDto.getBottleNumber())
                .user(user)
                .imageUrl(imageUrls)
                .isAnonymous(requestDto.getIsAnonymous())
                .tags(requestDto.getTags())
                .openDate(requestDto.getOpenDate())
                .score(requestDto.getScore())
                .regDate(LocalDateTime.now())
                .build();
        reviewRepository.save(review);
    }
    public void saveWhisky(WhiskyCreateRequestDto requestDto, UUID userUuid, String imageUrl){
        Whisky whisky = Whisky.builder()
                .whiskyName(requestDto.getWhiskyName())
                .whiskyCategory(requestDto.getCategory())
                .imageUrl(imageUrl == null ? "test" : imageUrl)
                .strength(requestDto.getStrength())
                .userUuid(userUuid)
                .bottledYear(requestDto.getBottledYear())
                .build();
        whiskyRepository.save(whisky);
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
    public Review findReviewByWhiskyUuid(String whiskyUuid){
        return queryFactory.selectFrom(review)
                .where(Expressions.stringTemplate("HEX({0})", review.whisky.uuid).eq(whiskyUuid.replace("-", "")))
                .fetchOne();
    }
    public void updateReviewByReviewUuid(ReviewUpsertRequestDto requestDto, String reviewUuid, Map<Long, String> imageUrls){
        queryFactory.update(review)
                .set(review.content, requestDto.getContent())
                .set(review.imageUrl, imageUrls)
                .set(review.isAnonymous, requestDto.getIsAnonymous())
                .set(review.openDate, requestDto.getOpenDate())
                .set(review.score, requestDto.getScore())
                .set(review.tags, requestDto.getTags())
                .set(review.modDate, LocalDateTime.now())
                .where(Expressions.stringTemplate("HEX({0})", review.uuid).eq(reviewUuid.replace("-", "")))
                .execute();
    }
    public void deleteReviewByReviewUuid(String reviewUuid){
        queryFactory.delete(review)
                .where(Expressions.stringTemplate("HEX({0})", review.uuid).eq(reviewUuid.replace("-", "")))
                .execute();
    }

    public List<WhiskyListResponseDto> findAllNameLikeWhiskyName(String name){
        return queryFactory.select(Projections.fields(WhiskyListResponseDto.class, whisky.whiskyName.as("whiskyName"), whisky.uuid.as("whiskyUuid")
                        ,new CaseBuilder()
                        .when(review.uuid.isNull())  // review가 없으면
                        .then(false)  // false 반환
                        .otherwise(true)  // 있으면 true 반환
                        .as("isFirst")  // 컬럼 이름 설정))
                ))
                .from(whisky)
                .leftJoin(review)
                .on(whisky.uuid.eq(review.whisky.uuid))
                .where(whisky.whiskyName.like("%" + name + "%"))
                .orderBy(whisky.whiskyName.asc())
                .limit(5)
                .fetch();
    }


    public List<MyWhiskyListResponseDto> findAllMyWhiskyListResponseDto(String name, String category, String nameOrder, String scoreOrder, String dateOrder, UUID userUuid){
        return queryFactory.select(Projections.fields(MyWhiskyListResponseDto.class,
                        whisky.uuid.as("whiskyUuid"),
                        whisky.whiskyName.as("name"),
                        review.score.avg().as("score"), // AVG(score) 사용
                        whisky.bottledYear.as("releaseYear"),
                        whisky.imageUrl.as("photoUrl"),
                        whisky.strength.as("strength"),
                        whisky.whiskyCategory.as("category"),
                        review.regDate.max().as("regDate"), // MAX(regDate)
                        review.modDate.max().as("modDate") // MAX(modDate)
                ))
                .from(whisky)
                .leftJoin(review).on(review.whisky.eq(whisky))
                .where(likeWhiskyName(name))
                .where(eqWhiskyCategory(category))
                .where(whisky.userUuid.eq(userUuid).or(whisky.userUuid.isNull()))
                .groupBy(
                        whisky.uuid,
                        whisky.whiskyName,
                        whisky.bottledYear,
                        whisky.imageUrl,
                        whisky.strength,
                        whisky.whiskyCategory,
                        review.regDate,   // regDate 추가
                        review.modDate    // modDate 추가
                )
                .having(review.score.avg().isNotNull())
                .orderBy(
                        orderByWhiskyName(nameOrder),    // 이름 정렬
                        orderByScore(scoreOrder),         // 점수 정렬
                        orderByRegDate(dateOrder),        // 출시일 정렬
                        review.modDate.max().desc()             // MAX(modDate) 내림차순 정렬
                )
                .fetch();
    }


    private BooleanExpression likeWhiskyName(String name){
        if(name == null || name.isEmpty())
            return null;
        return whisky.whiskyName.like("%" + name + "%");
    }
    private BooleanExpression eqWhiskyCategory(String category){
        if(category == null || category.isEmpty())
            return null;
        return whisky.whiskyCategory.eq(category);
    }
    private OrderSpecifier<?> orderByWhiskyName(String order) {
        if (Order.ASC.getOrder().equals(order) || order.isEmpty())
            return whisky.whiskyName.asc(); // ASC 정렬
        if (Order.DESC.getOrder().equals(order))
            return whisky.whiskyName.desc(); // DESC 정렬
        throw new RuntimeException();
    }
    private OrderSpecifier<?> orderByScore(String order) {
        if (Order.ASC.getOrder().equals(order) || order.isEmpty())
            return review.score.avg().asc();
        if (Order.DESC.getOrder().equals(order))
            return review.score.avg().desc(); // AVG(score) DESC 정렬
        throw new RuntimeException("Invalid order direction for score");
    }
    private OrderSpecifier<?> orderByRegDate(String order) {
        if (Order.ASC.getOrder().equals(order) || order.isEmpty())
            return review.regDate.asc(); // ASC 정렬
        if (Order.DESC.getOrder().equals(order))
            return review.regDate.desc(); // DESC 정렬
        throw new RuntimeException();
    }

}
