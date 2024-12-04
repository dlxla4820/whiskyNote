package develop.whiskyNote.repository;

import develop.whiskyNote.dto.ReviewCreateRequestDto;
import develop.whiskyNote.entity.Review;
import develop.whiskyNote.entity.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;

@Repository
public class ReviewDetailRepository {
    private final ReviewRepository reviewRepository;

    public ReviewDetailRepository(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void saveReview(ReviewCreateRequestDto requestDto, User user, Map<Long, String> imageUrls){
        Review review = Review.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .user(user)
                .imageUrl(imageUrls)
                .regDate(LocalDateTime.now())
                .build();
        reviewRepository.save(review);
    }
}
