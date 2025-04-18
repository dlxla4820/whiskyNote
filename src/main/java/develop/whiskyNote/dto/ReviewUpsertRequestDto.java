package develop.whiskyNote.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import develop.whiskyNote.entity.Review;
import develop.whiskyNote.entity.User;
import develop.whiskyNote.entity.UserWhisky;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ToString
public class ReviewUpsertRequestDto {
    private String myWhiskyUuid;
    private String content;
    private Boolean isAnonymous;
    private LocalDate openDate;
    private List<String> tags;
    private Long score;
    private List<String> imageNames;

    public Review toReview(UserWhisky userWhisky, User user){
        return Review.builder()
                .uuid(UUID.randomUUID())
                .userWhisky(userWhisky)
                .content(this.content)
                .user(user)
                .imageNames(this.imageNames)
                .isAnonymous(this.isAnonymous)
                .tags(this.tags)
                .openDate(this.openDate)
                .score(this.score)
                .regDate(LocalDateTime.now())
                .modDate(LocalDateTime.now())
                .build();
    }
}
