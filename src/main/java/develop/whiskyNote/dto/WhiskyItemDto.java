package develop.whiskyNote.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WhiskyItemDto {

    private int year;
    private String name;
    private String category;
    @JsonProperty("cask_type")
    private String caskType;
    @JsonProperty("stated_age")
    private String statedAge;
    private String strength;
    private String image;

    @Builder
    public WhiskyItemDto(String name, String category, String caskType, String statedAge, String strength, String image, String year) {
        this.name = name;
        this.category = category;
        this.caskType = caskType;
        this.statedAge = statedAge;
        this.strength = strength;
        this.image = image;
        this.year = Integer.parseInt(year);
    }
}
