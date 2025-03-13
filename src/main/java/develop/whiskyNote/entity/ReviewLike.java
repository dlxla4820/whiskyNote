package develop.whiskyNote.entity;


import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Getter
@Table(name="review_like",
    indexes = {
        @Index(name="idx_review_id", columnList = "reviewId"),
        @Index(name="idx_user_id", columnList = "userId")
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewLike {
    //해당 테이블에 데이터가 추가되면 좋아요를 누른 것
    //삭제되면 좋아요를 삭제한 것
    @Id
    private String reviewLikeId;
    @Column(nullable = false)
    private UUID reviewId;
    @Column(nullable = false)
    private UUID userId;

    @Builder
    public ReviewLike(UUID reviewId, UUID userId) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.reviewLikeId = generateReviewLikeId(reviewId, userId);
    }

    private static String generateReviewLikeId(UUID reviewId, UUID userId) {
        return(reviewId.toString() + userId.toString()).replaceAll("-", "");
    }

}

