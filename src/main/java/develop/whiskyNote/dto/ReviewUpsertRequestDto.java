package develop.whiskyNote.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewUpsertRequestDto {
    private String content;
    private Boolean isAnonymous;
    private LocalDate openDate;
    private List<String> tags;
    private Long score;
    @Override
    public String toString() {
        return new StringJoiner(", ", ReviewUpsertRequestDto.class.getSimpleName() + "[", "]")
                .add("content='" + content + "'")
                .add("isAnonymous='" + isAnonymous + "'")
                .add("openDate='" + openDate + "'")
                .add("tags='" + tags + "'")
                .add("score='" + score + "'")
                .toString();
    }

}
