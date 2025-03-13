package develop.whiskyNote.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import develop.whiskyNote.dto.GetOtherReviewListReqeustDto;
import develop.whiskyNote.dto.GetOtherReviewListResponseDto;
import develop.whiskyNote.entity.Review;
import develop.whiskyNote.entity.ReviewLikeCount;
import org.springframework.stereotype.Repository;

import static develop.whiskyNote.entity.QReview.review;

import java.time.LocalDateTime;
import java.util.List;

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
    public void save(Review review) {
        ReviewLikeCount reviewLikeCount = ReviewLikeCount.builder().reviewId(review.getUuid()).build();
        reviewLikeCountRepository.save(reviewLikeCount);
    }

    //좋아요 누르기

    //좋아요 취소하기

    //다른 유저 리뷰 읽어오기
    public List<GetOtherReviewListResponseDto> findOtherUserReview(GetOtherReviewListReqeustDto getOtherReviewListReqeustDto ) {
        return jpaQueryFactory.select(
                Projections.fields(GetOtherReviewListResponseDto.class,
                            review.uuid.as("reviewUuid"),
                            review.userWhisky.uuid.as("userWhiskyUuid"),
                            review.isAnonymous,//없어도 될거 같은데 득환이가 넣어달라함
                            review.openDate,
                            review.content,
                            review.score,
                            review.tags,
                            review.imageNames.as("imageNames"),
                            Expressions.dateTemplate(LocalDateTime.class, "GREATEST({0}, {1})", review.regDate, review.modDate)
                                .as("lastUpdateDate")
//                            reviewLike.review.isNotNull().as("likeUuid"),

                        ))
                .from(review)
//                .leftJoin(reviewLike).on(reviewLike.review)
                .fetch();
    }

}
