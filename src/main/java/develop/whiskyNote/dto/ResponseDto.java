package develop.whiskyNote.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import enums.Description;
import lombok.*;

import java.util.StringJoiner;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class ResponseDto<T> {

    private Integer code; // return code 200, 400 ...
    private Description description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorDescription;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String accessToken;

    @Override
    public String toString(){
        return new StringJoiner(", ", ResponseDto.class.getSimpleName() + "[", "]")
                .add("code='" + code + "'" )
                .add("description='"+ description + "'")
                .add("errorCode='"+ errorCode + "'")
                .add("errorDescription='"+ errorDescription + "'")
                .add("data=" + data)
                .toString();
    }
}