package develop.whiskyNote.repository;

import com.querydsl.core.QueryFactory;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import develop.whiskyNote.dto.ReviewCreateRequestDto;
import develop.whiskyNote.dto.ReviewResponseDto;
import develop.whiskyNote.entity.Review;
import develop.whiskyNote.entity.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import static develop.whiskyNote.entity.QReview.review;
@Repository
public class ReviewDetailRepository {
    private final ReviewRepository reviewRepository;
    private final JPAQueryFactory queryFactory;

    public ReviewDetailRepository(ReviewRepository reviewRepository, JPAQueryFactory queryFactory) {
        this.reviewRepository = reviewRepository;
        this.queryFactory = queryFactory;
    }


    public void saveReview(ReviewCreateRequestDto requestDto, User user, Map<Long, String> imageUrls){
        Review review = Review.builder()
                .content(requestDto.getContent())
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

    public ReviewResponseDto findReviewByUserUuid(UUID userUuid){
        return queryFactory.select(Projections.fields(ReviewResponseDto.class, review.content, review.imageUrl.as("imageUrl")
                                ,review.isAnonymous.as("is_anonymous"),review.tags, review.openDate.as("openDate"), review.score))
                .from(review)
                .where(review.user.uuid.eq(userUuid))
                .fetchOne();
    }
}
