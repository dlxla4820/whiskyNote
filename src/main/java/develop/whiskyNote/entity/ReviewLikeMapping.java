package develop.whiskyNote.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Table(name="review_like_mapping")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewLikeMapping {
    //해당 테이블에 데이터가 추가되면 좋아요를 누른 것
    //삭제되면 좋아요를 삭제한 것
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String reviewLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="like_count_id", nullable=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ReviewLikeCount reviewLikeCount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

}

