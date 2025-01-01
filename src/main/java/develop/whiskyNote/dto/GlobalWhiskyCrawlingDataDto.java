package develop.whiskyNote.dto;


import lombok.*;

@Getter
@Setter
public class GlobalWhiskyCrawlingDataDto {

    private Integer bottledYear;
    private String whiskyName;
    private String whiskyCategory;
    private Double strength;
    private String imageUrl;

}
