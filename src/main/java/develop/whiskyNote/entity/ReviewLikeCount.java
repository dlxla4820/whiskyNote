package develop.whiskyNote.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Table(name="review_like_count")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewLikeCount {
    @Id
    @Builder.Default
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid = UUID.randomUUID();

    @Column(name="review_id",nullable = false)//외래키 사용지 않는 이유 -> discord 20250313-2 검색
    private UUID reviewId;

    @Builder.Default
    @Column(name = "like_count")
    private Integer likeCount = 0;

    //리뷰에 좋아요 누른 사람 리스트
    @OneToMany(mappedBy = "reviewLikeCount", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReviewLikeMapping> reviewLikeMapping = new ArrayList<>();

    //리뷰 삭제 시에 삭제
    //리뷰의 isAnnonymous가 False이면 생성 안되어야 함
    //좋아요 삭제되면 삭제되어야 함
}
