package develop.whiskyNote.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.StringJoiner;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WhiskyCreateRequestDto {
    private String whiskyName;
    private String category;
    private Double strength;
    private Integer bottledYear;
    @Override
    public String toString() {
        return new StringJoiner(", ", WhiskyCreateRequestDto.class.getSimpleName() + "[", "]")
                .add("whiskyName='" + whiskyName + "'")
                .add("category='" + category + "'")
                .add("strength='" + strength + "'")
                .add("bottledYear='" + bottledYear + "'")
                .toString();
    }
}
