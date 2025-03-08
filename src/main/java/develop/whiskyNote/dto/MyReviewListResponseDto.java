package develop.whiskyNote.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MyReviewListResponseDto {
    private UUID reviewUuid;
    private String content;
    private List<String> imageNames;
    private Long score;
    private LocalDate openDate;
    private List<String> tags;
    @Override
    public String toString() {
        return new StringJoiner(", ", MyReviewListResponseDto.class.getSimpleName() + "[", "]")
                .add("reviewUuid='" + reviewUuid + "'")
                .add("content='" + content + "'")
                .add("imageNames" + imageNames + "'")
                .add("score='" + score + "'")
                .add("openDate='" + openDate + "'")
                .add("tags='" + tags + "'")
                .toString();
    }

}
