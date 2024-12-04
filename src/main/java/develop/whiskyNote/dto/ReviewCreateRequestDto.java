package develop.whiskyNote.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import develop.whiskyNote.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.StringJoiner;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateRequestDto {
    private String title;
    private String content;
    MultipartFile[] images;

    @Override
    public String toString() {
        return new StringJoiner(", ", ReviewCreateRequestDto.class.getSimpleName() + "[", "]")
                .add("title='" + title + "'")
                .add("content='" + content + "'")
                .add("images.count() =" + Arrays.stream(images).count())
                .toString();
    }
}
