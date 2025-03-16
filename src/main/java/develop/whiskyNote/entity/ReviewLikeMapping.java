package develop.whiskyNote.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Builder
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
    private UUID reviewLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //OneToMany랑 mapped 되는 필드
    private ReviewLikeCount reviewLikeCount;


    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //OneToMany랑 mapped 되는 필드
    private User user;

}

