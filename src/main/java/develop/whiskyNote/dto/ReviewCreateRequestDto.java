package develop.whiskyNote.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import develop.whiskyNote.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.StringJoiner;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewCreateRequestDto {
    private String content;
    private boolean isAnonymous;
    private LocalDate openDate;
    private MultipartFile[] images;
    private String tags;
    private Long score;
    @Override
    public String toString() {
        return new StringJoiner(", ", ReviewCreateRequestDto.class.getSimpleName() + "[", "]")
                .add("content='" + content + "'")
                .add("isAnonymous='" + isAnonymous + "'")
                .add("images.count() =" + Arrays.stream(images).count())
                .add("openDate='" + openDate + "'")
                .add("tags='" + tags + "'")
                .add("score='" + score + "'")
                .toString();
    }
}
