package develop.whiskyNote.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BaseWhiskyRequestDto {
    private List<InputWhiskyDTO> whiskyList;

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class InputWhiskyDTO {
        private String country;
        private String koreaName;
        private String englishName;
        private String category;
        private Double strength;
    }

}
