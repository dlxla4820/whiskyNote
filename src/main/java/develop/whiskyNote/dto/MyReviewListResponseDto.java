package develop.whiskyNote.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MyReviewListResponseDto {
    private String reviewUuid;
    private String content;
    private Map<Long, String> imageUrl;
    private Long score;
    private LocalDateTime openDate;
    private List<String> tags;
    @Override
    public String toString() {
        return new StringJoiner(", ", MyReviewListResponseDto.class.getSimpleName() + "[", "]")
                .add("reviewUuid='" + reviewUuid + "'")
                .add("content='" + content + "'")
                .add("score='" + score + "'")
                .add("openDate='" + openDate + "'")
                .add("tags='" + tags + "'")
                .toString();
    }

}
